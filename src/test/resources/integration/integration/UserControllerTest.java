package integration.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onoguera.loginwebapp.controller.JsonRequest;
import com.onoguera.loginwebapp.controller.Request;
import com.onoguera.loginwebapp.controller.UserControllerRest;
import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserConverter;
import com.onoguera.loginwebapp.service.UserService;
import com.onoguera.loginwebapp.view.JsonResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseEmpty;
import com.onoguera.loginwebapp.view.ResponseNotFound;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.ResponseUnsupportedMediaType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 04/06/2016.
 */
public class UserControllerTest {

    private final static String BASE_PATH = "/users";
    private final static String BASE_PATH_WITH_END_SLASH = "/users/";
    private final static String BASE_PATH_WITH_PATH_PARAM_NUMERIC = "/users/515151";
    private final static String BASE_PATH_WITH_PATH_PARAM_STRING = "/users/test";

    private static final String USER_ID = "userId";

    private static final String ROLE_ID = "roleId";

    private static final String PATH_ROLES = "roles";

    private final static String BAD_PATH = "users";
    private final static String BAD_PATH_2 = "badpath";

    private static final String CORRECT_PARAM = "userId";
    private UserConverter userConverter = new UserConverter();
    private static final ObjectMapper MAPPER = new ObjectMapper();


    private UserControllerRest userController = new UserControllerRest();

    @Test
    public void correctFiltersTest() {

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

    @After
    public void after() {
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void badFiltersTest() {

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
        pathParams.put("badparam", "badparam");

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be bad request", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));

    }

    @Test
    public void doGetEmptyResource() {

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be not found", response, instanceOf(ResponseNotFound.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doGeWithResource() throws JsonProcessingException {

        UserService userService = UserService.getInstance();
        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);

        ReadUser expectedUser = userConverter.entityToReadDTO(user);
        String output = MAPPER.writeValueAsString(expectedUser);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(CORRECT_PARAM, "test");

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(output));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser(user.getId());
        Assert.assertThat(" Must be Empty 0 roles", RoleService.getInstance().getRoles().size(), is(0));

    }


    @Test
    public void doGetEmptyCollection() {

        Request request = new Request(null, null, null,null);
        Response response = userController.doGet(request);
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is("[]"));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doGeWithCollectionResource() throws JsonProcessingException {

        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);

        User user2 = new User("test2", "test2");
        user2.addRole(new Role("test2"));
        userService.addUser(user2);

        List<ReadUser> expectedUsers = new ArrayList<>();
        expectedUsers.add(userConverter.entityToReadDTO(user2));
        expectedUsers.add(userConverter.entityToReadDTO(user));

        String output = MAPPER.writeValueAsString(expectedUsers);

        Request request = new Request(null, null, null,null);
        Response response = userController.doGet(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be empty", response.getOutput(), is(output));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_OK, HttpURLConnection.HTTP_OK, is(HttpURLConnection.HTTP_OK));

        //Restore inital test state
        userService.removeUser(user.getId());
        userService.removeUser(user2.getId());
        Assert.assertThat(" Must be Empty 0 roles", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPostWithoutParams() {
        Request request = new Request(null, null, null,null);
        Response response = userController.doPost(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPostOneUser() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID, "test");

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPostOneRoleOfOneUser() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID, "userTest");
        pathParams.put(ROLE_ID, "rolesTest");

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented", response, instanceOf(ResponseNotImplemented.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPostUnsupportedMediaType() {
        Map<String, String> pathParams = new HashMap<>();

        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doPost(request);
        Assert.assertThat(" Response must be ResponseUnsupportedMediaType", response, instanceOf(ResponseUnsupportedMediaType.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutWithoutParams() {
        Request request = new Request(null, null, null,null);
        Response response = userController.doPut(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutUnsupportedMediaType() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,"test1");
        Request request = new Request(null, pathParams, null,null);

        Response response = userController.doPut(request);
        Assert.assertThat(" Response must be ResponseUnsupportedMediaType", response, instanceOf(ResponseUnsupportedMediaType.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutUserCollection() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));

        List<WriteUser> users = new ArrayList<>();
        users.add(userConverter.entityToWriteDTO(userExpected));
        String rawBody = MAPPER.writeValueAsString(users);

        Map<String, String> pathParams = new HashMap<>();
        Request request = new JsonRequest(null,pathParams,rawBody);
        Response response = controller.doPut(request);

        UserService.getInstance().removeUser(userExpected.getId());
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(ResponseNotImplemented.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));

    }


    @Test
    public void doPutRoleCollection() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));

        List<WriteUser> users = new ArrayList<>();
        users.add(userConverter.entityToWriteDTO(userExpected));
        String rawBody = MAPPER.writeValueAsString(users);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");

        Request request = new JsonRequest(null,pathParams,rawBody);
        Response response = controller.doPut(request);
        UserService.getInstance().removeUser(userExpected.getId());
        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(ResponseNotImplemented.class));
        Assert.assertThat(" Must be Empty 0 roles",  RoleService.getInstance().getRoles().size(), is(0));
    }


    @Test
    public void doPutOneRoleOfUserRoleNotExists() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));
        UserService.getInstance().addUser(userExpected);

        WriteRole writeRole = new WriteRole("roleTest1");

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");
        pathParams.put(ROLE_ID,writeRole.getRole());

        Request request = new Request(null,pathParams,null,null);
        Response response = controller.doPut(request);
        Assert.assertThat(" Response must be ResponseNotFound", response, instanceOf(ResponseNotFound.class));

        UserService.getInstance().removeUser(userExpected.getId());
        Assert.assertThat(" Must be Empty 0 roles", RoleService.getInstance().getRoles().size(), is(0));
    }

    @Test
    public void doPutOneRoleOfUserUserNotExists() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));
        WriteRole writeRole = new WriteRole("roleTest1");

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");
        pathParams.put(ROLE_ID,writeRole.getRole());

        Request request = new Request(null,pathParams,null,null);;
        Response response = controller.doPut(request);
        Assert.assertThat(" Response must be ResponseNotFound", response, instanceOf(ResponseNotFound.class));

    }



    @Test
    public void doPutOneRoleOfUserRoleExists() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        RoleService.getInstance().addRole(new Role("roleTest1"));

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));
        UserService.getInstance().addUser(userExpected);

        WriteRole writeRole = new WriteRole("roleTest1");
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");
        pathParams.put(ROLE_ID, writeRole.getRole());

        WriteUser writeUser = userConverter.entityToWriteDTO(userExpected);
        writeUser.addRole(writeRole);
        String rawResponse = MAPPER.writeValueAsString(writeUser);

        Request request = new Request(null,pathParams,null,null);
        Response response = controller.doPut(request);
        Assert.assertThat(" Response must be jsonResponse ", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be user", response.getOutput(), is(rawResponse));

        UserService.getInstance().removeUser(userExpected.getId());
        RoleService.getInstance().removeRole("roleTest1");
    }


    @Test
    public void doPutOneUsersExists() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        RoleService.getInstance().addRole(new Role("roleTest1"));

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));
        UserService.getInstance().addUser(userExpected);

        WriteRole writeRole = new WriteRole("roleTest1");
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");
        pathParams.put(ROLE_ID, writeRole.getRole());

        WriteUser writeUser = userConverter.entityToWriteDTO(userExpected);
        writeUser.addRole(writeRole);
        String rawResponse = MAPPER.writeValueAsString(writeUser);

        Request request = new Request(null,pathParams,null,null);
        Response response = controller.doPut(request);
        Assert.assertThat(" Response must be jsonResponse ", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be user", response.getOutput(), is(rawResponse));

        UserService.getInstance().removeUser(userExpected.getId());
        RoleService.getInstance().removeRole(writeRole.getRole());
    }


    @Test
    public void doPutUser() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        RoleService.getInstance().addRole(new Role("roleTest1"));

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));
        UserService.getInstance().addUser(userExpected);

        WriteRole writeRole = new WriteRole("roleTest1");
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,userExpected.getId());
        pathParams.put(PATH_ROLES, "roles");
        pathParams.put(ROLE_ID, writeRole.getRole());

        WriteUser writeUser = userConverter.entityToWriteDTO(userExpected);
        writeUser.addRole(writeRole);
        String rawResponse = MAPPER.writeValueAsString(writeUser);

        Request request = new Request(null,pathParams,null,null);;
        Response response = controller.doPut(request);
        Assert.assertThat(" Response must be jsonResponse ", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be user", response.getOutput(), is(rawResponse));

        UserService.getInstance().removeUser(userExpected.getId());
        RoleService.getInstance().removeRole(writeRole.getRole());
    }






    @Test
    public void doPostUserCollection() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);
        Assert.assertThat("Service  must be only one user", userService.getUsers().size(), is(1));

        User userExpected = new User("test1", "test1");
        userExpected.addRole(new Role("test1"));

        List<WriteUser> users = new ArrayList<>();
        users.add(userConverter.entityToWriteDTO(userExpected));
        String rawBody = MAPPER.writeValueAsString(users);

        Map<String, String> pathParams = new HashMap<>();
        Request request = new JsonRequest(null,pathParams,rawBody);
        Response response = controller.doPost(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be users", response.getOutput(), is(MAPPER.writeValueAsString(users)));
        Assert.assertThat(" Service  must be only one user", userService.getUsers().size(), is(1));
        Assert.assertThat(" User save successfull", userService.getUser(userExpected.getId()).getId(), is(userExpected.getId()));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_CREATED, HttpURLConnection.HTTP_CREATED, is(HttpURLConnection.HTTP_CREATED));

        //Restore inital test state
        userService.removeUser(userExpected.getId());
        Assert.assertThat("Service  must be 0 users", userService.getUsers().size(), is(0));
    }

    @Test
    public void doPostRoleCollection() throws JsonProcessingException {

        UserControllerRest controller = new UserControllerRest();
        UserService userService = UserService.getInstance();

        User user = new User("test", "test");
        user.addRole(new Role("test"));
        userService.addUser(user);
        Assert.assertThat("Service  must be only one user", userService.getUsers().size(), is(1));

        List<WriteRole> roles = new ArrayList<>();
        WriteRole role1 = new WriteRole("role1");
        WriteRole role2 = new WriteRole("role2");
        roles.add(role1);
        roles.add(role2);
        String rawBody = MAPPER.writeValueAsString(roles);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID,user.getId());
        pathParams.put(PATH_ROLES,"roles");

        Request request = new JsonRequest(null,pathParams,rawBody);
        Response response = controller.doPost(request);

        Assert.assertThat(" Response must be jsonResponse", response, instanceOf(JsonResponse.class));
        Assert.assertThat(" Response must be roles", response.getOutput(), is(MAPPER.writeValueAsString(roles)));
        Assert.assertThat(" Service  must be only one user", userService.getUsers().size(), is(1));
        Assert.assertThat(" Only are one user", userService.getUser(user.getId()).getId(), is(user.getId()));
        Assert.assertThat(" User save 2 roles successfull", userService.getUser(user.getId()).getRoles().size(), is(2));
        Assert.assertThat(" Response status must be " + HttpURLConnection.HTTP_CREATED, HttpURLConnection.HTTP_CREATED, is(HttpURLConnection.HTTP_CREATED));

        //Restore inital test state
        userService.removeUser(user.getId());
        Assert.assertThat("Service  must be 0 users", userService.getUsers().size(), is(0));
    }


    @Test
    public void doDeleteEmptyParams() {
        Request request = new Request(null, null, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
    }

    @Test
    public void doDeleteWithoutUserParams() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badparam", "badparam");
        Request request = new Request(null, null, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseBadRequest", response, instanceOf(ResponseBadRequest.class));
    }

    @Test
    public void doDeleteUserNotExistParams() {
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("userId", "test");
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseNotFound", response, instanceOf(ResponseNotFound.class));
    }

    @Test
    public void doDeleteRolesUserParams() {
        Map<String, String> pathParams = new HashMap<>();
        UserService userService = UserService.getInstance();

        //Prepare test cases
        User userTest = new User("test","test");
        List<Role> roles = Arrays.asList(new Role("role1"), new Role("role2"));
        userTest.addRoles(roles);
        userService.addUser(userTest);

        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        pathParams.put("userId", "test");
        pathParams.put("roles", "");
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" There are 0 roles", userService.validateUser(userTest), nullValue());
        Assert.assertThat("Not delete user", userService.getUsers().size(), is(0));

    }

    @Test
    public void doDeleteRolesUserNullPathParams() {
        Map<String, String> pathParams = new HashMap<>();
        UserService userService = UserService.getInstance();

        //Prepare test cases
        User userTest = new User("test","test");
        List<Role> roles = Arrays.asList(new Role("role1"), new Role("role2"));
        userTest.addRoles(roles);
        userService.addUser(userTest);


        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        pathParams.put("userId", "test");
        pathParams.put("roles", null);
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" There are 0 roles", userService.validateUser(userTest), nullValue());
        Assert.assertThat("Not delete user", userService.getUsers().size(), is(0));


    }

    @Test
    public void doDeleteRolesPathParams() {
        Map<String, String> pathParams = new HashMap<>();
        UserService userService = UserService.getInstance();

        //Prepare test cases
        User userTest = new User("test","test");
        List<Role> roles = Arrays.asList(new Role("role1"), new Role("role2"));
        userTest.addRoles(roles);
        userService.addUser(userTest);

        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        pathParams.put("userId", "test");
        pathParams.put("roles", "roles");
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(0));
        Assert.assertThat("Not delete user", userService.getUsers().size(), is(1));
        //Restore state test
        userService.removeUser(userTest.getId());
        Assert.assertThat(" There are 0 users", userService.getUsers().size(), is(0));

    }


    @Test
    public void doDeleteExistingRolePathParams() {
        Map<String, String> pathParams = new HashMap<>();
        UserService userService = UserService.getInstance();

        //Prepare test cases
        User userTest = new User("test","test");
        List<Role> roles = Arrays.asList(new Role("role1"), new Role("role2"));
        userTest.addRoles(roles);
        userService.addUser(userTest);


        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        pathParams.put("userId", "test");
        pathParams.put("roles", "roles");
        pathParams.put("roleId", "role1");
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" There are 1 roles", userService.validateUser(userTest).getRoles().size(), is(1));
        Assert.assertThat("Not delete user", userService.getUsers().size(), is(1));
        //Restore state test
        userService.removeUser(userTest.getId());
        Assert.assertThat(" There are 0 users", userService.getUsers().size(), is(0));

    }

    @Test
    public void doDeleteNotExistingRolePathParams() {
        Map<String, String> pathParams = new HashMap<>();
        UserService userService = UserService.getInstance();

        //Prepare test cases
        User userTest = new User("test","test");
        List<Role> roles = Arrays.asList(new Role("role1"), new Role("role2"));
        userTest.addRoles(roles);
        userService.addUser(userTest);


        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        pathParams.put("userId", "test");
        pathParams.put("roles", "roles");
        pathParams.put("roleId", "Fakerole");
        Request request = new Request(null, pathParams, null,null);
        Response response = userController.doDelete(request);
        Assert.assertThat(" Response must be ResponseEmpty", response, instanceOf(ResponseEmpty.class));
        Assert.assertThat(" There are 2 roles", userService.validateUser(userTest).getRoles().size(), is(2));
        Assert.assertThat("Not delete user", userService.getUsers().size(), is(1));
        //Restore state test
        userService.removeUser(userTest.getId());
        Assert.assertThat(" There are 0 users", userService.getUsers().size(), is(0));

    }


}
