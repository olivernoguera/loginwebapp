package com.onoguera.loginwebapp.controller;

import com.google.gson.Gson;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
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


    @Test
    public void doGetWithResourceDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        UserService userService = UserService.getInstance();
        User user = new User("test","test");
        UserVO userVO = new UserVO(user.getId());
        userService.addUser(user);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");


        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"GET");

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty" , response.getOutput(), is(GSON.toJson(userVO)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser("test");
    }


    @Test
    public void doPostDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"POST");
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doPutDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"PUT");
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDeleteDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"DELETE");
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));

    }

    @Test
    public void doPatchDispatch() throws URISyntaxException {
        Controller controller = new UserController();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri,null,"PATH");
        Assert.assertThat(" Response must be mehod not allowed", response, instanceOf(ResponseMethodNotAllowed.class));

    }

}
