package com.onoguera.loginwebapp.integration;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.PageResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseInternalServerError;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
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
public final class PageController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);
    private static final String PAGE_ID = "pageId";
    private static final String PATH = "/page_";
    private static final String ROLE_PREFIX = "PAGE_";
    private static final Pattern p = Pattern.compile(PATH + "/*(?<" + PAGE_ID + ">\\S*)");

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
        //TODO check expired session
        if( session != null ) {
            response = this.getResponseFromUser(session.getUser(),session.getId(),pageId.toLowerCase());

            //has session must send to other page not login page
            //response = new ResponseForbidden();
        }else{
            try {
                response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, new HashMap<>(),"login");
            } catch (IOException e) {
                response  = new ResponseInternalServerError();
            }
        }

        return response;
    }

    @Override
    public Response doPost(Request request) {
        return new ResponseNotImplemented();
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
                //TODO multiple role
                values.put("page", currentPage);
                values.put("user", user.getId());

                response = new PageResponse(HttpURLConnection.HTTP_OK, values ,sessionID,currentPage) {};
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
