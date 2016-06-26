package com.onoguera.loginwebapp.view;

import java.io.IOException;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public final class LoginView extends HtmlView {

    private static LoginView view = null;

    private LoginView() throws IOException {
        super("html/login.html");
    }

    public static LoginView getView() throws IOException {
        if(view == null) {
            view = new LoginView();
        }
        return view;
    }

}
