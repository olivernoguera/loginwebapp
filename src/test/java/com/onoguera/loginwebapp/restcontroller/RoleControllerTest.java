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
import com.onoguera.loginwebapp.service.RoleConverter;
import com.onoguera.loginwebapp.service.RoleServiceInterface;
import com.onoguera.loginwebapp.service.UserServiceInterface;
import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 01/05/2017.
 */
public class RoleControllerTest {

    private static final RoleConverter roleConverter =  RoleConverter.getInstance();

    private class RoleServiceMock implements RoleServiceInterface{

        protected Set<Role> roles;
        protected Set<String> roleIds;
        protected Set<ReadRole> readRoles;
        protected Set<WriteRole> writeRoles;

        private void recalc() {

            roleIds = roles.stream().map(u -> u.getId()).collect(Collectors.toSet());
            readRoles = roles.stream().map(u -> roleConverter.entityToReadDTO(u)).collect(Collectors.toSet());
            writeRoles = roles.stream().map(u -> roleConverter.entityToWriteDTO(u)).collect(Collectors.toSet());
        }

        private RoleServiceMock(List<Role> roles){
            this.roles = new HashSet<>(roles);
            this.recalc();
        }

        @Override
        public boolean existsRoles(List<Role> roles) {
           return false;
        }

        public Role getRole(final String roleId) {
            return (Role) roles.stream().filter(u->u.getId().equals(roleId)).findFirst().orElse(null);
        }


        public List<ReadRole> getReadRoles() {
            return roles.stream().map(r -> RoleConverter.getInstance().entityToReadDTO(r)).collect(Collectors.toList());
        }

        public ReadRole getReadRole(String roleId) {
            Role roleEntity = this.getRole(roleId);
            if( roleEntity == null){
                return null;
            }
            return  RoleConverter.getInstance().entityToReadDTO(roleEntity);
        }

        public void addWriteRole(WriteRole role) {
            this.roles.add(RoleConverter.getInstance().writeDTOtoEntity(role));
            this.recalc();
        }

        @Override
        public void removeRole(String roleId) {
            Role role = this.getRole(roleId);
            this.roles.remove(role);
            this.recalc();
        }
    }

    private class UserServiceMock implements  UserServiceInterface{

        @Override
        public User validateUser(User user) {
            return null;
        }

        @Override
        public List<ReadUser> getReadUsers() {
            return null;
        }

        @Override
        public User getUser(String userId) {
            return null;
        }

        @Override
        public ReadUser getReadUser(String userId) {
            return null;
        }

        @Override
        public boolean upsertUser(WriteUser writeUser) {
            return false;
        }

        @Override
        public boolean upsertUser(User user) {
            return false;
        }

        @Override
        public void removeUser(String id) {

        }

        @Override
        public boolean setUsers(List<WriteUser> users) {
            return false;
        }

        @Override
        public boolean upsertRolesOfUser(String userId, List<WriteRole> newroles) {
            return false;
        }

        @Override
        public void removeUsers() {

        }
    }

    private final static Role MOCK_ROLE1 = new Role("ROLE1");
    private final static Role MOCK_ROLE2 = new Role("ROLE2");

    private static final String ROLE_ID = "roleId";


    @Test
    public void doGetTest(){

        List<Role> roles = Arrays.asList(MOCK_ROLE1,MOCK_ROLE2);
        List<ReadRole> readRoles = Arrays.asList(new ReadRole(MOCK_ROLE1.getId()),new ReadRole(MOCK_ROLE2.getId()));
        RoleServiceInterface roleServiceMock =
                new RoleServiceMock(roles);
        RoleControllerRest roleControllerRest = new RoleControllerRest(new UserServiceMock(),roleServiceMock);

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = null;
        Response expectedResponse =
                new JsonResponse(HttpURLConnection.HTTP_OK,readRoles);
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = roleControllerRest.doGet(request);
        Assert.assertThat("RoleControllerTest doGetTest get users collection",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put("BadKey","15");
        expectedResponse = new ResponseBadRequest();
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doGet(request);
        Assert.assertThat("RoleControllerTest doGetTest bad key role", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID,"15");
        expectedResponse = new ResponseNotFound();
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doGet(request);
        Assert.assertThat("RoleControllerTest doGetTest role not exists", response, is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse =  new JsonResponse(HttpURLConnection.HTTP_OK,new ReadRole(MOCK_ROLE1.getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doGet(request);
        Assert.assertThat("RoleControllerTest doGetTest role exists", response, is(expectedResponse));
    }

    @Test
    public void doPutTest(){

        List<Role> roles = Arrays.asList(MOCK_ROLE1,MOCK_ROLE2);
        List<ReadRole> readRoles = Arrays.asList(new ReadRole(MOCK_ROLE1.getId()),new ReadRole(MOCK_ROLE2.getId()));
        RoleServiceInterface roleServiceMock =
                new RoleServiceMock(new ArrayList<>());
        RoleControllerRest roleControllerRest = new RoleControllerRest(new UserServiceMock(),roleServiceMock);

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = null;
        Response expectedResponse = new ResponseBadRequest();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = roleControllerRest.doPut(request);
        Assert.assertThat("RoleControllerTest doPutTest get users collection",
                response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put("BadKey","15");
        expectedResponse = new ResponseBadRequest();
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doPut(request);
        Assert.assertThat("RoleControllerTest doPutTest bad key role", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID,"15");
        expectedResponse = new JsonResponse(HttpURLConnection.HTTP_CREATED,new ReadRole("15"));
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doPut(request);
        Assert.assertThat("RoleControllerTest doPutTest role not exists", response, is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse =  new JsonResponse(HttpURLConnection.HTTP_CREATED,new ReadRole(MOCK_ROLE1.getId()));
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doPut(request);
        Assert.assertThat("RoleControllerTest doPutTest role exists", response, is(expectedResponse));
    }


    @Test
    public void doDeleteTest(){

        List<Role> roles = Arrays.asList(MOCK_ROLE1,MOCK_ROLE2);

        RoleServiceInterface roleServiceMock = new RoleServiceMock(roles);
        RoleControllerRest roleControllerRest = new RoleControllerRest(new UserServiceMock(),roleServiceMock);

        Map<String, String> queryParams = null;
        Map<String, String> pathParams = new HashMap<>();

        pathParams.put("BadKey","15");
        Response expectedResponse = new ResponseBadRequest();
        Request request = new Request(queryParams,pathParams,null,null);
        Response response = roleControllerRest.doDelete(request);
        Assert.assertThat("RoleControllerTest doDeleteTest bad key role", response, is(expectedResponse));

        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID,"15");
        expectedResponse = new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doDelete(request);
        Assert.assertThat("RoleControllerTest doDeleteTest role not exists", response, is(expectedResponse));


        pathParams = new HashMap<>();
        pathParams.put(ROLE_ID, MOCK_ROLE1.getId());
        expectedResponse =  new ResponseEmpty();
        request = new Request(queryParams,pathParams,null,null);
        response = roleControllerRest.doDelete(request);
        Assert.assertThat("RoleControllerTest doDeleteTest role exists", response, is(expectedResponse));
    }

}
