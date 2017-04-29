package com.onoguera.loginwebapp.request;

import com.onoguera.loginwebapp.entities.Session;

import java.util.Map;

/**
 * Created by oliver on 4/06/16.
 *
 */
public class Request {

    private final Map<String, String> queryParams;
    private final Map<String, String> pathParams;
    private final String rawBody;
    private Session session;

    public Request(Map<String, String> queryParams, Map<String, String> pathParams, String rawBody,Session session) {
        this.queryParams = queryParams;
        this.pathParams = pathParams;
        this.rawBody = rawBody;
        this.session = session;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public Map<String, String> getPathParams() {
        return pathParams;
    }

    public String getRawBody() {
        return rawBody;
    }

    public Session getSession() {
        return session;
    }
}
