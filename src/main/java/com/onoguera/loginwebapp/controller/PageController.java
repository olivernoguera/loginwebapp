package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseInternalServerError;
import com.onoguera.loginwebapp.service.SessionService;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by olivernoguera on 25/06/2016.
 *
 */
public final class PageController extends HtmlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);
    private static final String PAGE_ID = "pageId";
    private static final String PATH = "/page_";
    private static final String ROLE_PREFIX = "PAGE_";
    private static final Pattern p = Pattern.compile(PATH + "/*(?<" + PAGE_ID + ">\\S*)");

    public PageController(SessionService sessionService) {
        super(sessionService);

    }

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return Arrays.asList(PAGE_ID);
    }

    @Override
    public Response doGet(Request request) {

        Response response = null;
        Map<String, String>  pathParams = request.getPathParams();
        String pageId = ROLE_PREFIX  + pathParams.get(PAGE_ID);
        Session session = request.getSession();

        if( session != null ) {
            response = this.getResponseFromUser(session.getUser(),session.getId(),pageId.toLowerCase());
        }else{
            try {
                response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, new HashMap<>(),"login");
            } catch (IOException e) {
                response  = new ResponseInternalServerError();
            }
        }

        return response;
    }

    private Response getResponseFromUser(User user,String sessionID, String currentPage){

        Response response = null;
        Map<String, String> values = new HashMap<>();
        List<Role> roles = user.getRoles();

        if( roles.isEmpty() ||
                !roles.stream().filter(r->r.getId().equals(currentPage.toUpperCase())).findFirst().isPresent()){
            try {
                response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, new HashMap<>(),sessionID, "login");
            } catch (IOException e) {
                response  = new ResponseInternalServerError();
            }
        }
        else{
            try {
                values.put("page", currentPage);
                values.put("user", user.getId());
                response = new PageResponse(HttpURLConnection.HTTP_OK, values ,sessionID,currentPage) {};
            } catch (IOException io) {
                response = new ResponseInternalServerError();
            }
        }
        return response;

    }

}
