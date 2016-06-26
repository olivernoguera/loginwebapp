package com.onoguera.loginwebapp.view;

import java.net.HttpURLConnection;

/**
 * Created by olivernoguera on 26/06/2016.
 */
public class ResponseMovedTemporaly extends Response  {
    public ResponseMovedTemporaly() {
        super(HttpURLConnection.HTTP_MOVED_TEMP, "Redictect.");
    }
}
