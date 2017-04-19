package integration.integration;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onoguera.loginwebapp.controller.Controller;
import com.onoguera.loginwebapp.controller.UserControllerRest;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserConverter;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import com.sun.net.httpserver.Headers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class BaseControllerTest {

    private static final String CORRECT_PARAM = "userId";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String ADMIN_AUTH = "Basic QURNSU46QURNSU4=";
    private static Headers adminHeaders;
    private static final User adminUser = new User("ADMIN", "ADMIN");
    private static final Role adminRole = new Role("ADMIN", true);
    private UserConverter userConverter = new UserConverter();

    @Before
    public void before() {
        adminHeaders = new Headers();
        adminHeaders.put("Authorization", Arrays.asList(ADMIN_AUTH));
        adminUser.addRole(adminRole);
        UserService userService = UserService.getInstance();
        userService.addUser(adminUser);
    }

    @After
    public void after() {
        UserService userService = UserService.getInstance();
        userService.removeUser(adminUser.getId());
    }


    @Test
    public void doGetUnathorizedDispatch() throws URISyntaxException, JsonProcessingException {

        Controller controller = new UserControllerRest();
        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);
        userService.removeUser(adminUser.getId());

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        URI uri = new URI("/users/test");

        Headers headers = new Headers();
        headers.put("Authorization", Arrays.asList(ADMIN_AUTH));
        Response response = controller.dispatch(uri, null, "GET", headers);
        Assert.assertThat(" Response must be ResponseUnauthorized", response, instanceOf(ResponseUnauthorized.class));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_UNAUTHORIZED, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser(user.getId());
    }


    @Test
    public void doGetWithResourceDispatch() throws URISyntaxException, JsonProcessingException {

        Controller controller = new UserControllerRest();
        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        URI uri = new URI("/users/test");

        Headers headers = new Headers();
        headers.put("Authorization", Arrays.asList(ADMIN_AUTH));
        Response response = controller.dispatch(uri, null, "GET", headers);

        ReadUser expectedUser = userConverter.entityToReadDTO(user);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(MAPPER.writeValueAsString(expectedUser)));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser(user.getId());
    }


    @Test
    public void doPostDispatch() throws URISyntaxException, IOException {
        Controller controller = new UserControllerRest();
        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);


        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));

        List<WriteUser> users = new ArrayList<>();
        users.add(userConverter.entityToWriteDTO(userExpected));
        File tmpUsers = new File("users.json");
        MAPPER.writeValue(tmpUsers,users);
        InputStream body = new FileInputStream(tmpUsers);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        URI uri = new URI("/users");

        Headers headers = new Headers();
        headers.put("Authorization", Arrays.asList(ADMIN_AUTH));
        headers.put("Content-Type", Arrays.asList("application/json; charset=utf-8"));
        Response response = controller.dispatch(uri, body, "POST", headers);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be users", response.getOutput(), is(MAPPER.writeValueAsString(users)));
        Assert.assertThat("Service  must be only one user", userService.getUsers().size(), is(1));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_CREATED, HttpURLConnection.HTTP_CREATED, is(HttpURLConnection.HTTP_CREATED));

        //Restore inital test state
        userService.removeUser(userExpected.getId());
        Assert.assertThat("Service  must be 0 users", userService.getUsers().size(), is(0));
        Assert.assertThat("Service  must be 0 roles", RoleService.getInstance().getRoles().size(), is(0));

    }





    @Test
    public void doPutDispatch() throws URISyntaxException {
        Controller controller = new UserControllerRest();
        URI uri = new URI("/users");
        Response response = controller.dispatch(uri, null, "PUT", adminHeaders);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDeleteDispatch() throws URISyntaxException {
        Controller controller = new UserControllerRest();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri, null, "DELETE", adminHeaders);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseNotFound.class));

    }

    @Test
    public void doPatchDispatch() throws URISyntaxException {
        Controller controller = new UserControllerRest();
        URI uri = new URI("/users/test");
        Response response = controller.dispatch(uri, null, "PATCH", adminHeaders);
        Assert.assertThat(" Response must be mehod not allowed", response, instanceOf(ResponseMethodNotAllowed.class));

    }

}
