package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.view.LoginResponse;
import com.onoguera.loginwebapp.view.PageResponse;
import com.onoguera.loginwebapp.view.Response;
import com.onoguera.loginwebapp.view.ResponseNotImplemented;
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
public class PageControllerTest {

    private PageController pageController = new PageController();
    private final static User USER_TEST = new User("test","test");
    private final static Role ROLE_TEST1 = new Role("PAGE_1");
    private final static Role ROLE_TEST2 = new Role("PAGE_2");
    private final static Role ROLE_TEST3 = new Role("PAGE_3");
    private static final String PAGE_ID = "pageId";

    @Test
    public void doGetWithoutSessionTest(){

        Map<String, String> pathParams = new HashMap<>();
        Request request = new Request(null,pathParams,null,null);
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));

    }

    @Test
    public void doGetWithSessionWihoutRolesTest(){

        Map<String, String> pathParams = new HashMap<>();
        Request request = new Request(null,pathParams,null,new Session(USER_TEST ,"1"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));
    }

    @Test
    public void doGetPage1(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID,"1");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST1);

        Request request = new Request(null,pathParams,null,new Session(user ,"1"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be PageResponse ", response, instanceOf(PageResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        PageResponse pageResponse = (PageResponse)response;
        Assert.assertThat(" Location must be page_1", pageResponse.getLocation(), is( "page_1"));
    }

    @Test
    public void doGetPage1WithoutRoles(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID, "1");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());

        Request request = new Request(null,pathParams,null,new Session(user ,"1"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);

        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));
    }

    @Test
    public void doGetPage2(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID, "2");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST2);

        Request request = new Request(null,pathParams,null,new Session(user ,"2"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(PageResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        PageResponse pageResponse = (PageResponse)response;
        Assert.assertThat(" Location must be page_2", pageResponse.getLocation(), is( "page_2"));

    }

    @Test
    public void doGetPage2WithRole1(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID, "2");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST1);

        Request request = new Request(null,pathParams,null,new Session(user ,"2"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(LoginResponse.class));
        Assert.assertThat(" Response must 302 status ", response.getHttpStatus(), is(302));
        LoginResponse loginResponse = (LoginResponse)response;
        Assert.assertThat(" Location must be login", loginResponse.getLocation(), is( "login"));
    }

    @Test
    public void doGetPage2WithRole1AndRole2(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID, "2");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST1);
        user.addRole(ROLE_TEST2);

        Request request = new Request(null,pathParams,null,new Session(user ,"2"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(PageResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        PageResponse pageResponse = (PageResponse)response;
        Assert.assertThat(" Location must be page_2", pageResponse.getLocation(), is( "page_2"));
    }

    @Test
    public void doGetPage1WithRole1AndRole2(){

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(PAGE_ID, "1");

        User user = new User(USER_TEST.getId(),USER_TEST.getPassword());
        user.addRole(ROLE_TEST1);
        user.addRole(ROLE_TEST2);

        Request request = new Request(null,pathParams,null,new Session(user ,"1"));
        pageController.setSessionService(new SessionMockServices.SessionServiceMock());
        Response response = pageController.doGet(request);
        Assert.assertThat(" Response must be LoginResponse ", response, instanceOf(PageResponse.class));
        Assert.assertThat(" Response must 200 status ", response.getHttpStatus(), is(200));
        PageResponse pageResponse = (PageResponse)response;
        Assert.assertThat(" Location must be page_1", pageResponse.getLocation(), is( "page_1"));
    }




    @Test
    public void doPostTest(){
        Request request = new Request(null,null,null,null);
        Response response = pageController.doPost(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doDeleteTest(){
        Request request = new Request(null,null,null,null);
        Response response = pageController.doDelete(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void doPutTest(){
        Request request = new Request(null,null,null,null);
        Response response = pageController.doPut(request);
        Assert.assertThat(" Response must be ResponseNotImplemented ", response, instanceOf(ResponseNotImplemented.class));
    }

    @Test
    public void checkAuthAndRestAPITest(){
        Assert.assertThat(" checkAuthAndRestAPI must be return null ", pageController.checkAuthAndRestAPI(null, null, null, null), nullValue());
    }
}
