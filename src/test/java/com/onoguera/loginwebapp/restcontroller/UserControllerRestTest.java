package com.onoguera.loginwebapp.restcontroller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadRole;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.JsonResponse;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseEmpty;
import com.onoguera.loginwebapp.response.ResponseNotFound;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.RoleConverter;
import com.onoguera.loginwebapp.service.RoleService;
import com.onoguera.loginwebapp.service.RoleServiceInterface;
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


    private static final String USER_ID = "userId";

    private static final String ROLE_ID = "roleId";

    private static final String PATH_ROLES = "roles";

    private static final RoleConverter roleConverter =  RoleConverter.getInstance();
    private static final UserConverter userConverter = UserConverter.getInstance();


    public static User getMockUser1(){
        User user = new User("mockUser1", "mockpassword1",     Arrays.asList(MOCK_ROLE1));
        return user;
    }

    public static User getMockUser2(){
        User user = new User("mockUser2", "mockpassword2", Arrays.asList(MOCK_ROLE1, MOCK_ROLE2));
        return user;
    }

    private class UserServiceMock implements UserServiceInterface{

        protected  Set<User> users ;
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
            User user = userConverter.writeDTOtoEntity(writeUser);
            this.removeUser(writeUser.getUsername());
            users.add(user);
            this.recalc();
        }

        @Override
        public void removeAllUsers() {
            users = new HashSet<>();
            this.recalc();
        }

        @Override
        public void createWriteUsers(List<WriteUser> usersBody) {
            List<User> newUsers = usersBody.stream().map(u->userConverter.writeDTOtoEntity(u)).collect(Collectors.toList());
            users.addAll(newUsers);
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

    private class RoleServiceMock implements RoleServiceInterface{

        protected  Set<Role> roles ;
        protected  Set<String> rolesIDs;
        protected  Set<ReadRole> readRoles;
        protected  Set<WriteRole> writeRoles;


        private void recalc(){

            rolesIDs = roles.stream().map(u->u.getId()).collect(Collectors.toSet());
            readRoles = roles.stream().map(u->roleConverter.entityToReadDTO(u)).collect(Collectors.toSet());
            writeRoles = roles.stream().map(u->roleConverter.entityToWriteDTO(u)).collect(Collectors.toSet());
        }
        public RoleServiceMock(Role ... roles){
            this.roles = new HashSet(Arrays.asList(roles));
            this.recalc();
        }

        @Override
        public Role getRole(String roleId) {
            return roles.stream().filter(r->r.getId().equals(roleId)).findFirst().orElse(null);
        }

        @Override
        public Collection<ReadRole> getReadRoles() {
            return readRoles;
        }

        @Override
        public ReadRole getReadRole(String roleId) {
            return readRoles.stream().filter(r->r.getRole().equals(roleId)).findFirst().orElse(null);
        }

        @Override
        public void addWriteRole(WriteRole writeRole) {
            Role role = roleConverter.writeDTOtoEntity(writeRole);
            this.removeRole(role.getId());
            this.roles.add(role);
            this.recalc();

        }

        @Override
        public void removeRole(String roleId) {
            Role user =  this.getRole(roleId);
            roles.remove(user);
            this.recalc();

        }
    }

    @Test
    public void doGetCollectionUsers(){

        UserControllerRest userControllerRest = new UserControllerRest(new UserServiceMock(),
                new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2));

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

        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1());
        userControllerRest = new UserControllerRest(userServiceMock,  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2));
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers  user 1",
                response, is(expectedResponse));


        userServiceMock = new UserServiceMock(getMockUser2());
        userControllerRest = new UserControllerRest(userServiceMock,  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2));
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers  user 2",
                response, is(expectedResponse));

        userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        userControllerRest = new UserControllerRest(userServiceMock,  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2));
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUsers());
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetCollectionUsers two users",
                response, is(expectedResponse));


    }


    @Test
    public void doGetUser(){

        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock, RoleService.getInstance());

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badKey", getMockUser1().getId());
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
        pathParams.put(USER_ID, getMockUser1().getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(getMockUser1().getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(getMockUser2().getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 2", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse =
                new JsonResponse(HttpURLConnection.HTTP_OK,userServiceMock.getReadUser(getMockUser1().getId()).getRoles());
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Roles", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, "notexists");
        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Role not exists", response,
                is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_OK,
                userServiceMock.getReadUser(getMockUser1().getId()).getRoles().stream().findFirst().get());
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doGet(request);
        Assert.assertThat("UserControllerRestTest doGetUser username 1 get Role not exists", response,
                is(expectedResponse));

    }

    @Test
    public void doDeleteUsers(){

        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,
                new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put("badKey", getMockUser1().getId());
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
                userServiceMock.getReadUser(getMockUser1().getId()), is(nullValue()));
        Assert.assertThat("UserControllerRestTest doDelete username 2 not exists",
                userServiceMock.getReadUser(getMockUser2().getId()), is(nullValue()));

        pathParams = new HashMap<>();
        userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        userControllerRest = new UserControllerRest(userServiceMock, new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));
        pathParams.put(USER_ID, getMockUser1().getId());
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete username 1", response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete username 1 not exists",
                userServiceMock.getReadUser(getMockUser1().getId()), is(nullValue()));
        Assert.assertThat("UserControllerRestTest doDelete username 2  exists",
                userServiceMock.getReadUser(getMockUser2().getId()),
                is(UserConverter.getInstance().entityToReadDTO(getMockUser2())));


        pathParams = new HashMap<>();
        userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        userControllerRest = new UserControllerRest(userServiceMock, new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));
        pathParams.put(USER_ID, getMockUser2().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete roles of user 2 response", response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete roles of user 2",
                userServiceMock.getReadUser(getMockUser2().getId()).getRoles().size(), is(Integer.valueOf(0)));
        Assert.assertThat("UserControllerRestTest doDelete  roles of user 1 username 1  exists",
                userServiceMock.getReadUser(getMockUser1().getId()),
                is(UserConverter.getInstance().entityToReadDTO(getMockUser1())));

        pathParams = new HashMap<>();

        userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        userControllerRest = new UserControllerRest(userServiceMock, new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));
        pathParams.put(USER_ID, getMockUser2().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doDelete(request);
        Assert.assertThat("UserControllerRestTest doDelete role 1 of user 2 response", response,
                is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doDelete role 1 of  user 2 size",
                userServiceMock.getReadUser(getMockUser2().getId()).getRoles().size(), is(Integer.valueOf(1)));
        Assert.assertThat("UserControllerRestTest doDelete role 1 of  user 2 is role2",
                userServiceMock.getReadUser(getMockUser2().getId()).getRoles().get(0),
                is(RoleConverter.getInstance().entityToReadDTO(MOCK_ROLE2)));
    }

    @Test
    public void doPostBadCallsUsers() {


        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(), getMockUser2());
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,  new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        Response expectedResponse = new ResponseNotImplemented();
        Request request = new Request(queryParams, pathParams, null, null);
        Response response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers /users/{user_id} not implemented , post only for collections",
                response, is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse = new ResponseNotImplemented();
        request = new Request(queryParams, pathParams, null, null);
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers /users/{user_id}/roles/{roleid} not implemented ," +
                " post only for collections", response, is(expectedResponse));


        pathParams = new HashMap<>();
        expectedResponse = new ResponseUnsupportedMediaType();
        request = new Request(queryParams, pathParams, null, null);
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers Not JsonRequest for collection users",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseUnsupportedMediaType();
        request = new Request(queryParams, pathParams, null, null);
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers Not JsonRequest for collection roles of users",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, "badUserId");
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseNotFound();
        request = new JsonRequest(queryParams, pathParams, "{}");
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers Not exists users for collection roles of users",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseBadRequest();
        request = new JsonRequest(queryParams, pathParams, "{badjson}");
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPostBadCallsUsers bad json por for collection roles of users",
                response, is(expectedResponse));
    }

    @Test
    public void doPostRolesOfUser() {


        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(), getMockUser2());
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,
                new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        Response expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED, Arrays.asList(
                roleConverter.entityToReadDTO(MOCK_ROLE1),roleConverter.entityToReadDTO(MOCK_ROLE2)));
        String roles = "  [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"role\": \"ROLE2\"\n" +
                "      }\n" +
                "    ]";
        Request request = new JsonRequest(queryParams,pathParams,roles);
        Response response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPost httpstatus for collection roles of users",
                response.getHttpStatus(), is(HttpURLConnection.HTTP_CREATED));
        Assert.assertThat("UserControllerRestTest doPost httpstatus for collection roles of users",
                response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doPost userservice two roles on user one",
                userServiceMock.getUser(getMockUser1().getId()).getRoles(), is(Arrays.asList(MOCK_ROLE1,MOCK_ROLE2)));


        pathParams = new HashMap<>();
        expectedResponse = new ResponseBadRequest();
        request = new JsonRequest(queryParams,pathParams,"{}");
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPost create users bad json",
                response, is(expectedResponse));

        pathParams = new HashMap<>();

        userServiceMock.removeAllUsers();
        Assert.assertThat("UserControllerRestTest doPost userservice empty users previuous to create",
                userServiceMock.getReadUsers().size(), is(Integer.valueOf(0)));
        getMockUser2().setRoles(Arrays.asList(MOCK_ROLE1,MOCK_ROLE2));
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,
                Arrays.asList(userConverter.entityToWriteDTO(getMockUser1()),userConverter.entityToWriteDTO(getMockUser2())));
        String users = "[\n" +
                "  {\n" +
                "    \"username\": \"mockUser1\",\n" +
                "    \"password\": \"mockpassword1\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      } "+
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"mockUser2\",\n" +
                "    \"password\": \"mockpassword2\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      } ,"+
                "      {\n" +
                "        \"role\": \"ROLE2\"\n" +
                "      } "+
                "    ]\n" +
                "  }\n" +
                "]";
        request = new JsonRequest(queryParams,pathParams,users);
        response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPost httpstatus for collection of users",
                response.getHttpStatus(), is(HttpURLConnection.HTTP_CREATED));

        Assert.assertThat("UserControllerRestTest doPost response for collection  of users",
                response.getOutput(), is(expectedResponse.getOutput()));
    }

    @Test
    public void doPostUsers() {


        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(), getMockUser2());
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,
                new RoleServiceMock(MOCK_ROLE2,MOCK_ROLE1));

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();

        Response expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,
                Arrays.asList(userConverter.entityToWriteDTO(getMockUser1()),userConverter.entityToWriteDTO(getMockUser2())));
        String users = "[\n" +
                "  {\n" +
                "    \"username\": \"mockUser1\",\n" +
                "    \"password\": \"mockpassword1\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      } "+
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"username\": \"mockUser2\",\n" +
                "    \"password\": \"mockpassword2\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      } ,"+
                "      {\n" +
                "        \"role\": \"ROLE2\"\n" +
                "      } "+
                "    ]\n" +
                "  }\n" +
                "]";
        Request request = new JsonRequest(queryParams,pathParams,users);
        Response response = userControllerRest.doPost(request);
        Assert.assertThat("UserControllerRestTest doPost httpstatus for collection of users",
                response.getHttpStatus(), is(HttpURLConnection.HTTP_CREATED));

        Assert.assertThat("UserControllerRestTest doPost response for collection  of users",
                response.getOutput(), is(expectedResponse.getOutput()));
    }

    @Test
    public void doPutBadCalls(){


        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        RoleServiceMock roleServiceMock =  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2);
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,roleServiceMock);


        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();
        Response expectedResponse = new ResponseNotImplemented();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPost /users/ not implemented , post only for single resource",
                response, is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        expectedResponse = new ResponseNotImplemented();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut /users/{user_id}/roles  not implemented ," +
                " post only for collections", response, is(expectedResponse));


        pathParams = new HashMap<>();
        expectedResponse = new ResponseUnsupportedMediaType();
        pathParams.put(USER_ID, getMockUser2().getId());
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut Not JsonRequest for user ",
                response, is(expectedResponse));



        pathParams = new HashMap<>();
        userControllerRest.getUserService().removeUser( getMockUser1().getId());
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE2.getId());

        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut add Role 2 create user1 not found no body",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        String users = "  {\n" +
                "    \"username\": \"mockUser1\",\n" +
                "    \"password\": \"mockpassword1\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      }, " +
                "      {\n" +
                "        \"role\": \"ROLE2\"\n" +
                "      } " +
                "    ]\n" +
                "  }";
        request = new JsonRequest(queryParams,pathParams,users);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut bad request because username is not path id",
                response, is(new ResponseBadRequest()));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser2().getId());
        request = new JsonRequest(queryParams,pathParams,"{}");
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut bad request null user",
                response, is(new ResponseBadRequest()));

        pathParams = new HashMap<>();


    }

    @Test
    public void doPutRoleOfUsers(){

        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        RoleServiceMock roleServiceMock =  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2);
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,roleServiceMock);
        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();

        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE2.getId());

        WriteUser expectedWriteUser = new WriteUser(getMockUser1().getId(),getMockUser1().getPassword(),
                Arrays.asList(new WriteRole(MOCK_ROLE1.getId()),new WriteRole(MOCK_ROLE2.getId())));
        Response expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,expectedWriteUser);

        Request request = new Request(queryParams,pathParams,null,null);
        Response response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut add Role 2 for user 1",
                response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doPut add Role 2 for user 1 writer user",
                userServiceMock.getWriteUser(getMockUser1().getId()), is(expectedWriteUser));

        pathParams = new HashMap<>();
        pathParams.put(USER_ID, getMockUser1().getId());
        pathParams.put(PATH_ROLES, PATH_ROLES);
        pathParams.put(ROLE_ID, MOCK_ROLE2.getId());

        expectedWriteUser = new WriteUser(getMockUser1().getId(),getMockUser1().getPassword(),
                Arrays.asList(new WriteRole(MOCK_ROLE1.getId()),new WriteRole(MOCK_ROLE2.getId())));
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,expectedWriteUser);

        request = new Request(queryParams,pathParams,null,null);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut add Role 2 for user 1 Idempotency",
                response, is(expectedResponse));
        Assert.assertThat("UserControllerRestTest doPut add Role 2 for user 1 writer user Idempotency",
                userServiceMock.getWriteUser(getMockUser1().getId()), is(expectedWriteUser));
    }


    @Test
    public void doPutUser(){

        UserServiceMock userServiceMock = new UserServiceMock(getMockUser1(),getMockUser2());
        RoleServiceMock roleServiceMock =  new RoleServiceMock(MOCK_ROLE1,MOCK_ROLE2);
        UserControllerRest userControllerRest = new UserControllerRest(userServiceMock,roleServiceMock);
        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();

        pathParams.put(USER_ID, getMockUser2().getId());
        getMockUser2().setRoles(Arrays.asList(MOCK_ROLE1,MOCK_ROLE2));
        Request request = new JsonRequest(queryParams,pathParams,"[]");
        Response response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut bad json",
                response, is(new ResponseBadRequest()));


        pathParams = new HashMap<>();
        String users = "  {\n" +
                "    \"username\": \"mockUser2\",\n" +
                "    \"password\": \"mockpassword2\",\n" +
                "    \"roles\": [\n" +
                "      {\n" +
                "        \"role\": \"ROLE1\"\n" +
                "      }, " +
                "      {\n" +
                "        \"role\": \"ROLE2\"\n" +
                "      } " +
                "    ]\n" +
                "  }";
        pathParams.put(USER_ID, getMockUser2().getId());
        Response expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,
                userConverter.entityToWriteDTO(getMockUser2()));
        request = new JsonRequest(queryParams,pathParams,users);
        response = userControllerRest.doPut(request);
        Assert.assertThat("UserControllerRestTest doPut httpstatus for user 2",
                response.getHttpStatus(), is(HttpURLConnection.HTTP_CREATED));
        Assert.assertThat("UserControllerRestTest doPut response  for create user 2",
                response.getOutput(), is(expectedResponse.getOutput()));
    }



}
