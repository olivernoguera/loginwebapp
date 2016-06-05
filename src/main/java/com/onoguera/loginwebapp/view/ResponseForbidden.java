package com.onoguera.loginwebapp.view;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public final class ResponseForbidden extends Response {

    public ResponseForbidden() {
        super(HttpURLConnection.HTTP_FORBIDDEN, "Not have permissions to this method.");
    }
}
