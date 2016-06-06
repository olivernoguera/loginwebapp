package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseForbidden;
import com.onoguera.loginwebapp.view.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.onoguera.loginwebapp.view.ResponseUnsupportedMediaType;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by oliver on 1/06/16.
 */
public abstract class BaseController implements Controller {

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
                             final Headers headers) {
        if (!checkMethodAllowed(method)) {
            LOGGER.warn(String.format("Method %s not allowed", method));
            return new ResponseMethodNotAllowed();
        }

        Request request;
        try {
            request = this.getRequest(requestURI.getQuery(), requestURI.getPath(), requestBody, headers);
        } catch (IOException io) {
            LOGGER.warn("Bad request.", io);
            return new ResponseBadRequest();
        }

        if (this instanceof AuthController) {
            List<String> authorizations = headers.get("Authorization");
            List<Role> roles = this.getRoles(authorizations);
            if (roles.isEmpty()) {
                return new ResponseUnauthorized();
            }
            if (!validMediaType(request.getRawBody(), method, headers)) {
                return new ResponseUnsupportedMediaType();
            }
            if (!method.equals(METHOD_GET)) {
                //Modify and create and delete
                Optional<Role> writRole = roles.stream().filter(r -> r.isWriteAccess()).findFirst();
                if (!writRole.isPresent()) {
                    return new ResponseForbidden();
                }
            }
        }

        return dispatch(request, method);
    }

    private Response dispatch(final Request request, final String method) {
        if (METHOD_GET.equals(method)) {
            return doGet(request);
        } else if (METHOD_POST.equals(method)) {
            return doPost(request);
        } else if (METHOD_PUT.equals(method)) {
            return doPut(request);
        } else {
            //only delete
            return doDelete(request);
        }
    }

    protected boolean checkMethodAllowed(final String method) {
        if (METHOD_GET.equals(method)) {
            return true;
        } else if (METHOD_POST.equals(method)) {
            return true;
        } else if (METHOD_PUT.equals(method)) {
            return true;
        } else if (METHOD_DELETE.equals(method)) {
            return true;
        }
        return false;
    }

    private boolean validMediaType(String rawBody, String method, Headers headers) {
        if (method.equals(METHOD_GET) || method.equals(METHOD_DELETE)) {
            return true;
        }
        if (rawBody == null || rawBody.length() == 0) {
            return true;
        }

        if (!this.isApplicationJson(headers)) {
            return false;
        }
        return true;
    }

    protected boolean isApplicationJson(Headers headers) {
        List<String> contentTypes = headers.get("Content-type");
        if (contentTypes == null) {
            return false;
        }
        Optional<String> exists =
                contentTypes.stream().filter(ct -> ct.equals("application/json")).findFirst();
        return exists.isPresent();
    }

    private Request getRequest(final String query, final String path, InputStream requestBody, Headers headers) throws IOException {

        Map<String, String> queryParams = this.parseQueryParams(query);
        Map<String, String> pathParams =
                this.parsePathParams(path, this.getPathParams(), this.getURLPattern());
        String rawBody = this.parseFirstRequestBody(requestBody);
        if (isApplicationJson(headers)) {
            return new JsonRequest(queryParams, pathParams, rawBody);
        }
        return new Request(queryParams, pathParams, rawBody);
    }

    public static Map<String, String> parsePathParams(final String path,
                                                      List<String> params,
                                                      Pattern pattern) {
        Map<String, String> result = new HashMap<>();
        if (params != null && !params.isEmpty()) {
            Matcher m = pattern.matcher(path);
            if (m.find()) {
                for (String param : params) {
                    String value = m.group(param);
                    if (value != null && !value.trim().equals(EMPTY_STRING)) {
                        result.put(param, value);
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
    private Map<String, String> parseQueryParams(final String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            //TODO examinate header to change charset enconding
            List<NameValuePair> paramsPair = URLEncodedUtils.parse(query, Charset.defaultCharset());
            paramsPair.forEach(pair -> {
                queryParams.put(pair.getName(), pair.getValue());
            });
        }
        return queryParams;
    }



    public String parseFirstRequestBody(InputStream requestBody) throws IOException {

        if (requestBody == null) {
            return null;
        }

        InputStreamReader isr =  new InputStreamReader(requestBody,Charset.defaultCharset());
        BufferedReader br = new BufferedReader(isr);
        int b;
        StringBuilder buf = new StringBuilder();
        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();

        if (buf.toString().isEmpty()) {
            return null;
        }

        return buf.toString();

    }


    /**
     * @param authorizations
     * @return auth ok
     */
    private List<Role> getRoles(List<String> authorizations) {
        List<Role> roles = new ArrayList<>();
        if (authorizations == null) {
            return roles;
        }
        for (String auth : authorizations) {
            if (auth != null && auth.startsWith("Basic")) {
                // Authorization: Basic base64credentials
                String base64Credentials = auth.substring("Basic".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                        Charset.defaultCharset());
                // credentials = username:password
                final String[] values = credentials.split(":", 2);
                if (values.length < 2) {
                    continue;
                }
                User user = new User(values[0], values[1]);
                roles.addAll(userService.getRoles(user));
            }
        }
        return roles;
    }

}
