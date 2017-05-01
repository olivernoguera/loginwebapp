package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 01/05/2017.
 */
public class LogoutControllerTest {

    private static final String COOKIE = "Cookie";

    @Test
    public void doPostTest() throws IOException {

        LogoutController  logoutController = new LogoutController (new SessionServiceWithoutSession());
        Map<String, String> queryParams = null;
        Map<String, String> pathParams = null;
        Response expectedResponse = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, queryParams,"login");
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = logoutController.doPost(request);
        Assert.assertThat("LogoutControllerTest doPostTest without session valid response",
                response, is(expectedResponse));

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));
        User userSession = new User("1","2");
        Session session = new Session(userSession,expectedSessionId);
        logoutController = new LogoutController (new SessionServiceWithSession(userSession));
        expectedResponse = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, queryParams,"login");
        request = new Request(queryParams,pathParams,null, session);
        response = logoutController.doPost(request);
        Assert.assertThat("LogoutControllerTest doPostTest without session valid response",
                response, is(expectedResponse));

    }

}
