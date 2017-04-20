package com.onoguera.loginwebapp.response;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.view.HtmlView;
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
 * Created by olivernoguera on 20/04/2017.
 */
public class HtmlResponseTest {


    private final static class MockHtmlResponse extends HtmlResponse{


        public MockHtmlResponse(int httpStatus, Map<String, String> values, HtmlView htmlView,
                                String sessionId, String location) throws IOException {
            super(httpStatus, values, htmlView, sessionId, location);
        }

        public MockHtmlResponse(int httpStatus, Map<String, String> values, HtmlView htmlView,
                                String location) throws IOException {
            super(httpStatus, values, htmlView, location);
        }
    }

    private final static class MockHtmlView extends HtmlView{

        public MockHtmlView(String templateName) throws IOException {
            super(templateName);
        }
    }

    @Test(expected = IOException.class)
    public void badHtmlViewTest() throws IOException {
        MockHtmlView mockHtmlView = new MockHtmlView("badview.html");
    }




    @Test
    public void htmlViewWithSession() throws IOException {

        MockHtmlView mockHtmlView = new MockHtmlView("test.html");
        int httpStatusMock = 200;
        String  mockSession = "sesion1";
        String mockLocation =  "location1";
        String expectedSession =  Session.class.getSimpleName()+"="+mockSession;
        String expectedOutput = "<html></html>\n";
        MockHtmlResponse responseMock =
                new MockHtmlResponse(httpStatusMock,new HashMap<>(),mockHtmlView, mockSession, mockLocation );


        Assert.assertThat("HtmlResponseTest htmlViewWithSession getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession setOutput",
                responseMock.getOutput(), is(expectedOutput));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession getLocation",
                responseMock.getLocation(), is(mockLocation));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession getSession",
                responseMock.getSession(), is(expectedSession));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession getContentType",
                responseMock.getContentType(), is(ContentType.TEXT_HTML.toString()));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession getBytes", responseMock.getBytes(),
                is(expectedOutput.getBytes(StandardCharsets.UTF_8)));

        Headers headersMock = new Headers();
        responseMock.setHeadersResponse(headersMock);

        Assert.assertThat("HtmlResponseTest htmlViewWithSession setCookie", headersMock.get("Set-Cookie"),
                is(Arrays.asList(expectedSession)));
        Assert.assertThat("HtmlResponseTest htmlViewWithSession Location", headersMock.get("Location"),
                is(Arrays.asList(mockLocation)));

    }

    @Test
    public void htmlViewWithoutSession() throws IOException {

        MockHtmlView mockHtmlView = new MockHtmlView("test.html");
        int httpStatusMock = 200;
        String mockLocation =  "location1";
        String  mockSession = "0";
        String expectedSession =  Session.class.getSimpleName()+"="+mockSession;
        String expectedOutput = "<html></html>\n";
        MockHtmlResponse responseMock =
                new MockHtmlResponse(httpStatusMock,new HashMap<>(),mockHtmlView,  mockLocation );


        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession setOutput",
                responseMock.getOutput(), is(expectedOutput));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession getLocation",
                responseMock.getLocation(), is(mockLocation));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession getSession",
                responseMock.getSession(), is(expectedSession));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession getContentType",
                responseMock.getContentType(), is(ContentType.TEXT_HTML.toString()));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession getBytes", responseMock.getBytes(),
                is(expectedOutput.getBytes(StandardCharsets.UTF_8)));

        Headers headersMock = new Headers();
        responseMock.setHeadersResponse(headersMock);

        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession setCookie", headersMock.get("Set-Cookie"),
                is(Arrays.asList(expectedSession)));
        Assert.assertThat("HtmlResponseTest htmlViewWithoutSession Location", headersMock.get("Location"),
                is(Arrays.asList(mockLocation)));

    }




}
