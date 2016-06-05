package com.onoguera.loginwebapp.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

/**
 * Created by olivernoguera on 05/06/2016.
 */
public class JsonRequest extends Request {

    private static final Gson GSON  = new GsonBuilder().create();

    public JsonRequest(final Map<String, String> queryParams, final Map<String, String> pathParams,final String rawBody) {
        super(queryParams, pathParams, rawBody);
    }


    public Object getBodyObject(Class clazz) {
        return GSON.fromJson(this.getRawBody(), clazz);
    }

}
