package com.onoguera.loginwebapp.restcontroller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.JsonResponse;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseEmpty;
import com.onoguera.loginwebapp.response.ResponseNotFound;
import com.onoguera.loginwebapp.service.RoleConverter;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.UserConverter;
import com.onoguera.loginwebapp.service.UserServiceInterface;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 29/04/2017.
 */
public class UserControllerRestTest {

    private final static Role MOCK_ROLE1 = new Role("ROLE1");
    private final static Role MOCK_ROLE2 = new Role("ROLE2");

    private final static User MOCK_USER_1 = new User("mockUser1", "mockpassword1",
                     Arrays.asList(MOCK_ROLE1));
    private final static User MOCK_USER_2 = new User("mockUser2", "mockpassword2",
            Arrays.asList(MOCK_ROLE1, MOCK_ROLE2));

    private static final String USER_ID = "userId";

    private static final String ROLE_ID = "roleId";

    private static final String PATH_ROLES = "roles";

    private class UserServiceMock implements UserServiceInterface{

        protected  Set<User> users ;
        protected  UserConverter userConverter;
        protected  Set<String> usersIds;
        protected  Set<ReadUser> readUsers;
        protected  Set<WriteUser> writeUsers;

        private void recalc(){

            usersIds = users.stream().map(u->u.getId()).collect(Collectors.toSet());
            readUsers = users.stream().map(u->userConverter.entityToReadDTO(u)).collect(Collectors.toSet());
            writeUsers = users.stream().map(u->userConverter.entityToWriteDTO(u)).collect(Collectors.toSet());
        }

        public UserServiceMock(User ... users){

            this.users = new HashSet(Arrays.asList(users));
            userConverter = UserConverter.getInstance();
            this.recalc();
        }

        @Override
        public User validateUser(User user) {
            if( users.contains(user)){
                return user;
            }else{
                return null;
            }
        }

        @Override
        public Collection<ReadUser> getReadUsers() {
            return readUsers;
        }

        @Override
        public ReadUser getReadUser(String userId) {
            return readUsers.stream().filter(u->u.getUsername().equals(userId)).findFirst().orElse(null);
        }

        @Override
        public WriteUser getWriteUser(String userId) {
            return writeUsers.stream().filter(u->u.getUsername().equals(userId)).findFirst().orElse(null);
        }

        @Override
        public void updateWriteUser(WriteUser writeUser) {
            users.add(userConverter.writeDTOtoEntity(writeUser));
            this.recalc();
        }

        @Override
        public void removeAllUsers() {
            users = new HashSet<>();
            this.recalc();
        }

        @Override
        public void createWriteUsers(List<WriteUser> usersBody) {
            users = usersBody.stream().map(u->userConverter.writeDTOtoEntity(u)).collect(Collectors.toSet());
            this.recalc();
        }

        @Override
        public User getUser(String userId) {
            return users.stream().filter(u->u.getId().equals(userId)).findFirst().orElse(null);
        }

        @Override
        public void updateUser(User user) {
            if( validateUser(user) != null){
                users.add(user);
                this.recalc();
            }

        }

        @Override
        public void removeUser(String userId) {
            User user =  this.getUser(userId);
            users.remove(user);
            this.recalc();

        }
    }

    @Test
    public void doGetCollectionUsers(){

        UserControllerRest userControllerRest = new UserControllerRest(new UserServiceMock(),
                RoleService.getInstance());

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = null;
        JsonResponse expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,new ArrayList<>());
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = userControllerRest.doGet(request);

        Assert.assertThat("UserControllerRestTest doGetCollectionUsers empty users null pathparams",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers empty users empty pathparams",
                response, is(expectedResponse));

        UserServiceMock userServiceMock = new UserServiceMock(MOCK_USER_1);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers  user 1",
                response, is(expectedResponse));


        userServiceMock = new UserServiceMock(MOCK_USER_2);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers  user 2",
                response, is(expectedResponse));

        userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers two users",
                response, is(expectedResponse));


    }


    @Test
    public void doGetUser(){

        UserServiceMock userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badKey", MOCK_USER_1.getId());
        Response expectedResponse = new ResponseBadRequest();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser bad key user", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, "usernamenotexists");
        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username not exists", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, MOCK_USER_1.getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(MOCK_USER_1.getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, MOCK_USER_2.getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(MOCK_USER_2.getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 2", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, MOCK_USER_1.getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse =
                new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(MOCK_USER_1.getId()).getRoles());
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Roles", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, MOCK_USER_1.getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, "notexists");
        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Role not exists", response,
                is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(USER_ID, MOCK_USER_1.getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,
                userServiceMock.getReadUser(MOCK_USER_1.getId()).getRoles().stream().findFirst().get());
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Role not exists", response,
                is(expectedResponse));

    }

    @Test
    public void doDeleteUsers(){

        UserServiceMock userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badKey", MOCK_USER_1.getId());
        Response expectedResponse = new ResponseBadRequest();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete User bad key user", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, "usernamenotexists");
        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete username not exists", response, is(expectedResponse));


        pathParams = new HashMap<>();
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete username not exists", response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete username 1 not exists",
                userServiceMock.getReadUser(MOCK_USER_1.getId()), is(nullValue()));
        Assert.assertThat("UserControllerRestTest doDelete username 2 not exists",
                userServiceMock.getReadUser(MOCK_USER_2.getId()), is(nullValue()));



        pathParams = new HashMap<>();
        userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        pathParams.put(USER_ID, MOCK_USER_1.getId());
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete username 1", response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete username 1 not exists",
                userServiceMock.getReadUser(MOCK_USER_1.getId()), is(nullValue()));
        Assert.assertThat("UserControllerRestTest doDelete username 2  exists",
                userServiceMock.getReadUser(MOCK_USER_2.getId()),
                is(UserConverter.getInstance().entityToReadDTO(MOCK_USER_2)));


        pathParams = new HashMap<>();
        userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        pathParams.put(USER_ID, MOCK_USER_2.getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete roles of user 2 response", response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete roles of user 2",
                userServiceMock.getReadUser(MOCK_USER_2.getId()).getRoles().size(), is(Integer.valueOf(0)));
        Assert.assertThat("UserControllerRestTest doDelete  roles of user 2 username 2  exists",
                userServiceMock.getReadUser(MOCK_USER_2.getId()),
                is(UserConverter.getInstance().entityToReadDTO(MOCK_USER_2)));




        pathParams = new HashMap<>();
        MOCK_USER_2.setRoles(Arrays.asList(MOCK_ROLE1,MOCK_ROLE2));
        userServiceMock = new UserServiceMock(MOCK_USER_1,MOCK_USER_2);
        userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());
        pathParams.put(USER_ID, MOCK_USER_2.getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete role 1 of user 2 response", response,
                is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete role 1 of  user 2 size",
                userServiceMock.getReadUser(MOCK_USER_2.getId()).getRoles().size(), is(Integer.valueOf(1)));
        Assert.assertThat("UserControllerRestTest doDelete role 1 of  user 2 is role2",
                userServiceMock.getReadUser(MOCK_USER_2.getId()).getRoles().get(0),
                is(RoleConverter.getInstance().entityToReadDTO(MOCK_ROLE2)));
        Assert.assertThat("UserControllerRestTest doDelete  roles of user 2 username 2  exists",
                userServiceMock.getReadUser(MOCK_USER_2.getId()),
                is(UserConverter.getInstance().entityToReadDTO(MOCK_USER_2)));



    }

}
