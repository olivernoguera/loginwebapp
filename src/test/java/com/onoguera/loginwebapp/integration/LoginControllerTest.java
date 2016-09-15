package com.onoguera.loginwebapp.integration;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseBadRequest;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
import com.onoguera.loginwebapp.view.ResponseUnauthorized;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public class LoginControllerTest {

    private LoginController loginController = new LoginController();
    private final static User USER_TEST = new User("test","test");
    private final static Role ROLE_TEST1 = new Role("PAGE_1");
    private final static Role ROLE_TEST2 = new Role("PAGE_2");


    @Test
    public void doGetWithoutSessionTest(){

        Map<String, String> pathParams = new HashMap<>();
        Request request = new Request(null,pathParams,null,null);
        Response response = loginController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));

    }

    @Test
    public void doGetWithSessionWihoutRolesTest(){

        Map<String, String> pathParams = new HashMap<>();
        Request request = new Request(null,pathParams,null,new Session(USER_TEST ,"1"));

        Response response = loginController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));
    }

    @Test
    public void doGetPage1(){

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST1);

        Request request = new Request(null,null,null,new Session(user ,"1"));

        Response response = loginController.doGet(request);
        Assert.assertThat(" Response must be PageResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be page_1", loginResponse.getLocation(), is( "page_1"));
    }

    @Test
    public void doPostBadRequestTest(){
        Request request = new Request(null,null,null,null);
        Response response = loginController.doPost(request);
        Assert.assertThat(" Response must be ResponseBadRequest ", response, instanceOf(ResponseBadRequest.class));
    }

    @Test
    public void doPostInvalidUserTest(){

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username","test1");
        queryParams.put("password","test");
        Request request = new Request(queryParams,null,null,null);
        loginController.setUserService(new UserMockServices.UserServiceNotValidUserMock());

        Response response = loginController.doPost(request);
        Assert.assertThat(" Response must be ResponseUnauthorized ", response, instanceOf(ResponseUnauthorized.class));
    }

    @Test
    public void doPostPage1UserTest(){

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username","test");
        queryParams.put("password", "test");

        Request request = new Request(queryParams,null,null,null);
        loginController.setUserService(new UserMockServices.UserServicePage1Mock());
        loginController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = loginController.doPost(request);

        Assert.assertThat(" Response must be PageResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be page_1", loginResponse.getLocation(), is("page_1"));;
    }

    @Test
    public void doPostPage1WithoutRolesTest(){

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("username","test");
        queryParams.put("password", "test");

        Request request = new Request(queryParams,null,null,null);
        loginController.setUserService(new UserMockServices.UserServiceWithoutRolesMock());
        loginController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = loginController.doPost(request);

        Assert.assertThat(" Response must be PageResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is("login"));;
    }


    @Test
    public void doDeleteTest(){
        Request request = new Request(null,null,null,null);
        Response response = loginController.doDelete(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doPutTest(){
        Request request = new Request(null,null,null,null);
        Response response = loginController.doPut(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void checkAuthAndRestAPITest(){
        Assert.assertThat(" checkAuthAndRestAPI must be return null ", loginController.checkAuthAndRestAPI(null, null, null, null), nullValue());
    }

}
