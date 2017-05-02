package com.onoguera.loginwebapp.restcontroller;

import com.onoguera.loginwebapp.controller.Authorization;
import com.onoguera.loginwebapp.controller.BaseController;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseForbidden;
import com.onoguera.loginwebapp.response.ResponseUnauthorized;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.PageApiRoleService;
import com.onoguera.loginwebapp.service.UserService;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by olivernoguera on 05/06/2016.
 *  Abstract class for api controllers required authentication
 */
public abstract class RestAuthController extends BaseController {

    protected final UserService userService;


    public RestAuthController(UserService userService) {
        this.userService = userService;
    }

    public  Response getBadHeaders(String method, Headers headers, ContentType contentType, Request request) {

        List<Role> roles = this.getRoles(headers,contentType.getCharset());

        if (roles.isEmpty()  && !roles.contains(PageApiRoleService.API_ROLE)) {
            return new ResponseUnauthorized();
        }
        if (!RequestUtils.validMediaType( method, contentType)) {
            return new ResponseUnsupportedMediaType();
        }
        if (!method.equals(METHOD_GET)) {
            //Modify and create and delete
            if (!roles.contains(PageApiRoleService.WRITER_API_ROLE)) {
                return new ResponseForbidden();
            }
        }
        return null;
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
            User userStore = userService.validateUser(user);
            if( userStore != null){
                roles.addAll(userStore.getRoles());
            }
        }

        return roles;
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


    protected Request getRequest(Map<String, String> pathParams, final String path,
                               InputStream requestBody,
                               ContentType contentType,
                               Headers headers) throws IOException {
        if(Objects.isNull(pathParams) ||  Objects.isNull(requestBody) || Objects.isNull(contentType) ||
                Objects.isNull(headers)){
            throw new IllegalArgumentException(
                    MessageFormat.format("Elements getRequest {0}, requestBody {1}, contentType {2} , " +
                                    "headers {3} must not be null ",
                            pathParams,requestBody, contentType, headers));
        }
        Map<String, String> queryParams = new HashMap<>();
        String rawBody = RequestUtils.parseFirstRequestBody(requestBody, contentType.getCharset());

        return new JsonRequest(queryParams, pathParams, rawBody);

    }

    public UserService getUserService() {
        return userService;
    }
}
