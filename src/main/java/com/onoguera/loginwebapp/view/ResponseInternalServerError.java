package com.onoguera.loginwebapp.view;

import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public final class ResponseInternalServerError extends Response {

    public ResponseInternalServerError() {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR, "Internal server error.");
    }

}
