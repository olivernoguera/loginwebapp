package com.onoguera.loginwebapp.view;

import com.onoguera.loginwebapp.response.HtmlResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public class LoginResponse extends HtmlResponse {


    public LoginResponse(int httpStatus, Map<String, String> values,String location) throws IOException {
        super(httpStatus, values, LoginView.getView(),location);
    }

    public LoginResponse(int httpStatus, Map<String, String> values,String sessionId,String location) throws IOException {
        super(httpStatus, values, LoginView.getView(),sessionId,location);
    }
}
