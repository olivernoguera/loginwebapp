package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.view.Response;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by oliver on 1/06/16.
 */
public interface Controller {

    boolean filter(String contextPath);

    Response dispatch(final URI requestURI, InputStream requestBody, final String requestMethod);
}
