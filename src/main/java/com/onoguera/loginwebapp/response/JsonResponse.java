package com.onoguera.loginwebapp.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;

/**
 * Created by oliver on 3/06/16.
 *
 */
public final class JsonResponse extends Response {

    protected final static String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonResponse.class);

    public JsonResponse(int httpStatus, Object object)  {
        super(httpStatus,CONTENT_TYPE);
        try {
            super.setOutput(mapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            super.setOutput("Internal server error.");
            super.setHttpStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
            LOGGER.error("Not serialize. ", e);
        }
    }


}
