package com.onoguera.loginwebapp.view;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public final class ResponseUnsupportedMediaType extends Response {

    public ResponseUnsupportedMediaType()
    {
        super(HttpURLConnection.HTTP_UNSUPPORTED_TYPE, "Unsupported mediatype.");
    }
}
