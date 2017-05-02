package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseUnauthorized;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.LoginResponse;
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
public class LoginControllerTest {


    @Test
    public void doPostBadCasesTest() throws IOException {

        LoginController  loginController = new LoginController (new SessionServiceWithoutSession(),
                new MockUserServices.UserServiceMockValidateKO());
        Map<String, String> queryParams = null;
        Map<String, String> pathParams = null;
        Response expectedResponse = new ResponseBadRequest();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostTest without query params null", response, is(expectedResponse));

        queryParams = new HashMap<>();
        expectedResponse = new ResponseBadRequest();
        request = new Request(queryParams,pathParams,null,null);
        response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostTest without query params empty", response, is(expectedResponse));


        queryParams = new HashMap<>();
        expectedResponse = new ResponseBadRequest();
        request = new Request(queryParams,pathParams,null,null);
        response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostTest without username null", response, is(expectedResponse));


        queryParams = new HashMap<>();
        queryParams.put("username","test");
        expectedResponse = new ResponseBadRequest();
        request = new Request(queryParams,pathParams,null,null);
        response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostTest without password null", response, is(expectedResponse));

        queryParams = new HashMap<>();
        queryParams.put("username","test");
        queryParams.put("password","test");
        expectedResponse = new ResponseUnauthorized();
        request = new Request(queryParams,pathParams,null,null);
        response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostTest user unathorized", response, is(expectedResponse));

    }


    @Test
    public void doPostGoodCasesTest() throws IOException {

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(SessionServiceWithSession.COOKIE, Arrays.asList("Session="+expectedSessionId));
        User userSession = new User("1","2");
        Session sesion = new Session(userSession, expectedSessionId);
        LoginController  loginController = new LoginController (new SessionServiceWithSession(sesion),
                new MockUserServices.UserServiceMockValidateOK());

        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        queryParams.put("username","test");
        queryParams.put("password","test");
        Response expectedResponse = new LoginResponse(HttpURLConnection.HTTP_OK, pathParams,"login");
        Request request = new Request(queryParams,pathParams,null,sesion);
        Response response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostGoodCasesTest without roles", response, is(expectedResponse));


        UserService userService =  new MockUserServices.UserServiceMockValidateOKWithRoles();
        loginController = new LoginController (new SessionServiceWithSession(sesion), userService);
        queryParams.put("username", new SessionServiceWithSession(sesion).getSession("1").getUser().getId());
        queryParams.put("password", new SessionServiceWithSession(sesion).getSession("1").getUser().getPassword());
        expectedResponse = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP,
                queryParams,sesion.getId() ,"PAGE1".toLowerCase()) {};
        request = new Request(queryParams,pathParams,null,sesion);
        response = loginController.doPost(request);
        Assert.assertThat("LoginControllerTest doPostGoodCasesTest with roles status", response.getHttpStatus(),
                is(expectedResponse.getHttpStatus()));
        Assert.assertThat("LoginControllerTest doPostGoodCasesTest with roles output", response.getOutput(),
                is(expectedResponse.getOutput()));
        Assert.assertThat("LoginControllerTest doPostGoodCasesTest with roles contentType", response.getContentType(),
                is(expectedResponse.getContentType()));


    }

    @Test
    public void doGetTest() throws IOException {

        Headers headers = new Headers();

        String expectedSessionId = "1";
        headers.put(SessionServiceWithSession.COOKIE, Arrays.asList("Session="+expectedSessionId));
        User userSession = new User("1","2", Arrays.asList(new Role("Role1")));
        Session sesion = new Session(userSession, expectedSessionId);

        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> pathParams = new HashMap<>();
        UserService userService =  new MockUserServices.UserServiceMockValidateOKWithRoles();
        LoginController loginController = new LoginController (new SessionServiceWithSession(sesion), userService);
        Session session = new SessionServiceWithSession(sesion).getSession(expectedSessionId);

        queryParams.put("username", session.getUser().getId());
        queryParams.put("password", session.getUser().getPassword());
        Response expectedResponse = new LoginResponse(HttpURLConnection.HTTP_MOVED_TEMP,
                queryParams,session.getId() ,"PAGE1".toLowerCase()) {};
        Request request = new Request(queryParams,pathParams,null,session);
        Response response = loginController.doGet(request);
        Assert.assertThat("LoginControllerTest doGetTest with roles and session status", response.getHttpStatus(),
                is(expectedResponse.getHttpStatus()));
        Assert.assertThat("LoginControllerTest doGetTest with roles and session output", response.getOutput(),
                is(expectedResponse.getOutput()));
        Assert.assertThat("LoginControllerTest doGetTest with roles and session contentType", response.getContentType(),
                is(expectedResponse.getContentType()));


        queryParams.put("username", session.getUser().getId());
        queryParams.put("password", session.getUser().getPassword());
        expectedResponse = new LoginResponse(HttpURLConnection.HTTP_OK, pathParams,"login");
        request = new Request(queryParams,pathParams,null,null);
        response = loginController.doGet(request);
        Assert.assertThat("LoginControllerTest doGetTest without session response", response.getHttpStatus(),
                is(expectedResponse.getHttpStatus()));


    }


}
