package com.onoguera.loginwebapp.utils;

import com.onoguera.loginwebapp.controller.Authorization;
import com.onoguera.loginwebapp.controller.BaseController;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    private static final String COOKIE = "Cookie";


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
    public void nullContentTypeHeader() {

        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, Arrays.asList(null,"test"));
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest nullContentTypeHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest nullContentTypeHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest nullContentTypeHeader authorizations must be null", authorization, nullValue());
    }



    @Test
    public void nullCharsetTypeHeader() {

        Headers headers = new Headers();
        headers.put(CONTENT_TYPE_HEADER, Arrays.asList(DEFAULT_MIME_TYPE));
        ContentType contentType = RequestUtils.getContentType(headers);
        Authorization authorization  = RequestUtils.getAuthorizationFromHeader(headers, contentType.getCharset());

        Assert.assertThat("RequestUtilsTest nullCharsetTypeHeader contentType charset must be UTF-8",
                contentType.getCharset(), is(DEFAULT_CHARSET));
        Assert.assertThat("RequestUtilsTest nullCharsetTypeHeader contentType mimetype must be text/html",
                contentType.getMimeType(), is(DEFAULT_MIME_TYPE));
        Assert.assertThat("RequestUtilsTest nullCharsetTypeHeader authorizations must be null", authorization, nullValue());
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


    @Test
    public void getBadCookie(){

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE,Arrays.asList("Session",expectedSessionId));

        String sessionId = RequestUtils.getSessionId(headers);
        Assert.assertThat("RequestUtilsTest getBadCookie getSessionId",
                sessionId, is(nullValue()));
    }


    @Test
    public void getGoodCookie(){

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE,Arrays.asList("Session="+expectedSessionId));

        String sessionId = RequestUtils.getSessionId(headers);
        Assert.assertThat("RequestUtilsTest getGoodCookie getSessionId",
                sessionId, is(expectedSessionId));

    }

    @Test
    public void getBadCookieKey(){

        Headers headers = new Headers();
        String expectedSessionId = "14";
        headers.put(COOKIE,Arrays.asList("test="+expectedSessionId));

        String sessionId = RequestUtils.getSessionId(headers);
        Assert.assertThat("RequestUtilsTest getBadCookieKey getSessionId",
                sessionId, is(nullValue()));

    }


    @Test
    public void getPathvariableOneLevel(){

        String basePath = "/basepath";
        String entityId = "entityid";
        Pattern p = Pattern.compile(basePath + "/*(?<" + entityId + ">\\S*)");

        String id1 = "14";
        String path = basePath + "/" +id1 ;

        List<String> params = new ArrayList<>();
        params.add(entityId);

        Map<String,String> result = RequestUtils.parsePathParams(path,params,p);
        Assert.assertThat("RequestUtilsTest getPathvariableOneLevel key is entityId",
                result.keySet().iterator().next(), is(entityId));
        Assert.assertThat("RequestUtilsTest getPathvariableOneLevel value is ",
                result.values().iterator().next(), is(id1));
    }

    @Test
    public void getPathvariableTwoLevels(){

        String collectionOne = "/collectionOne";
        String entityId1 = "entityid";
        String entityId2 = "entityid2";
        String collectionTwo = "collectionTwo";
        Pattern p =   Pattern.compile(collectionOne + "/*(?<" + entityId1 + ">[^:\\/\\s]+)?\\/?(?<" + collectionTwo +
                ">"+collectionTwo+")?\\/?(?<" + entityId2 + ">[^:\\/\\s]+)?");

        String id1 = "14";
        String id2 = "15";
        String path = collectionOne + "/" +id1 + "/" + collectionTwo + "/" + id2;

        List<String> params = new ArrayList<>();
        params.add(entityId1);
        params.add(collectionTwo);
        params.add(entityId2);

        Map<String,String> result = RequestUtils.parsePathParams(path,params,p);

        Assert.assertThat("RequestUtilsTest getPathvariableTwoLevels value of entity1 is ",
                result.get(entityId1), is(id1));
        Assert.assertThat("RequestUtilsTest getPathvariableTwoLevels value of entity2 is  ",
                result.get(entityId2), is(id2));
    }


    @Test
    public void putAndPostSupportJson(){

        Assert.assertThat("RequestUtilsTest putAndPostSupportJson put is  supported  ",
                RequestUtils.validMediaType( BaseController.METHOD_PUT, ContentType.APPLICATION_JSON),
                is(Boolean.valueOf(true)));

        Assert.assertThat("RequestUtilsTest putAndPostSupportJson post is  supported  ",
                RequestUtils.validMediaType(BaseController.METHOD_POST, ContentType.APPLICATION_JSON),
                is(Boolean.valueOf(true)));

    }

    @Test
    public void notJsonMediaType(){

        Assert.assertThat("RequestUtilsTest notJsonMediaType put with not json  ",
                RequestUtils.validMediaType( BaseController.METHOD_PUT, ContentType.APPLICATION_XML),
                is(Boolean.valueOf(false)));

        Assert.assertThat("RequestUtilsTest notJsonMediaType put with not json ",
                RequestUtils.validMediaType(BaseController.METHOD_POST, ContentType.APPLICATION_XML),
                is(Boolean.valueOf(false)));

        Assert.assertThat("RequestUtilsTest notJsonMediaType delete  with not json   ",
                RequestUtils.validMediaType( BaseController.METHOD_DELETE, ContentType.APPLICATION_XML),
                is(Boolean.valueOf(true)));

        Assert.assertThat("RequestUtilsTest notJsonMediaType get  with not json  ",
                RequestUtils.validMediaType( BaseController.METHOD_GET, ContentType.APPLICATION_XML),
                is(Boolean.valueOf(true)));

    }


    @Test
    public void parseQueryParamsUrlEncondedBadInputStream() throws FileNotFoundException {

        String badString = "";
        InputStream is = new ByteArrayInputStream(badString.getBytes(StandardCharsets.UTF_8));
        Assert.assertThat("RequestUtilsTest parseQueryParamsUrlEncondedBadInputStream empty body ",
                RequestUtils.parseQueryParamsUrlEnconded(is,Charset.defaultCharset()).isEmpty(),
                is(Boolean.valueOf(true)));

    }

    @Test
    public void parseQueryParamsUrlEncondedGoodBody() throws FileNotFoundException {

        String goodBody = "id1=14&id2=20";

        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));
        Map params = RequestUtils.parseQueryParamsUrlEnconded(is,Charset.defaultCharset());
        Assert.assertThat("RequestUtilsTest parseQueryParamsUrlEncondedGoodBody id1y ",
                params.get("id1"),
                is("14"));
        Assert.assertThat("RequestUtilsTest parseQueryParamsUrlEncondedGoodBody id2 ",
                params.get("id2"),
                is("20"));

    }


    @Test
    public void parseFirstRequestBodyTest() throws IOException {

        String goodBody = "id1=14&id2=20";
        InputStream is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));

        Assert.assertThat("RequestUtilsTest parseFirstRequestBodyTest string params ",
                RequestUtils.parseFirstRequestBody(is,Charset.defaultCharset()), is(goodBody));
        Assert.assertThat("RequestUtilsTest parseFirstRequestBodyTest null inputstream",
                RequestUtils.parseFirstRequestBody(null,Charset.defaultCharset()), is(nullValue()));

        goodBody = "";
        is = new ByteArrayInputStream(goodBody.getBytes(StandardCharsets.UTF_8));
        Assert.assertThat("RequestUtilsTest parseFirstRequestBodyTest empty string",
                RequestUtils.parseFirstRequestBody(is,Charset.defaultCharset()), is(nullValue()));

    }




}
