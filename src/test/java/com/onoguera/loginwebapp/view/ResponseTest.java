package com.onoguera.loginwebapp.view;

import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * Created by olivernoguera on 20/04/2017.
 */
public class ResponseTest {

    private final static class ResponseMock extends Response {

        public ResponseMock(int httpStatus, String output) {
            super(httpStatus,output);
        }

        public ResponseMock(int httpStatus, ContentType contentType) {
            super(httpStatus,contentType);
        }

    }

    @Test
    public void responseWithOutput() throws UnsupportedEncodingException {

        int httpStatusMock = 400;
        String output = "testoutput";

        ResponseMock responseMock = new ResponseMock(httpStatusMock,output);

        Assert.assertThat("ResponseTest responseWithOutput getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("ResponseTest responseWithOutput getOutput",
                responseMock.getOutput(), is(output));
        Assert.assertThat("ResponseTest responseWithOutput getContentType",
                responseMock.getContentType(), is(ContentType.APPLICATION_JSON.toString()));
        Assert.assertThat("ResponseTest responseWithOutput getBytes", responseMock.getBytes(),
                is(output.getBytes( StandardCharsets.UTF_8)));
    }


    @Test(expected = NullPointerException.class)
    public void responseWithoutOutputWithoutValue() throws UnsupportedEncodingException {

        int httpStatusMock = 400;

        ResponseMock responseMock = new ResponseMock(httpStatusMock,ContentType.APPLICATION_XML);

        Assert.assertThat("ResponseTest responseWithoutOutputWithoutValue getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("ResponseTest responseWithoutOutputWithoutValue getOutput",
                responseMock.getOutput(), is(nullValue()));
        Assert.assertThat("ResponseTest responseWithoutOutputWithoutValue getContentType",
                responseMock.getContentType(), is(ContentType.APPLICATION_XML.toString()));
        responseMock.getBytes();
    }

    @Test
    public void responseWithoutOutputWithValue() throws UnsupportedEncodingException {

        int httpStatusMock = 400;
        String output = "testoutput";
        ResponseMock responseMock = new ResponseMock(httpStatusMock,ContentType.APPLICATION_XML);
        responseMock.setOutput(output);

        Assert.assertThat("ResponseTest responseWithoutOutputWithValue getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));

        responseMock.setHttpStatus(500);

        Assert.assertThat("ResponseTest responseWithoutOutputWithValue setHttpStatus",
                responseMock.getHttpStatus(), is(500));

        Assert.assertThat("ResponseTest responseWithoutOutputWithValue getOutput",
                responseMock.getOutput(), is(output));
        Assert.assertThat("ResponseTest responseWithoutOutputWithValue getContentType",
                responseMock.getContentType(), is(ContentType.APPLICATION_XML.toString()));
        Assert.assertThat("ResponseTest responseWithoutOutputWithValue getBytes", responseMock.getBytes(),
                is(output.getBytes( StandardCharsets.UTF_8)));
    }

}
