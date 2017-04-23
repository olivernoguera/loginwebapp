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
public class PageViewTest {



    @Test
    public void pageViewWithoutSession() throws IOException {


        int httpStatusMock = 200;
        Map<String,String> params = new HashMap<>();
        String  mockSession = "sesion1";
        String mockLocation =  "location1";
        String expectedSession =  Session.class.getSimpleName()+"="+mockSession;
        String expectedOutput = "<html>\n" +
                "    <body>\n" +
                "        <h1>Page </h1>\n" +
                "\n" +
                "        <h2>Hello </b></h2>\n" +
                "\n" +
                "        <form action=\"/logout\" method=\"post\">\n" +
                "            <input type=\"submit\" value=\"Logout\">\n" +
                "        </form>\n" +
                "    </body>\n" +
                "</html>\n";

        PageResponse pageResponse = new PageResponse(httpStatusMock,params,mockSession,mockLocation);

        Assert.assertThat("PageViewTest pageViewWithoutSession getHttpStatus",
                pageResponse.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("PageViewTest pageViewWithoutSession setOutput",
                pageResponse.getOutput(), is(expectedOutput));
        Assert.assertThat("PageViewTest pageViewWithoutSession getLocation",
                pageResponse.getLocation(), is(mockLocation));
        Assert.assertThat("PageViewTest pageViewWithoutSession getSession",
                pageResponse.getSession(), is(expectedSession));
        Assert.assertThat("PageViewTest pageViewWithoutSession getContentType",
                pageResponse.getContentType(), is(ContentType.TEXT_HTML.toString()));
        Assert.assertThat("PageViewTest pageViewWithoutSession getBytes", pageResponse.getBytes(),
                is(expectedOutput.getBytes(StandardCharsets.UTF_8)));

        Headers headersMock = new Headers();
        pageResponse.setHeadersResponse(headersMock);

        Assert.assertThat("PageViewTest pageViewWithoutSession setCookie", headersMock.get("Set-Cookie"),
                is(Arrays.asList(expectedSession)));
        Assert.assertThat("PageViewTest pageViewWithoutSession Location", headersMock.get("Location"),
                is(Arrays.asList(mockLocation)));

    }
}
