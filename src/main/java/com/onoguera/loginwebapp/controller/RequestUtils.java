package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.sun.net.httpserver.Headers;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains some functions to read and parse elements of request.
 *
 * Created by oliver on 7/06/16.
 */
public final  class RequestUtils {

    private static final String COOKIE = "Cookie";
    private static final String EMPTY_STRING = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtils.class);
    /**
     * Protect instance final for final utility class
     */
    private RequestUtils(){}


    private static final String BASIC_AUTH_HEADER = "Basic";

    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final String HEADER_SEPARATOR = ":";

    private static final String AUTH_HEADER = "Authorization";


    /**
     * This method search contentypes on headers
     * @param headers of request
     * @return  ContentType
     */
    public static ContentType getContentType(Headers headers){
        ContentType defaultContenType =
                ContentType.create(ContentType.TEXT_HTML.getMimeType(), Charset.forName("UTF-8"));

        if( headers == null){

            return defaultContenType;
        }
        if( headers.isEmpty()){
            return defaultContenType;
        }

        List<String> contentTypes = headers.get(CONTENT_TYPE_HEADER);
        if(contentTypes == null || contentTypes.size() == 0){
            return defaultContenType;
        }

        String contentTypeString = headers.getFirst(CONTENT_TYPE_HEADER);
        if( contentTypeString == null){
            return defaultContenType;
        }

        ContentType aux = ContentType.parse(contentTypeString);
        if( aux.getCharset() != null){
            return aux;
        }
        defaultContenType =
                ContentType.create(aux.getMimeType(), Charset.forName("UTF-8"));
        return defaultContenType;

    }


    /**
     * This method search on headers user/pass of basic authentication
     * @param headers of request
     * @return Authentication must be null if not exists
     */
    public static Authorization getAuthorizationFromHeader(Headers headers,Charset charset){

        Authorization authorization = null;
        if (headers == null) {
            return authorization;
        }

        List<String> headersList = headers.get(AUTH_HEADER);
        if (headersList == null) {
            return authorization;
        }

        for (String header : headersList) {
            if (header != null && header.startsWith(BASIC_AUTH_HEADER)) {
                // Authorization: Basic base64credentials
                String base64Credentials = header.substring(BASIC_AUTH_HEADER.length()).trim();
                String credentials;
                try{
                    credentials = new String(Base64.getDecoder().decode(base64Credentials), charset);
                }catch (IllegalArgumentException e){
                    continue;
                }

                // credentials = username:password
                final String[] userPassword = credentials.split(HEADER_SEPARATOR, 2);
                if (userPassword != null && userPassword.length != 2) {
                    continue;
                }
                authorization = new Authorization(userPassword[0],userPassword[1]);
            }
        }
        return authorization;

    }


    /**
     * @param
     * @return
     */
    public static Map<String, String>  parseQueryParamsUrlEnconded(final InputStream body,final Charset charset) {
        Map<String, String> queryParams = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(body))) {
            String urlencoded = reader.readLine();
            if(urlencoded != null && urlencoded.trim().length() > 0) {
                List<NameValuePair> list = URLEncodedUtils.parse(urlencoded, charset);
                list.forEach(pair -> {
                    queryParams.put(pair.getName(), pair.getValue());
                });
            }
        } catch(IOException e) {
            LOGGER.error("parseQueryParams", e);
        }
        return queryParams;
    }


    public static String parseFirstRequestBody(InputStream requestBody, Charset charset) throws IOException {

        if (requestBody == null) {
            return null;
        }

        InputStreamReader isr =  new InputStreamReader(requestBody,charset);
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


    public static boolean validMediaType(String rawBody, String method, ContentType contentType) {
        if (method.equals(BaseController.METHOD_GET) || method.equals(BaseController.METHOD_DELETE)) {
            return true;
        }
        if (rawBody == null || rawBody.length() == 0) {
            return true;
        }

        if (!isApplicationJson(contentType)) {
            return false;
        }
        return true;
    }

    public static boolean isApplicationJson(ContentType contentType) {
        return contentType.getMimeType().equals(ContentType.APPLICATION_JSON.getMimeType());
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


    public static String getSessionId(Headers headers)
    {
        List<String> cookies = headers.get(COOKIE);
        if(cookies != null) {
            for(String cookie : cookies) {
                StringTokenizer tokens = new StringTokenizer(cookie, ";");
                while(tokens.hasMoreTokens()) {
                    String[] pairNameValue = tokens.nextToken().split("=");
                    if( pairNameValue == null || pairNameValue.length != 2 ){
                        continue;
                    }
                    String name = pairNameValue[0].trim();
                    if( name.equals(Session.class.getSimpleName())){
                        return pairNameValue[1];
                    }
                }
            }
        }
        return null;
    }

}
