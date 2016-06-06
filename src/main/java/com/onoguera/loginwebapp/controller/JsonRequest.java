package com.onoguera.loginwebapp.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by olivernoguera on 05/06/2016.
 */
public class JsonRequest extends Request {

    private static final Gson GSON = new GsonBuilder().create();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JsonRequest(final Map<String, String> queryParams, final Map<String, String> pathParams,
                       final String rawBody) {
        super(queryParams, pathParams, rawBody);
    }

    public Object getBodyObject(Class clazz) throws  IOException {
        return OBJECT_MAPPER.readValue(getRawBody(),clazz);
    }

    public  <T> T  getBodyObject(final TypeReference<T> type) throws  IOException {
       return fromJSON(type,super.getRawBody());
    }

    private static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) throws IOException {
        T data = null;
        data = new ObjectMapper().readValue(jsonPacket, type);
        return data;
    }
}
