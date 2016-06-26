package com.onoguera.loginwebapp.view;

import java.io.IOException;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public final class PageView extends HtmlView {

    private static PageView view =  null;

    private PageView() throws IOException {
        super("html/userpage.html");
    }

    public static PageView getView() throws IOException {
        if (view == null) {
            view = new PageView();
        }
        return view;
    }
}
