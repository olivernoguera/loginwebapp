package com.onoguera.loginwebapp.view;

import java.net.HttpURLConnection;

/**
 * Created by olivernoguera on 05/06/2016.
 */
public class ResponseEmpty extends Response {

    public ResponseEmpty()
    {
        super(HttpURLConnection.HTTP_OK, "");
    }
}
