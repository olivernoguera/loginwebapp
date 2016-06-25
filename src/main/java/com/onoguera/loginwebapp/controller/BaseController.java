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
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;


/**
 * Created by oliver on 1/06/16.
 *
 */
public abstract class BaseController implements Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    private UserService userService = UserService.getInstance();

    public final static String METHOD_POST = "POST";
    public final static String METHOD_GET = "GET";
    public final static String METHOD_PUT = "PUT";
    public final static String METHOD_DELETE = "DELETE";


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
            request = this.getRequest(requestURI.getQuery(), requestURI.getPath(), requestBody,contentType);
        } catch (IOException io) {
            LOGGER.warn("Bad request.", io);
            return new ResponseBadRequest();
        }


        if (this instanceof AuthController) {

            List<Role> roles = this.getRoles(headers,contentType.getCharset());
            if (roles.isEmpty()) {
                return new ResponseUnauthorized();
            }
            if (!RequestUtils.validMediaType(request.getRawBody(), method, contentType)) {
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



    private Request getRequest(final String query, final String path,
                               InputStream requestBody,
                               ContentType contentType) throws IOException {


        Map<String, String> queryParams = RequestUtils.parseQueryParams(query, contentType.getCharset());
        Map<String, String> pathParams =
                RequestUtils.parsePathParams(path, this.getPathParams(), this.getURLPattern());
        String rawBody = RequestUtils.parseFirstRequestBody(requestBody, contentType.getCharset());
        if (RequestUtils.isApplicationJson(contentType)) {
            return new JsonRequest(queryParams, pathParams, rawBody);
        }
        return new Request(queryParams, pathParams, rawBody);
    }

    /**
     * @param headers
     * @return List of roles with authentication
     */
    private List<Role> getRoles(Headers headers,Charset currentCharset) {
        List<Role> roles = new ArrayList<>();
        Authorization authorization = RequestUtils.getAuthorizationFromHeader(headers, currentCharset);
        if( authorization != null){
            User user = new User(authorization.getUsername(),authorization.getPassword());
            roles.addAll(userService.getRoles(user));
        }

        return roles;
    }

}
