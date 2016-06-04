package com.onoguera.loginwebapp.controller;

import com.google.gson.Gson;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.sun.net.httpserver.Headers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class BaseControllerTest {

    private static final String CORRECT_PARAM = "userId";
    private static final Gson GSON = new Gson();
    private static final String ADMIN_AUTH = "Basic QURNSU46QURNSU4=";
    private static Headers adminHeaders;
    private static final User adminUser = new User("ADMIN","ADMIN");

    @Before
    public void before(){
        adminHeaders = new Headers();
        adminHeaders.put("Authorization", Arrays.asList(ADMIN_AUTH));
        UserService userService = UserService.getInstance();
        userService.addUser(adminUser);
    }

    @After
    public void after(){
        UserService userService = UserService.getInstance();
        userService.removeUser(adminUser.getId());
    }


    @Test
    public void doGetUnathorizedDispatch() throws URISyntaxException {

        Controller controller = new UserController();
        UserService userService = UserService.getInstance();

        User user = new User("test","test");
        userService.addUser(user);
        userService.removeUser(adminUser.getId());

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        URI uri = new URI("/users/test");

        Headers headers = new Headers();
        headers.put("Authorization", Arrays.asList(ADMIN_AUTH));
        Response response = controller.dispatch(uri,null,"GET", headers);

        UserVO expectedUser = new UserVO(user.getId());

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(ResponseUnauthorized.class));
        //Restore inital test state
        userService.removeUser("test");
    }


    @Test
    public void doGetWithResourceDispatch() throws URISyntaxException {

        Controller controller = new UserController();
        UserService userService = UserService.getInstance();

        User user = new User("test","test");
        userService.addUser(user);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        URI uri = new URI("/users/test");

        Headers headers = new Headers();
        headers.put("Authorization", Arrays.asList(ADMIN_AUTH));
        Response response = controller.dispatch(uri,null,"GET", headers);

        UserVO expectedUser = new UserVO(user.getId());

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty" , response.getOutput(), is(GSON.toJson(expectedUser)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser("test");
    }


    @Test
    public void doPostDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"POST", adminHeaders);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doPutDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"PUT", adminHeaders);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDeleteDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"DELETE", adminHeaders);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));

    }

    @Test
    public void doPatchDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"PATH", adminHeaders);
        Assert.assertThat(" Response must be mehod not allowed", response, instanceOf(ResponseMethodNotAllowed.class));

    }

}