package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseForbidden;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.onoguera.loginwebapp.view.ResponseUnsupportedMediaType;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by olivernoguera on 05/06/2016.
 *  Abstract class for api controllers required authentication
 */
public abstract class RestAuthController extends BaseController{

    protected UserService userService = UserService.getInstance();

    public  Response getBadHeaders(String method, Headers headers, ContentType contentType, Request request) {
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

}
