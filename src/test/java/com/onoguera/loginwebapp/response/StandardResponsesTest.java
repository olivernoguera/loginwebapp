package com.onoguera.loginwebapp.response;

import org.junit.Assert;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 21/04/2017.
 */
public class StandardResponsesTest {

    @Test
    public void responseBadRequestTest()  {

        String output = "Bad Request.";
        String comment = "this is a test";

        ResponseBadRequest responseBadRequest = new ResponseBadRequest();

        Assert.assertThat("StandardResponsesTest responseBadRequestTest getHttpStatus",
                responseBadRequest.getHttpStatus(), is(HttpURLConnection.HTTP_BAD_REQUEST));

        Assert.assertThat("StandardResponsesTest responseBadRequestTest setOutput",
                responseBadRequest.getOutput(), is(output));

        responseBadRequest = new ResponseBadRequest(comment);

        Assert.assertThat("StandardResponsesTest responseBadRequestTest getHttpStatus",
                responseBadRequest.getHttpStatus(), is(HttpURLConnection.HTTP_BAD_REQUEST));

        Assert.assertThat("ResponseTest responseBadRequestTest setOutput with comment",
                responseBadRequest.getOutput(), is(output+comment));

    }

    @Test
    public void responseForbiddenTest()  {

        String output = "Forbidden";
        ResponseForbidden responseForbidden = new ResponseForbidden();

        Assert.assertThat("StandardResponsesTest responseForbiddenTest getHttpStatus",
                responseForbidden.getHttpStatus(), is(HttpURLConnection.HTTP_FORBIDDEN));

        Assert.assertThat("StandardResponsesTest responseForbiddenTest setOutput",
                responseForbidden.getOutput(), is(output));
    }

    @Test
    public void responseInternalServerErrorTest()  {

        String output = "Internal server error.";
        ResponseInternalServerError responseInternalServerError = new ResponseInternalServerError();

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest getHttpStatus",
                responseInternalServerError.getHttpStatus(), is(HttpURLConnection.HTTP_INTERNAL_ERROR));

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest setOutput",
                responseInternalServerError.getOutput(), is(output));
    }

    @Test
    public void responseEmptyTest()  {

        String output = "";
        ResponseEmpty responseEmpty = new ResponseEmpty();

        Assert.assertThat("StandardResponsesTest responseBadRequestTest getHttpStatus",
                responseEmpty.getHttpStatus(), is(HttpURLConnection.HTTP_OK));

        Assert.assertThat("StandardResponsesTest responseBadRequestTest setOutput",
                responseEmpty.getOutput(), is(output));

    }

    @Test
    public void responseMethodNotAllowedTest()  {

        String output = "Bad method.";
        ResponseMethodNotAllowed responseMethodNotAllowed = new ResponseMethodNotAllowed();

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest getHttpStatus",
                responseMethodNotAllowed.getHttpStatus(), is(HttpURLConnection.HTTP_BAD_METHOD));

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest setOutput",
                responseMethodNotAllowed.getOutput(), is(output));
    }

    @Test
    public void responseNotFoundTest()  {

        String output = "Not Found.";
        ResponseNotFound responseNotFound = new ResponseNotFound();

        Assert.assertThat("StandardResponsesTest responseNotFoundTest getHttpStatus",
                responseNotFound.getHttpStatus(), is(HttpURLConnection.HTTP_NOT_FOUND));

        Assert.assertThat("StandardResponsesTest responseNotFoundTest setOutput",
                responseNotFound.getOutput(), is(output));
    }

    @Test
    public void responseNotImplementedTest()  {

        String output = "Not implemented.";
        ResponseNotImplemented responseNotImplemented = new ResponseNotImplemented();

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest getHttpStatus",
                responseNotImplemented.getHttpStatus(), is(HttpURLConnection.HTTP_NOT_IMPLEMENTED));

        Assert.assertThat("StandardResponsesTest responseInternalServerErrorTest setOutput",
                responseNotImplemented.getOutput(), is(output));
    }

    @Test
    public void responseUnauthorizedTest()  {

        String output = "Unauthorized.";
        ResponseUnauthorized responseUnauthorized = new ResponseUnauthorized();

        Assert.assertThat("StandardResponsesTest responseUnauthorizedTest getHttpStatus",
                responseUnauthorized.getHttpStatus(), is(HttpURLConnection.HTTP_UNAUTHORIZED));

        Assert.assertThat("StandardResponsesTest responseUnauthorizedTest setOutput",
                responseUnauthorized.getOutput(), is(output));
    }

    @Test
    public void responseUnsupportedMediaTypeTest()  {

        String output = "Unsupported mediatype.";
        ResponseUnsupportedMediaType responseUnsupportedMediaType = new ResponseUnsupportedMediaType();

        Assert.assertThat("StandardResponsesTest responseUnsupportedMediaTypeTest getHttpStatus",
                responseUnsupportedMediaType.getHttpStatus(), is(HttpURLConnection.HTTP_UNSUPPORTED_TYPE));

        Assert.assertThat("StandardResponsesTest responseUnsupportedMediaTypeTest setOutput",
                responseUnsupportedMediaType.getOutput(), is(output));
    }





}
