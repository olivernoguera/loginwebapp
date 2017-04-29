package com.onoguera.loginwebapp.controller;

import com.onoguera.loginwebapp.entities.Session;
import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by olivernoguera on 29/04/2017.
 */
public abstract class HtmlController extends BaseController {

    protected final SessionServiceInterface sessionService;

    public HtmlController(SessionServiceInterface sessionService) {

        this.sessionService = sessionService;
    }

    @Override
    public Response getBadHeaders(String method, Headers headers, ContentType contentType, Request request) {
        return null;
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

        if(Objects.isNull(pathParams) ||  Objects.isNull(requestBody) || Objects.isNull(contentType) ||
                Objects.isNull(headers)){
            throw new IllegalArgumentException(
                    MessageFormat.format("Elements getRequest {0}, requestBody {1}, contentType {2} , " +
                                    "headers {3} must not be null ",
                    pathParams,requestBody, contentType, headers));
        }

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

    public SessionServiceInterface getSessionService() {
        return sessionService;
    }
}
