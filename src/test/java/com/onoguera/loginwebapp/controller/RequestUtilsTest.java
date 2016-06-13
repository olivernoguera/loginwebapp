package com.onoguera.loginwebapp.controller;

import com.sun.net.httpserver.Headers;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by oliver on 7/06/16.
 */
public class RequestUtilsTest {


    private static final String CONTENT_TYPE_HEADER = "Content-Type:";

    private static final String CREDENTIALS_SEPARATOR = ":";

    private static final String AUTH_HEADER = "Authorization";

    private static final String CHARSET_HEADER = "charset:";

    private static final String CONTENT_TYPE_SEPARATOR = ";";

    private static final String BASIC_AUTH_HEADER = "Basic";

    private static final String DEFAULT_MIME_TYPE = ContentType.TEXT_HTML.getMimeType();

    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");


    @Test
    public void testNullHeaders() {
        ContentType contentType = RequestUtils.getContentType(null);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(null, contentType.getCharset());
        Assert.assertThat("testNullHeaders:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testNullHeaders:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testNullHeaders:authorizations must be null", authorization, nullValue());
    }


    @Test
    public void testWhitoutHeadersHeader() {

        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testWhitoutHeadersHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testWhitoutHeadersHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testWhitoutHeadersHeader:authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testNullHeader() {
        Headers headers = new Headers();
        headers.put(null, null);
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testNullHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testNullHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testNullHeaders:authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testNullHeader2() {
        Headers headers = new Headers();
        headers.put("header", null);
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testNullHeader2:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testNullHeader2:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testNullHeader2:authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testEmptyHeaders3() {
        Headers headers = new Headers();
        headers.put("header", new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testEmptyHeaders3:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testEmptyHeaders3:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testEmptyHeaders3:authorizations must be null", authorization, nullValue());
    }


    @Test
    public void testEmptyContentType() {
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testEmptyContentType:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testEmptyContentType:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testEmptyContentType:authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testEmptyAuth() {
        Headers headers = new Headers();
        headers.put(AUTH_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testEmptyAuth:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testEmptyAuth:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testEmptyAuth:authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testBadAuthHeader() {
        Headers headers = new Headers();
        String basicAuthBad = "basic";
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(basicAuthBad));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testBadAuthHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testBadAuthHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testBadAuthHeader:authorizations must be null", authorization, nullValue());

    }

    @Test
    public void testBadContentType() {
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testBadContentType:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testBadContentType:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testBadContentType:authorizations must be null", authorization, nullValue());
    }



    @Test
    public void testNumericAuthHeader() {

        Headers headers = new Headers();
        String basicAuthBad = "Basic11111";
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(basicAuthBad));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testNumericAuthHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testNumericAuthHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testNumericAuthHeader:authorizations must be null", authorization, nullValue());

    }

    @Test
    public void testOnlyUserAuthHeader() {
        //"User" like decode credential
        String header = "BasicVXNlcg==";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testOnlyUserAuthHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testOnlyUserAuthHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testOnlyUserAuthHeader:authorizations must be null", authorization, nullValue());


    }

    @Test
    public void testPasswordWithDoublePointAuthCredentialsHeader() {
        //"User" as user and "Pass:word"  as password decode credential
        String header = "Basic VXNlcjpQYXNzOndvcmQ=";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testOnlyUserAuthHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testOnlyUserAuthHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testOnlyUserAuthHeader:authorizations user must be", authorization.getUsername(), is("User"));
        Assert.assertThat("testOnlyUserAuthHeader:password must be Pass:word", authorization.getPassword(), is("Pass:word"));

    }

    @Test
    public void testNormalAuthCredentialsHeader() {
        String header = "Basic VXNlcjpQYXNzd29yZA==";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("testOnlyUserAuthHeader:contentType charset must be UTF-8", contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("testOnlyUserAuthHeader:contentType mimetype must be text/html", contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("testOnlyUserAuthHeader:authorizations user must be", authorization.getUsername(), is("User"));
        Assert.assertThat("testOnlyUserAuthHeader:password must be Pass:word", authorization.getPassword(), is("Password"));


    }


}
