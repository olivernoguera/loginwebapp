package com.onoguera.loginwebapp.response;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 *
 */
public final class ResponseNotImplemented extends Response {

    public ResponseNotImplemented() {
        super(HttpURLConnection.HTTP_NOT_IMPLEMENTED, "Not implemented.");
    }
}
