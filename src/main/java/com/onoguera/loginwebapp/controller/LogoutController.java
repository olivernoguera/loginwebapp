package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseInternalServerError;
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
public final class LogoutController  extends HtmlController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);

    private static final String PATH = "/logout";
    private static final Pattern p = Pattern.compile(PATH + "\\S*");

    @Override
    public Pattern getURLPattern() {
        return p;
    }

    @Override
    public List<String> getPathParams() {
        return new ArrayList<>();
    }

    @Override
    public Response doPost(Request request) {
        Map<String, String> values = new HashMap<>();
        Response response = null;
        Session session = request.getSession();
        //TODO check expired session
        if( session != null ) {
            sessionService.delete(session.getId());
        }
        try {
            response = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, values,"login");
        } catch (IOException e) {
            response  = new ResponseInternalServerError();
        }

        return response;

    }


}
