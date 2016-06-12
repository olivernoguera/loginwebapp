package com.onoguera.loginwebapp.controller;

import com.sun.net.httpserver.Headers;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains some functions to read and parse elements of request.
 *
 * Created by oliver on 7/06/16.
 */
public final  class RequestUtils {

    private static final String EMPTY_STRING = "";

    /**
     * Protect instance final for final utility class
     */
    private RequestUtils(){}


    private static final String BASIC_AUTH_HEADER = "Basic";

    private static final String CONTENT_TYPE_HEADER = "Content-Type:";

    private static final String CREDENTIALS_SEPARATOR = ":";

    private static final String CHARSET_HEADER = "charset:";


    private static final String CONTENT_TYPE_SEPARATOR = ";";

    /**
     * This method search on headers user/pass of basic authentication
     * @param headers of request
     * @return List of par user/password decrypt
     */


    public static ContentType getContentType(Headers headers){

        ContentType contentType =
                ContentType.create(ContentType.TEXT_HTML.getMimeType(), Charset.defaultCharset().defaultCharset());

        if( headers == null){

            return contentType;
        }

        String contentTypeString = headers.getFirst(CONTENT_TYPE_HEADER);
        return ContentType.parse(contentTypeString);

    }

    /**
     * This method search on headers user/pass of basic authentication
     * @param headers of request
     * @return List of par user/password decrypt
     */
    public static String[] getAuthorizationFromHeader(List<String> headers,Charset charset){

        String[] authorization = null;

        if (headers == null) {
            return authorization;
        }

        for (String header : headers) {
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
                final String[] userPassword = credentials.split(CREDENTIALS_SEPARATOR, 2);
                if (userPassword != null && userPassword.length != 2) {
                    continue;
                }
                authorization = userPassword;

            }
        }
        return authorization;

    }


    /**
     * @param query uri
     * @return
     */
    public static Map<String, String>  parseQueryParams(final String query,final Charset charset) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null) {
            List<NameValuePair> paramsPair = URLEncodedUtils.parse(query, charset);
            paramsPair.forEach(pair -> {
                queryParams.put(pair.getName(), pair.getValue());
            });
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

}
