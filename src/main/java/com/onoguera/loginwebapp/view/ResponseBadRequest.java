package com.onoguera.loginwebapp.view;

import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public class ResponseBadRequest extends Response {

    public ResponseBadRequest()
    {
        super(HttpURLConnection.HTTP_BAD_REQUEST, "Bad Request.");
    }
}