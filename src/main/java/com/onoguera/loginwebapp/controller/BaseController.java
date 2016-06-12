package com.onoguera.loginwebapp.controller;


import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
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
import org.apache.http.entity.ContentType;
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

        ContentType contentType;

        try {
            contentType = RequestUtils.getContentType(headers);
        }  catch (IllegalArgumentException i) {
            LOGGER.warn("Bad request.", i);
            return new ResponseBadRequest();
        }



        Request request;
        try {
            request = this.getRequest(requestURI.getQuery(), requestURI.getPath(), requestBody, headers,contentType);
        } catch (IOException io) {
            LOGGER.warn("Bad request.", io);
            return new ResponseBadRequest();
        }



        if (this instanceof AuthController) {
            List<String> authorizations = headers.get("Authorization");
            List<Role> roles = this.getRoles(authorizations,contentType.getCharset());
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

    private Request getRequest(final String query, final String path,
                               InputStream requestBody, Headers headers,
                               ContentType contentType) throws IOException {


        Map<String, String> queryParams = RequestUtils.parseQueryParams(query, contentType.getCharset());
        Map<String, String> pathParams =
                RequestUtils.parsePathParams(path, this.getPathParams(), this.getURLPattern());
        String rawBody = RequestUtils.parseFirstRequestBody(requestBody, contentType.getCharset());
        if (isApplicationJson(headers)) {
            return new JsonRequest(queryParams, pathParams, rawBody);
        }
        return new Request(queryParams, pathParams, rawBody);
    }






    /**
     * @param headers
     * @return List of roles with authentication
     */
    private List<Role> getRoles(List<String> headers,Charset currentCharset) {
        List<Role> roles = new ArrayList<>();
        String[] authorizations  = RequestUtils.getAuthorizationFromHeader(headers, currentCharset);
        User user = new User(authorizations[0], authorizations[1]);
        roles.addAll(userService.getRoles(user));
        return roles;
    }

}
