package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.sun.net.httpserver.Headers;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by oliver on 1/06/16.
 */
public abstract class BaseController  implements  Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private UserService userService = UserService.getInstance();


    private final static String METHOD_POST = "POST";
    private final static String METHOD_GET = "GET";
    private final static String METHOD_PUT = "PUT";
    private final static String METHOD_DELETE = "DELETE";

    private static final String EMPTY_STRING = "";

    public abstract Pattern getURLPattern();

    public abstract List<String> getPathParams();

    public abstract Response doGet(final Request request);

    public abstract Response doPost(final Request request);

    public abstract Response doPut(final Request request);

    public abstract Response doDelete(final Request request);


    @Override
    public boolean filter(String contextPath) {
        if (contextPath == null) {
            return false;
        }
        return getURLPattern().matcher(contextPath).matches();
    }

    @Override
    public Response dispatch(final URI requestURI,
                             final InputStream requestBody,
                             final String method,
                             final Headers headers)
    {
        Request request;
        try {
            request = this.getRequest(requestURI.getQuery(), requestURI.getPath(), requestBody);
        }catch (IOException io){
            LOGGER.warn("Bad request." ,io);
            return new ResponseBadRequest();
        }

        if( this instanceof AuthController){
            List<String> authorizations = headers.get("Authorization");
            if(!this.validateAuthorizations(authorizations)){
                return new ResponseUnauthorized();
            }
        }

        if (METHOD_GET.equals(method)) {
            return doGet(request);
        } else if (METHOD_POST.equals(method)) {
            return doPost(request);
        } else if (METHOD_PUT.equals(method)) {
            return doPut(request);
        } else if (METHOD_DELETE.equals(method)) {
            return doDelete(request);
        } else{
            LOGGER.warn(String.format("Method %s not allowed", method));
            return new ResponseMethodNotAllowed();
        }
    }

    private Request getRequest(final String query, final String path, InputStream requestBody) throws IOException {

        Map<String, String> queryParams = this.parseQueryParams(query);
        Map<String, String> pathParams =
                this.parsePathParams(path, this.getPathParams(), this.getURLPattern());
        String rawBody = this.parseFirstRequestBody(requestBody);
        return new Request(queryParams,pathParams,rawBody);
    }

    public static Map<String, String> parsePathParams(final String path,
                                                      List<String> params,
                                                      Pattern pattern ) {
        Map<String, String> result = new HashMap<>();
        if (params != null && !params.isEmpty()) {
            Matcher m = pattern.matcher(path);
            if (m.find()) {
                for(String param: params){
                    String value = m.group(param);
                    if( value != null && !value.trim().equals(EMPTY_STRING)){
                        result.put(param,value);
                    }
                }
            }

        }
        return result;
    }

    /**
     * @param query uri
     * @return
     */
    private Map<String,String> parseQueryParams(final String query) {
        Map<String, String> queryParams = new HashMap<>();
        if( query != null){
            //TODO examinate header to change charset enconding
            List<NameValuePair> paramsPair = URLEncodedUtils.parse(query, Charset.defaultCharset());
            paramsPair.forEach(pair -> {
                queryParams.put(pair.getName(), pair.getValue());
            });
        }
        return queryParams;
    }


    private String parseFirstRequestBody(InputStream requestBody) throws IOException {

        if (requestBody == null) {
            return null;
        }

        String requestRawBody = EMPTY_STRING;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody))) {
            requestRawBody += reader.readLine();
        }

        if (requestRawBody.equals(EMPTY_STRING)) {
            return null;
        }

        return requestRawBody;
    }

    /**
     *
     * @param authorizations
     * @return auth ok TODO must be return role, there are read only roles and write roles
     */
    private boolean validateAuthorizations(List<String> authorizations) {
        if( authorizations == null){
            return false;
        }
        for( String auth: authorizations){
            if (auth != null && auth.startsWith("Basic")) {
                // Authorization: Basic base64credentials
                String base64Credentials = auth.substring("Basic".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                        Charset.defaultCharset());
                // credentials = username:password
                final String[] values = credentials.split(":",2);
                if( values.length < 2){
                    continue;
                }
                User user = new User(values[0],values[1]);
                if( userService.validateAdmin(user)){
                    return true;
                }

            }
        }
        return false;
    }

}