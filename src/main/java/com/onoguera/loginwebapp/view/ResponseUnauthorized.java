package com.onoguera.loginwebapp.view;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public final class ResponseUnauthorized extends Response {

    public ResponseUnauthorized() {
        super(HttpURLConnection.HTTP_UNAUTHORIZED, "Unauthorized.");
    }
}
