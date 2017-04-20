package com.onoguera.loginwebapp.response;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 *
 */
public final class ResponseForbidden extends Response {

    public ResponseForbidden() {
        super(HttpURLConnection.HTTP_FORBIDDEN, "Forbidden");
    }
}
