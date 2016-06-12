package com.onoguera.loginwebapp.controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by oliver on 7/06/16.
 */
public class RequestUtilsTest {


    @Test
    public void testNullHeaders()  {
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(null);
        Assert.assertThat("testNullHeaders:authorizations must be 0 Users", authorizations.size(), is(0));

    }

    @Test
    public void testNullHeader()  {
        String header = null;
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testNullHeader:authorizations must be 0 Users", authorizations.size(), is(0));
    }

    @Test
    public void testNotAuthHeader()  {
        String header = "basic";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testNotAuthHeader:authorizations must be 0 Users", authorizations.size(), is(0));

    }

    @Test
    public void testEmptyAuthHeader()  {
        String header = "Basic";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testEmptyAuthHeader:authorizations must be 0 Users", authorizations.size(), is(0));

    }

    @Test
    public void testNumericAuthHeader()  {
        String header = "Basic11111";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testNumericAuthHeader:authorizations must be 0 Users", authorizations.size(), is(0));

    }

    @Test
    public void testOnlyUserAuthHeader()  {
        //"User" like decode credential
        String header = "BasicVXNlcg==";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testOnlyUserAuthHeader:authorizations must be 0 Users", authorizations.size(), is(0));

    }

    @Test
    public void testPasswordWithDoublePointAuthCredentialsHeader()  {
        //"User" as user and "Pass:word"  as password decode credential
        String header = "Basic VXNlcjpQYXNzOndvcmQ=";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:authorizations must be 1 Users", authorizations.size(), is(1));
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:user must be User", authorizations.get(0)[0], is("User"));
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:password must be Pass:word", authorizations.get(0)[1], is("Pass:word"));
    }

    @Test
    public void testNormalAuthCredentialsHeader()  {
        //"User" as user and "Pass:word"  as password decode credential
        String header = "Basic VXNlcjpQYXNzd29yZA==";
        List<String> headers = Arrays.asList(header);
        List<String[]> authorizations = RequestUtils.getAuthorizationFromHeader(headers);
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:authorizations must be 1 Users", authorizations.size(), is(1));
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:user must be User", authorizations.get(0)[0], is("User"));
        Assert.assertThat("testPasswordWithDoublePointAuthCredentialsHeader:password must be Password", authorizations.get(0)[1], is("Password"));

    }




}
