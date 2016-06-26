package com.onoguera.loginwebapp.view;

import java.io.IOException;
import java.util.Map;

/**
 * Created by olivernoguera on 25/06/2016.
 */
public class PageResponse extends  HtmlResponse {

    public PageResponse(int httpStatus, Map<String, String> values, String sessionId,String location) throws IOException {
        super(httpStatus, values, PageView.getView(),sessionId,location);
    }

}
