package com.onoguera.loginwebapp.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onoguera.loginwebapp.entities.User;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by olivernoguera on 20/04/2017.
 */
public class JsonResponseTest {

    private static final ObjectMapper mapper = new ObjectMapper();


    private static class MockBadEntityJackson{


    }

    @Test
    public void responseJsonUserTest() throws UnsupportedEncodingException, JsonProcessingException {

        User mockUser = new User("1","1234");
        int httpStatusMock = 400;
        JsonResponse responseMock = new JsonResponse(httpStatusMock,mockUser);


        Assert.assertThat("JsonResponseTest responseJsonUserTest getHttpStatus",
                responseMock.getHttpStatus(), is(httpStatusMock));
        Assert.assertThat("JsonResponseTest responseJsonUser setOutput",
                responseMock.getOutput(), is(mapper.writeValueAsString(mockUser)));
        Assert.assertThat("JsonResponseTest responseJsonUserTest getContentType",
                responseMock.getContentType(), is(ContentType.APPLICATION_JSON.toString()));
        Assert.assertThat("JsonResponseTest responseJsonUserTest getBytes", responseMock.getBytes(),
                is(mapper.writeValueAsString(mockUser).getBytes(StandardCharsets.UTF_8)));
    }


    @Test
    public void responseJsonUserBadJacksonTest() throws UnsupportedEncodingException {

        int expectedStatus = 500;
        int httpStatusMock = 400;
        String expetecedOutput = "Internal server error.";

        JsonResponse responseMock = new JsonResponse(httpStatusMock,new MockBadEntityJackson());

        Assert.assertThat("JsonResponseTest responseJsonUserBadJacksonTest getHttpStatus",
                responseMock.getHttpStatus(), is(expectedStatus));
        Assert.assertThat("JsonResponseTest responseJsonUserBadJacksonTest setOutput",
                responseMock.getOutput(), is(expetecedOutput));
    }



}
