package com.onoguera.loginwebapp.view;

import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 *
 */
public final class ResponseMethodNotAllowed extends Response {

    public ResponseMethodNotAllowed() {
        super(HttpURLConnection.HTTP_BAD_METHOD, "Bad method.");
    }
}
