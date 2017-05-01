package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseInternalServerError;
import com.onoguera.loginwebapp.response.ResponseUnauthorized;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.onoguera.loginwebapp.service.UserServiceInterface;
import com.onoguera.loginwebapp.view.LoginResponse;
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
public final class LoginController extends HtmlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    private static final String PATH = "/login";
    private static final Pattern p = Pattern.compile(PATH + "\\S*");
    private UserServiceInterface userService;

    public LoginController(SessionServiceInterface sessionService,UserServiceInterface userService) {
        super(sessionService);
        this.userService  = userService;
    }

    public UserServiceInterface getUserService() {
        return userService;
    }

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
        if( session != null ) {
            response = this.getResponseFromUser(session.getUser(),session.getId());
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
        Response response = null;
        Map<String, String> queryParams = request.getQueryParams();

        if( queryParams == null || queryParams.isEmpty()){
            return new ResponseBadRequest();
        }
        String username = queryParams.get("username");
        String password = queryParams.get("password");
        if( username == null || password == null){
            return new ResponseBadRequest();
        }
        User user = new User(username, password);
        User validateUser = userService.validateUser(user);

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
        if( roles == null || roles.isEmpty()){
            try {
                response = new LoginResponse(HttpURLConnection.HTTP_OK, values,"login");
            } catch (IOException e) {
               response = new ResponseInternalServerError();
            }
        }
        else{
            try {
                String rolePage = roles.stream().findFirst().get().getId();
                values.put("page", rolePage);
                values.put("user", user.getId());
                response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP,
                        values,sessionID ,rolePage.toLowerCase()) {};
            } catch (IOException io) {
                response = new ResponseInternalServerError();
            }
        }
        return response;

    }


}
