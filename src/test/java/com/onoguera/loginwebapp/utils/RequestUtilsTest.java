package com.onoguera.loginwebapp.utils;

import com.onoguera.loginwebapp.controller.Authorization;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 24/04/2017.
 */
public class RequestUtilsTest {


    private static final String CONTENT_TYPE_HEADER = "Content-Type";

    private static final String CREDENTIALS_SEPARATOR = ":";

    private static final String AUTH_HEADER = "Authorization";

    private static final String CHARSET_HEADER = "charset";

    private static final String CONTENT_TYPE_SEPARATOR = ";";

    private static final String BASIC_AUTH_HEADER = "Basic";

    private static final String DEFAULT_MIME_TYPE = ContentType.TEXT_HTML.getMimeType();

    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");


    @Test
    public void testNullHeaders() {

        ContentType contentType = RequestUtils.getContentType(null);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(null, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testNullHeaders:contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testNullHeaders:contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testNullHeaders:authorizations must be null", authorization, nullValue());
    }


    @Test
    public void testWhitoutHeadersHeader() {

        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testWhitoutHeadersHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testWhitoutHeadersHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testWhitoutHeadersHeader authorizations must be null",
                authorization, nullValue());
    }

    @Test
    public void testNullHeader() {

        Headers headers = new Headers();
        headers.put(null, null);
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testNullHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testNullHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testNullHeaders authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testNullHeader2() {

        Headers headers = new Headers();
        headers.put("header", null);
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testNullHeader2 contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testNullHeader2 contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testNullHeader2 authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testEmptyHeaders3() {

        Headers headers = new Headers();
        headers.put("header", new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testEmptyHeaders3 contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testEmptyHeaders3 contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testEmptyHeaders3 authorizations must be null", authorization, nullValue());
    }


    @Test
    public void testEmptyContentType() {

        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testEmptyContentType contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testEmptyContentType contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testEmptyContentType authorizations must be null",
                authorization, nullValue());
    }

    @Test
    public void testEmptyAuth() {
        Headers headers = new Headers();
        headers.put(AUTH_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testEmptyAuth contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testEmptyAuth contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testEmptyAuth authorizations must be null", authorization, nullValue());
    }

    @Test
    public void testBadAuthHeader() {

        Headers headers = new Headers();
        String basicAuthBad = "basic";
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(basicAuthBad));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testBadAuthHeader:contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testBadAuthHeader:contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testBadAuthHeader:authorizations must be null", authorization, nullValue());

    }

    @Test
    public void testBadContentType() {

        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, new ArrayList<>());
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testBadContentType contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testBadContentType contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testBadContentType authorizations must be null",
                authorization, nullValue());
    }

    @Test
    public void testNumericAuthHeader() {

        Headers headers = new Headers();
        String basicAuthBad = "Basic11111";
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(basicAuthBad));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest testNumericAuthHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTesttest NumericAuthHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testNumericAuthHeader authorizations must be null",
                authorization, nullValue());

    }

    @Test
    public void testOnlyUserAuthHeader() {
        //"User" like decode credential
        String header = "BasicVXNlcg==";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testOnlyUserAuthHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testOnlyUserAuthHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testOnlyUserAuthHeader authorizations must be null",
                authorization, nullValue());


    }

    @Test
    public void testPasswordWithDoublePointAuthCredentialsHeader() {

        //"User" as user and "Pass:word"  as password decode credential
        String header = "Basic VXNlcjpQYXNzOndvcmQ=";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testPasswordWithDoublePointAuthCredentialsHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testPasswordWithDoublePointAuthCredentialsHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testPasswordWithDoublePointAuthCredentialsHeader authorizations user must be",
                authorization.getUsername(), is("User"));
        Assert.assertThat("RequestUtilsTest testPasswordWithDoublePointAuthCredentialsHeader password must be Pass:word",
                authorization.getPassword(), is("Pass:word"));

    }

    @Test
    public void testNormalAuthCredentialsHeader() {

        String header = "Basic VXNlcjpQYXNzd29yZA==";
        Headers headers = new Headers();
        ContentType contentType = RequestUtils.getContentType(headers);
        headers.put(AUTH_HEADER, Arrays.asList(header));

        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());
        Assert.assertThat("RequestUtilsTest testNormalAuthCredentialsHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest testNormalAuthCredentialsHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest testNormalAuthCredentialsHeader authorizations user must be",
                authorization.getUsername(), is("User"));
        Assert.assertThat("RequestUtilsTest testNormalAuthCredentialsHeader password must be Pass:word",
                authorization.getPassword(), is("Password"));
    }


    @Test
    public void testHtmlContentTypeUTF16() {

        String header = ContentType.TEXT_HTML.getMimeType()+ "; " +CHARSET_HEADER + " = "+ Charset.forName("utf-16").name();
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, Arrays.asList(header));
        ContentType contentType = RequestUtils.getContentType(headers);
        Assert.assertThat("RequestUtilsTest testHtmlContentTypeUTF16 contentType charset must be UTF-16",
                Charset.forName("UTF-16").name(), is(contentType.getCharset().name()));
        Assert.assertThat("RequestUtilsTest testHtmlContentTypeUTF16 contentType mimetype must be text/html",
                ContentType.TEXT_HTML.getMimeType(), is(contentType.getMimeType()));
    }


    @Test
    public void testHtmlContentTypeUTF8() {

        String header = ContentType.TEXT_HTML.getMimeType()+ "; " +CHARSET_HEADER + " = "+
                Charset.forName("UTF-8").name();
        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER,  Arrays.asList(header));
        ContentType contentType = RequestUtils.getContentType(headers);
        Assert.assertThat("RequestUtilsTest testHtmlContentTypeUTF8 getCharset",
                Charset.forName("utf-8").name(), is(contentType.getCharset().name()));
        Assert.assertThat("RequestUtilsTest testHtmlContentTypeUTF8 getMimeType",
                ContentType.TEXT_HTML.getMimeType(), is(contentType.getMimeType()));
    }


}
