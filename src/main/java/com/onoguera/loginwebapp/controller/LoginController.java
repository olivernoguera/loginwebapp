package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.service.SessionService;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseForbidden;
import com.onoguera.loginwebapp.view.ResponseInternalServerError;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by olivernoguera on 25/06/2016.
 *
 */
public final class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private static final String PATH = "/login";
    private static final Pattern p = Pattern.compile(PATH + "\\S*");
    private UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return new ArrayList<>();
    }

    @Override
    public Response doGet(Request request) {
        Map<String, String> values = new HashMap<>();
        Response response = null;



        Session session = request.getSession();
        //TODO check expired session
        if( session != null ) {
            response = this.getResponseFromUser(session.getUser(),session.getId());

            //has session must send to other page not login page
            //response = new ResponseForbidden();
        }else{
            values.put("title", "Login user");
            values.put("message", "Put user and password");
            try {
                response = new LoginResponse(HttpURLConnection.HTTP_OK, values,"login");
            }catch (IOException io){
                response = new ResponseInternalServerError();
            }
        }


        return response;
    }

    @Override
    public Response doPost(Request request) {

        Map<String, String> queryParams = request.getQueryParams();
        String username = queryParams.get("username");
        String password = queryParams.get("password");
        User user = new User(username, password);
        User validateUser = userService.validateUser(user);
        Response response = null;


        //TODO check correct role
        if( validateUser == null){
            response = new ResponseUnauthorized();
        }
        else {
            Session session = sessionService.createSession(validateUser);
            response = this.getResponseFromUser(validateUser,session.getId());
        }
        return response;

    }


    private Response getResponseFromUser(User user,String sessionID){
        Response response = null;
        Map<String, String> values = new HashMap<>();
        List<Role> roles = user.getRoles();
        if( roles.isEmpty()){
            response = new ResponseForbidden();
        }
        else{
            try {
                //TODO multiple role
                String rolePage = roles.stream().findFirst().get().getId();
                values.put("page", rolePage);
                values.put("user", user.getId());

                response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, values,sessionID ,rolePage.toLowerCase()) {};
            } catch (IOException io) {
                response = new ResponseInternalServerError();
            }
        }
        return response;

    }

    @Override
    public Response doPut(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doDelete(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response checkAuthAndRestAPI(String method, Headers headers, ContentType contentType, Request request) {
        return null;
    }


}
