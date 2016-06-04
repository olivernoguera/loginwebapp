package com.onoguera.loginwebapp.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by oliver on 3/06/16.
 */
public class JsonResponse extends Response
{
    protected final static String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final Gson GSON  = new GsonBuilder().create();


    public JsonResponse(int httpStatus, Object object) {
        super(httpStatus);
        super.setOutput(GSON.toJson(object));
        super.setContentType(CONTENT_TYPE);
    }



}
