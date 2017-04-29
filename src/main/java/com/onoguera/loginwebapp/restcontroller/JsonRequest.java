package com.onoguera.loginwebapp.restcontroller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onoguera.loginwebapp.request.Request;

import java.io.IOException;
import java.util.Map;

/**
 * Created by olivernoguera on 05/06/2016.
 *
 */
public class JsonRequest extends Request {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JsonRequest(final Map<String, String> queryParams, final Map<String, String> pathParams,
                       final String rawBody) {
        super(queryParams, pathParams, rawBody, null);
    }

    public Object getBodyObject(Class clazz) throws  IOException {
        return OBJECT_MAPPER.readValue(getRawBody(),clazz);
    }

    public  <T> T  getBodyObject(final TypeReference<T> type) throws  IOException {
       return fromJSON(type,super.getRawBody());
    }

    private static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) throws IOException {
        return OBJECT_MAPPER.readValue(jsonPacket, type);
    }
}
