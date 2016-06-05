package com.onoguera.loginwebapp.controller;

import com.google.gson.Gson;
import com.onoguera.loginwebapp.model.Role;
import com.onoguera.loginwebapp.model.User;
import com.onoguera.loginwebapp.model.UserVO;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class UserControllerTest {

    private final static String BASE_PATH = "/users";
    private final static String BASE_PATH_WITH_END_SLASH = "/users/";
    private final static String BASE_PATH_WITH_PATH_PARAM_NUMERIC = "/users/515151";
    private final static String BASE_PATH_WITH_PATH_PARAM_STRING = "/users/test";

    private final static String BAD_PATH = "users";
    private final static String BAD_PATH_2 = "badpath";

    private static final String CORRECT_PARAM = "userId";
    private static final Gson GSON = new Gson();

    private UserController userController = new UserController();

    @Test
      public void correctFiltersTest(){

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH,
                userController.filter(BASE_PATH),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_END_SLASH,
                userController.filter(BASE_PATH_WITH_END_SLASH),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_PATH_PARAM_NUMERIC,
                userController.filter(BASE_PATH_WITH_PATH_PARAM_NUMERIC),
                is(Boolean.TRUE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BASE_PATH_WITH_PATH_PARAM_STRING,
                userController.filter(BASE_PATH_WITH_PATH_PARAM_STRING),
                is(Boolean.TRUE));
    }

    @Test
    public void badFiltersTest(){

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BAD_PATH,
                userController.filter(BAD_PATH),
                is(Boolean.FALSE));

        Assert.assertThat(getClass().getSimpleName() + " correctFiltersTest::Path::" +
                        BAD_PATH_2,
                userController.filter(BAD_PATH_2),
                is(Boolean.FALSE));
    }


    @Test
    public void doGetBadPathParamResource() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badparam","badparam");

        Request request = new Request(null, pathParams, null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be bad request", response, instanceOf(ResponseBadRequest.class));

    }

    @Test
    public void doGetEmptyResource(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be not found", response, instanceOf(ResponseNotFound.class));
    }

    @Test
    public void doGeWithResource(){

        UserService userService = UserService.getInstance();
        User user = new User("test","test");
        userService.addUser(user,new Role("test"));

        UserVO userVO = new UserVO(user.getId(),user.getRoles());
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null);
        Response response = userController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse" , response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty" , response.getOutput(), is(GSON.toJson(userVO)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser("test");

    }


    @Test
    public void doGetEmptyCollection(){

        Request request = new Request(null, null, null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be jsonResponse" , response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is("[]"));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
    }

    @Test
    public void doGeWithCollectionResource(){

        UserService userService = UserService.getInstance();

        User user = new User("test","test");
        userService.addUser(user,new Role("test"));
        User user2 = new User("test2","test2");
        userService.addUser(user2,new Role("test2"));

        List<UserVO> collectionUser = new ArrayList<>();
        collectionUser.add(new UserVO(user2.getId(),user2.getRoles()));
        collectionUser.add(new UserVO(user.getId(),user.getRoles()));

        Request request = new Request(null, null, null);
        Response response = userController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse" , response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty" , response.getOutput(), is(GSON.toJson(collectionUser)));
        Assert.assertThat(" Response status must be "+ HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser("test");
        userService.removeUser("test2");
    }

    @Test
    public void doPost(){
        Request request = new Request(null, null, null);
        Response response = userController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented" , response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
     public void doPut(){
        Request request = new Request(null, null, null);
        Response response = userController.doPut(request);
        Assert.assertThat(" Response must be ResponseNotImplemented" , response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDelete(){
        Request request = new Request(null, null, null);
        Response response = userController.doPut(request);
        Assert.assertThat(" Response must be ResponseNotImplemented" , response, instanceOf(ResponseNotImplemented.class));
    }
}
