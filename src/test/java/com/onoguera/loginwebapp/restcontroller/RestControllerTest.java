package com.onoguera.loginwebapp.restcontroller;

import com.onoguera.loginwebapp.entities.Role;
import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.model.ReadUser;
import com.onoguera.loginwebapp.model.WriteRole;
import com.onoguera.loginwebapp.model.WriteUser;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.onoguera.loginwebapp.response.ResponseForbidden;
import com.onoguera.loginwebapp.response.ResponseUnauthorized;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.PageApiRoleService;
import com.onoguera.loginwebapp.service.UserService;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 29/04/2017.
 */
public class RestControllerTest {

    private static final User mockUserSession =
            new User("User","Pass:word", Arrays.asList(new Role("ROLE1")));
    private static final User mockUserSessionWriteAccess = new User("User","Pass:word",
            Arrays.asList(PageApiRoleService.WRITER_API_ROLE));
    private static final String COOKIE = "Cookie";
    private static final String AUTH_HEADER = "Authorization";

    protected class MockRestAuthController extends RestAuthController {


        public MockRestAuthController(UserService userService) {
            super(userService);
        }

        @Override
        public Pattern getURLPattern() {
            return Pattern.compile("");
        }

        @Override
        public List<String> getPathParams() {
            return new ArrayList<>();

        }

    }

    private class UserServiceMock implements UserService {

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
        public boolean upsertRolesOfUser(String userId, List<WriteRole> rolesBody) {
            return false;
        }

        @Override
        public void removeUsers() {

        }
    }

    private class UserServiceWithRolesMock extends UserServiceMock {

        @Override
        public User validateUser(User user) {

            return mockUserSession;
        }

    }

    private class UserServiceWithRolesWriteAccessMock extends UserServiceMock {

        @Override
        public User validateUser(User user) {

            return mockUserSessionWriteAccess;
        }

    }

    protected static Request emptyRequest(){
        return new Request(null,new HashMap<>(),null,null);
    }

    protected static Headers goodAuthHeaders(){

        String header = "Basic VXNlcjpQYXNzOndvcmQ=";
        Headers headers = new Headers();
        headers.put(AUTH_HEADER, Arrays.asList(header));
        return headers;
    }

    @Test
    public void getBadHeadersTest(){

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        Assert.assertThat("RestControllerTest getBadHeadersTest empty headers headers get",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_GET,new Headers(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(new ResponseUnauthorized()));


        mockRestAuthController = new MockRestAuthController(new UserServiceWithRolesMock());

        Assert.assertThat("RestControllerTest getBadHeadersTest empty users headers get with roles",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_GET,new Headers(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(new ResponseUnauthorized()));

        mockRestAuthController = new MockRestAuthController(new UserServiceWithRolesMock());

        Assert.assertThat("RestControllerTest getBadHeadersTest users on headers get with roles",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_GET,goodAuthHeaders(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(nullValue()));

        Assert.assertThat("RestControllerTest getBadHeadersTest users on headers post with read roles",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_POST,goodAuthHeaders(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(new ResponseForbidden()));

        mockRestAuthController = new MockRestAuthController(new UserServiceWithRolesWriteAccessMock());

        Assert.assertThat("RestControllerTest getBadHeadersTest users on headers post with write roles",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_POST,goodAuthHeaders(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(nullValue()));

        Assert.assertThat("RestControllerTest getBadHeadersTest post not valida mediaType",
                mockRestAuthController.getBadHeaders(mockRestAuthController.METHOD_POST,goodAuthHeaders(),
                        ContentType.TEXT_HTML,emptyRequest()), is(new ResponseUnsupportedMediaType()));

    }

    @Test
    public void getBadMethods(){

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());

        Assert.assertThat("RestControllerTest getBadMethods method get",
                mockRestAuthController.checkMethodAllowed(mockRestAuthController.METHOD_GET), is(Boolean.TRUE));
        Assert.assertThat("RestControllerTest getBadMethods method post",
                mockRestAuthController.checkMethodAllowed(mockRestAuthController.METHOD_POST), is(Boolean.TRUE));
        Assert.assertThat("RestControllerTest getBadMethods method put",
                mockRestAuthController.checkMethodAllowed(mockRestAuthController.METHOD_PUT), is(Boolean.TRUE));
        Assert.assertThat("RestControllerTest getBadMethods method delete",
                mockRestAuthController.checkMethodAllowed(mockRestAuthController.METHOD_DELETE), is(Boolean.TRUE));
        Assert.assertThat("RestControllerTest getBadMethods method patch",
                mockRestAuthController.checkMethodAllowed("PATCH"), is(Boolean.FALSE));


    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullPathParams() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        mockRestAuthController.getRequest(null, null,  ClassLoader.getSystemResourceAsStream(("test.html"))
                , ContentType.APPLICATION_JSON, new Headers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullHeaders() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        mockRestAuthController.getRequest(new HashMap<>(), null,   ClassLoader.getSystemResourceAsStream(("test.html")),
                ContentType.APPLICATION_JSON, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullRequestBody() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        mockRestAuthController.getRequest(new HashMap<>(), null,  null, ContentType.APPLICATION_JSON, new Headers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullContentType() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        mockRestAuthController.getRequest(new HashMap<>(), null,   ClassLoader.getSystemResourceAsStream(("test.html")),
                null, new Headers());



    }

    @Test(expected = IOException.class)
    public void getReponsBadRequestBody() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        mockRestAuthController.getRequest(new HashMap<>(), null,  new FileInputStream(""),ContentType.APPLICATION_JSON,
                new Headers());
    }

    @Test
    public void getReponsWithoutSession() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());
        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));


        Request request =
                mockRestAuthController.getRequest(new HashMap<>(), null,
                        ClassLoader.getSystemResourceAsStream(("test.html")),
                        ContentType.APPLICATION_JSON,
                        headers);

        Assert.assertThat("RestControllerTest getReponsWithoutSession pathParams",
                request.getPathParams(), is(new HashMap<>()));
        Assert.assertThat("RestControllerTest getReponsWithoutSession queryParams",
                request.getQueryParams(), is(new HashMap<>()));
        Assert.assertThat("RestControllerTest getReponsWithoutSession rawBody",
                request.getRawBody(), is("<html></html>"));
        Assert.assertThat("RestControllerTest getReponsWithoutSession getSession",
                request.getSession(), is(nullValue()));
    }


    @Test
    @Ignore
    public void getReponseQueryParamsWithoutUrlEnconded() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));
        String goodBody = "id1=14&id2=20";
        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));

        Request request =
                mockRestAuthController.getRequest(new HashMap<>(), null,
                        is,
                        ContentType.APPLICATION_JSON,
                        headers);

        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded pathParams",
                request.getPathParams(), is(new HashMap<>()));
        /**
         * TODO Review need implements parse queryparams and body in the same method, because inputstream request
         *  is consumed firsts time
         */
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded queryParams",
                request.getQueryParams(), is(new HashMap<>()));
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded getSession",
                request.getSession(), is(new Session(mockUserSession, expectedSessionId)));
    }

    @Test
    @Ignore
    public void getReponsQueryParamsWithUrlEnconded() throws IOException {

        MockRestAuthController mockRestAuthController = new MockRestAuthController(new UserServiceMock());


        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));
        String goodBody = "id1=14&id2=20";
        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));
        Map expectedParams = RequestUtils.parseQueryParamsUrlEnconded(is, Charset.defaultCharset());

        Request request =
                mockRestAuthController.getRequest(new HashMap<>(), null,
                        new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8)),
                        ContentType.APPLICATION_FORM_URLENCODED,
                        headers);

        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded pathParams",
                request.getPathParams(), is(new HashMap<>()));
        /**
         * TODO Review need implements parse queryparams and body in the same method, because inputstream request
         *  is consumed firsts time
         */
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded queryParams",
                request.getQueryParams(), is(expectedParams));
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("RestControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded getSession",
                request.getSession(), is(new Session(mockUserSession, expectedSessionId)));
    }
}
