package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.entities.User;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
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
public class HtmlControllerTest {


    private final static User mockUserSession = new User("test1","test2");
    private static final String COOKIE = "Cookie";

    private class MockHtmlController extends  HtmlController{

        @Override
        public Pattern getURLPattern() {
            return Pattern.compile("");
        }

        @Override
        public List<String> getPathParams() {
            return new ArrayList<>();

        }
    }

    private class SessionServiceWithoutSession implements SessionServiceInterface{

        @Override
        public void delete(String id) {

        }

        @Override
        public Session getSession(String sessionId) {
            return null;
        }

        @Override
        public Session createSession(User user) {
            return null;
        }
    }

    private class SessionServiceWithSession implements SessionServiceInterface{

        @Override
        public void delete(String id) {

        }

        @Override
        public Session getSession(String sessionId) {
            return new Session(mockUserSession,sessionId);
        }

        @Override
        public Session createSession(User user) {
            return null;
        }
    }

    private static Request emptyRequest(){
        return new Request(null,new HashMap<>(),null,null);
    }


    @Test
    public void getBadHeadersTest(){

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        Assert.assertThat("HtmlControllerTest getBadHeadersTest isnull",
                mockHtmlController.getBadHeaders(MockHtmlController.METHOD_GET,new Headers(),
                        ContentType.APPLICATION_JSON,emptyRequest()), is(nullValue()));

    }

    @Test
    public void getBadMethods(){

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        Assert.assertThat("HtmlControllerTest getBadMethods method get",
                mockHtmlController.checkMethodAllowed(MockHtmlController.METHOD_GET), is(Boolean.TRUE));
        Assert.assertThat("HtmlControllerTest getBadMethods method post",
                mockHtmlController.checkMethodAllowed(MockHtmlController.METHOD_POST), is(Boolean.TRUE));
        Assert.assertThat("HtmlControllerTest getBadMethods method put",
                mockHtmlController.checkMethodAllowed(MockHtmlController.METHOD_PUT), is(Boolean.FALSE));
        Assert.assertThat("HtmlControllerTest getBadMethods method delete",
                mockHtmlController.checkMethodAllowed(MockHtmlController.METHOD_DELETE), is(Boolean.FALSE));
        Assert.assertThat("HtmlControllerTest getBadMethods method patch",
                mockHtmlController.checkMethodAllowed("PATCH"), is(Boolean.FALSE));

        Assert.assertThat("HtmlControllerTest getBadMethods method delete",
                mockHtmlController.doDelete(emptyRequest()), is(new ResponseNotImplemented()));
        Assert.assertThat("HtmlControllerTest getBadMethods method get",
                mockHtmlController.doGet(emptyRequest()), is(new ResponseNotImplemented()));
        Assert.assertThat("HtmlControllerTest getBadMethods method post",
                mockHtmlController.doPost(emptyRequest()), is(new ResponseNotImplemented()));
        Assert.assertThat("HtmlControllerTest getBadMethods method put",
                mockHtmlController.doPut(emptyRequest()), is(new ResponseNotImplemented()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullPathParams() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        mockHtmlController.getRequest(null, null,  ClassLoader.getSystemResourceAsStream(("test.html"))
                , ContentType.APPLICATION_JSON, new Headers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullHeaders() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        mockHtmlController.getRequest(new HashMap<>(), null,   ClassLoader.getSystemResourceAsStream(("test.html")),
                ContentType.APPLICATION_JSON, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullRequestBody() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        mockHtmlController.getRequest(new HashMap<>(), null,  null, ContentType.APPLICATION_JSON, new Headers());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getReponseNullContentType() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        mockHtmlController.getRequest(new HashMap<>(), null,   ClassLoader.getSystemResourceAsStream(("test.html")),
                null, new Headers());



    }

    @Test(expected = IOException.class)
    public void getReponsBadRequestBody() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());
        mockHtmlController.getRequest(new HashMap<>(), null,  new FileInputStream(""),ContentType.APPLICATION_JSON,
                new Headers());
    }

    @Test
    public void getReponsWithoutSession() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithoutSession());

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));


        Request request =
                mockHtmlController.getRequest(new HashMap<>(), null,
                        ClassLoader.getSystemResourceAsStream(("test.html")),
                ContentType.APPLICATION_JSON,
                        headers);

        Assert.assertThat("HtmlControllerTest getReponsWithoutSession pathParams",
                request.getPathParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithoutSession queryParams",
                request.getQueryParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithoutSession rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("HtmlControllerTest getReponsWithoutSession getSession",
                request.getSession(), is(nullValue()));
    }


    @Test
    public void getReponsWithSession() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithSession());

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));

        Request request =
                mockHtmlController.getRequest(new HashMap<>(), null,
                        ClassLoader.getSystemResourceAsStream(("test.html")),
                        ContentType.APPLICATION_JSON,
                        headers);

        Assert.assertThat("HtmlControllerTest getReponsWithSession pathParams",
                request.getPathParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithSession queryParams",
                request.getQueryParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithSession rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("HtmlControllerTest getReponsWithSession getSession",
                request.getSession(), is(new Session(mockUserSession, expectedSessionId)));
    }

    @Test
    public void getReponsWithSessionAndQueryParamsWithoutUrlEnconded() throws IOException {
        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithSession());

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));
        String goodBody = "id1=14&id2=20";
        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));

        Request request =
                mockHtmlController.getRequest(new HashMap<>(), null,
                        is,
                        ContentType.APPLICATION_JSON,
                        headers);

        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded pathParams",
                request.getPathParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded queryParams",
                request.getQueryParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithoutUrlEnconded getSession",
                request.getSession(), is(new Session(mockUserSession, expectedSessionId)));
    }

    @Test
    public void getReponsWithSessionAndQueryParamsWithUrlEnconded() throws IOException {

        MockHtmlController mockHtmlController = new MockHtmlController();
        mockHtmlController.setSessionService(new SessionServiceWithSession());

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE, Arrays.asList("Session="+expectedSessionId));
        String goodBody = "id1=14&id2=20";
        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));
        Map expectedParams = RequestUtils.parseQueryParamsUrlEnconded(is, Charset.defaultCharset());

        Request request =
                mockHtmlController.getRequest(new HashMap<>(), null,
                        new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8)),
                        ContentType.APPLICATION_FORM_URLENCODED,
                        headers);

        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded pathParams",
                request.getPathParams(), is(new HashMap<>()));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded queryParams",
                request.getQueryParams(), is(expectedParams));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded rawBody",
                request.getRawBody(), is(""));
        Assert.assertThat("HtmlControllerTest getReponsWithSessionAndQueryParamsWithUrlEnconded getSession",
                request.getSession(), is(new Session(mockUserSession, expectedSessionId)));
    }
}
