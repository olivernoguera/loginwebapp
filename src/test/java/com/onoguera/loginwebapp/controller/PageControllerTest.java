package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.PageResponse;
import com.sun.net.httpserver.Headers;
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
public class PageControllerTest {


    private static final String PAGE_ID = "pageId";

    @Test
    public void doGetTest() throws IOException {

        Headers headers = new Headers();

        String expectedSessionId = "1";
        headers.put(SessionServiceWithSession.COOKIE, Arrays.asList("Session="+expectedSessionId));
        User userSession = new User("1","2", Arrays.asList(new Role("PAGE_1")));
        Session sesion = new Session(userSession, expectedSessionId);

        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID,"1");
        PageController pageController = new PageController (new SessionServiceWithSession(sesion));
        Session session = new SessionServiceWithSession(sesion).getSession(expectedSessionId);

        queryParams.put("username", session.getUser().getId());
        queryParams.put("password", session.getUser().getPassword());
        Map<String,String> expectedValues = new HashMap<>();
        expectedValues.put("page", "PAGE_1".toLowerCase());
        expectedValues.put("user", "1");
        Response expectedResponse = new PageResponse(HttpURLConnection.HTTP_OK, expectedValues ,session.getId(),"page_1") {};
        Request request = new Request(queryParams,pathParams,null,session);
        Response response = pageController.doGet(request);
        Assert.assertThat("PageControllerTest doGetTest with roles and session response status", response.getHttpStatus(),
                is(expectedResponse.getHttpStatus()));
        Assert.assertThat("PageControllerTest doGetTest with roles and session response contentType", response.getContentType(),
                is(expectedResponse.getContentType()));

        Assert.assertThat("PageControllerTest doGetTest with roles and session response output", response.getOutput().trim(),
                is(expectedResponse.getOutput().trim()));

        queryParams.put("username", session.getUser().getId());
        queryParams.put("password", session.getUser().getPassword());
        expectedResponse =  new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP,
                queryParams,session.getId() ,"PAGE_1".toLowerCase()) {};
        request = new Request(queryParams,pathParams,null,null);
        response = pageController.doGet(request);

        Assert.assertThat("PageControllerTest doGetTest with roles and session status", response.getHttpStatus(),
                is(expectedResponse.getHttpStatus()));
        Assert.assertThat("PageControllerTest doGetTest with roles and session output", response.getOutput(),
                is(expectedResponse.getOutput()));
        Assert.assertThat("PageControllerTest doGetTest with roles and session contentType", response.getContentType(),
                is(expectedResponse.getContentType()));

    }

    @Test
    public void doGetTestWithoutSession() throws IOException {

        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID,"1");
        PageController pageController = new PageController (new SessionServiceWithoutSession());
        Response expectedResponse = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP, new HashMap<>(),"login");
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = pageController.doGet(request);
        Assert.assertThat("PageControllerTest doGetTestWithoutSession response", response, is(expectedResponse));

    }

}
