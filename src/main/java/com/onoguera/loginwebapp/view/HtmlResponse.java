package com.onoguera.loginwebapp.view;

import com.onoguera.loginwebapp.entities.Session;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
/**
 * Created by oliver on 3/06/16.
 *
 */
public abstract class  HtmlResponse extends Response {

    private final static ContentType CONTENT_TYPE = ContentType.TEXT_HTML;
    protected String session;
    protected String location;

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlResponse.class);

    public HtmlResponse(int httpStatus,
                        Map<String, String> values,
                        HtmlView htmlView,
                        String sessionId,
                        String location) throws IOException {

        super(httpStatus, CONTENT_TYPE);
        super.setOutput(htmlView.getOutput(values));
        this.location = location;
        this.session = String.format("%s=%s", Session.class.getSimpleName(),sessionId);

    }

    public HtmlResponse(int httpStatus, Map<String, String> values, HtmlView htmlView,String location)
            throws IOException {

        super(httpStatus, CONTENT_TYPE);
        super.setOutput(htmlView.getOutput(values));
        this.location = location;
        this.session = String.format("%s=0", Session.class.getSimpleName());
    }

    public void setHeadersResponse(Headers headers){

        headers.add("Set-Cookie", session);
        headers.add("Location", location);
    }

    public String getSession() {
        return session;
    }

    public String getLocation() {
        return location;
    }
}
