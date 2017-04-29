package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olivernoguera on 29/04/2017.
 */
public abstract class HtmlController extends BaseController {

    @Override
    public Response getBadHeaders(String method, Headers headers, ContentType contentType, Request request) {
        return null;
    }


    @Override
    public Response doPut(Request request) {
        return new ResponseNotImplemented();
    }

    @Override
    public Response doDelete(Request request) {
        return new ResponseNotImplemented();
    }

    protected boolean checkMethodAllowed(final String method) {
        if (METHOD_GET.equals(method)) {
            return true;
        } else if (METHOD_POST.equals(method)) {
            return true;
        } else if (METHOD_PUT.equals(method)) {
            return false;
        } else if (METHOD_DELETE.equals(method)) {
            return false;
        }
        return false;
    }


    protected Request getRequest(Map<String, String> pathParams, final String path,
                               InputStream requestBody,
                               ContentType contentType,
                               Headers headers) throws IOException {

        Map<String, String> queryParams = new HashMap<>();
        if( contentType.getMimeType().equals(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())){
            queryParams =  RequestUtils.parseQueryParamsUrlEnconded(requestBody, contentType.getCharset());
        }

        String sessionId = RequestUtils.getSessionId(headers);
        Session session = null;
        if( sessionId != null){
            session = sessionService.getSession(sessionId);
        }
        return new Request(queryParams, pathParams,"",session);

    }

}
