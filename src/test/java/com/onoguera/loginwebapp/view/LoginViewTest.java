package com.onoguera.loginwebapp.view;

import com.onoguera.loginwebapp.entities.Session;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 23/04/2017.
 */
public class LoginViewTest {


    @Test
    public void loginViewWithoutSession() throws IOException {


        int httpStatusMock = 200;
        String mockLocation =  "location1";
        Map<String,String> params = new HashMap<>();
        String  mockSession = "0";
        String expectedSession =  Session.class.getSimpleName()+"="+mockSession;
        String expectedOutput = "<html>\n" +
                "\n" +
                "    <body>\n" +
                "\n" +
                "        <h1></h1>\n" +
                "        <h2></h2>\n" +
                "\n" +
                "        <form action=\"/login\" method=\"post\">\n" +
                "\n" +
                "            <label for=\"username\">Username</label>\n" +
                "            <input type=\"text\" name=\"username\">\n" +
                "\n" +
                "            <label for=\"password\">Password</label>\n" +
                "            <input type=\"password\" name=\"password\">\n" +
                "\n" +
                "            <input type=\"submit\" value=\"Login\">\n" +
                "\n" +
                "        </form>\n" +
                "\n" +
                "    </body>\n" +
                "\n" +
                "</html>\n";
        LoginResponse loginResponse = new LoginResponse(httpStatusMock,params,mockLocation);

        Assert.assertThat("LoginViewTest loginViewWithoutSession getHttpStatus",
                loginResponse.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("LoginViewTest loginViewWithoutSession setOutput",
                loginResponse.getOutput(), is(expectedOutput));
        Assert.assertThat("LoginViewTest loginViewWithoutSession getLocation",
                loginResponse.getLocation(), is(mockLocation));
        Assert.assertThat("LoginViewTest loginViewWithoutSession getSession",
                loginResponse.getSession(), is(expectedSession));
        Assert.assertThat("LoginViewTest loginViewWithoutSession getContentType",
                loginResponse.getContentType(), is(ContentType.TEXT_HTML.toString()));
        Assert.assertThat("LoginViewTest loginViewWithoutSession getBytes", loginResponse.getBytes(),
                is(expectedOutput.getBytes(StandardCharsets.UTF_8)));

        Headers headersMock = new Headers();
        loginResponse.setHeadersResponse(headersMock);

        Assert.assertThat("LoginViewTest loginViewWithoutSession setCookie", headersMock.get("Set-Cookie"),
                is(Arrays.asList(expectedSession)));
        Assert.assertThat("LoginViewTest loginViewWithoutSession Location", headersMock.get("Location"),
                is(Arrays.asList(mockLocation)));

    }

    @Test
    public void loginViewWithSession() throws IOException {

        int httpStatusMock = 200;
        Map<String,String> params = new HashMap<>();
        String  mockSession = "sesion1";
        String mockLocation =  "location1";
        String expectedSession =  Session.class.getSimpleName()+"="+mockSession;
        String expectedOutput = "<html>\n" +
                "\n" +
                "    <body>\n" +
                "\n" +
                "        <h1></h1>\n" +
                "        <h2></h2>\n" +
                "\n" +
                "        <form action=\"/login\" method=\"post\">\n" +
                "\n" +
                "            <label for=\"username\">Username</label>\n" +
                "            <input type=\"text\" name=\"username\">\n" +
                "\n" +
                "            <label for=\"password\">Password</label>\n" +
                "            <input type=\"password\" name=\"password\">\n" +
                "\n" +
                "            <input type=\"submit\" value=\"Login\">\n" +
                "\n" +
                "        </form>\n" +
                "\n" +
                "    </body>\n" +
                "\n" +
                "</html>\n";
        LoginResponse loginResponse = new LoginResponse(httpStatusMock,params,mockSession,mockLocation);


        Assert.assertThat("LoginViewTest loginViewWithSession getHttpStatus",
                loginResponse.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("LoginViewTest loginViewWithSession setOutput",
                loginResponse.getOutput(), is(expectedOutput));
        Assert.assertThat("LoginViewTest loginViewWithSession getLocation",
                loginResponse.getLocation(), is(mockLocation));
        Assert.assertThat("LoginViewTest loginViewWithSession getSession",
                loginResponse.getSession(), is(expectedSession));
        Assert.assertThat("LoginViewTest loginViewWithSession getContentType",
                loginResponse.getContentType(), is(ContentType.TEXT_HTML.toString()));
        Assert.assertThat("LoginViewTest loginViewWithSession getBytes", loginResponse.getBytes(),
                is(expectedOutput.getBytes(StandardCharsets.UTF_8)));

        Headers headersMock = new Headers();
        loginResponse.setHeadersResponse(headersMock);

        Assert.assertThat("LoginViewTest loginViewWithSession setCookie", headersMock.get("Set-Cookie"),
                is(Arrays.asList(expectedSession)));
        Assert.assertThat("LoginViewTest loginViewWithSession Location", headersMock.get("Location"),
                is(Arrays.asList(mockLocation)));

    }


}
