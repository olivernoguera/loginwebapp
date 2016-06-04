package com.onoguera.loginwebapp.view;


import java.net.HttpURLConnection;

/**
 * Created by oliver on 4/06/16.
 */
public class ResponseNotFound extends Response {

    public ResponseNotFound()
    {
        super(HttpURLConnection.HTTP_NOT_FOUND, "Not Found.");
    }
}
