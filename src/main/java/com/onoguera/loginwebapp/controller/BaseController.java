package com.onoguera.loginwebapp.controller;


import com.onoguera.loginwebapp.request.Request;
import com.onoguera.loginwebapp.request.RequestUtils;
import com.onoguera.loginwebapp.response.Response;
import com.onoguera.loginwebapp.response.ResponseBadRequest;
import com.onoguera.loginwebapp.response.ResponseMethodNotAllowed;
import com.onoguera.loginwebapp.response.ResponseNotImplemented;
import com.onoguera.loginwebapp.response.ResponseUnsupportedMediaType;
import com.onoguera.loginwebapp.service.SessionServiceInterface;
import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * Created by oliver on 1/06/16.
 *
 */
public abstract class BaseController implements Controller {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected SessionServiceInterface sessionService = null;

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";


    public abstract Pattern getURLPattern();

    public abstract List<String> getPathParams();

    protected Response doGet(final Request request)  {
        return new ResponseNotImplemented();
    }

    protected Response doPost(final Request request)  {
        return new ResponseNotImplemented();
    }

    protected Response doPut(final Request request) {
        return new ResponseNotImplemented();
    }

    protected Response doDelete(final Request request) {
        return new ResponseNotImplemented();
    }

    protected abstract boolean checkMethodAllowed(final String method) ;

    protected abstract Response getBadHeaders(String method, Headers headers, ContentType contentType, Request request);

    protected abstract Request getRequest(Map<String, String> pathParams, String path, InputStream requestBody,
                                          ContentType contentType, Headers headers)  throws IOException;

    public boolean filter(String contextPath) {
        if (contextPath == null) {
            return false;
        }
        return getURLPattern().matcher(contextPath).matches();
    }

    @Override
    public Response dispatch(final URI requestURI,
                             final InputStream requestBody,
                             final String method,
                             final Headers headers) {

        if (!checkMethodAllowed(method)) {
            LOGGER.warn(String.format("Method %s not allowed", method));
            return new ResponseMethodNotAllowed();
        }

        ContentType contentType;

        try {
            contentType = RequestUtils.getContentType(headers);
        }  catch (IllegalArgumentException i) {
            LOGGER.warn("Bad request.", i);
            return new ResponseUnsupportedMediaType();
        }

        Request request;
        try {
            request = this.getRequest(requestURI.getPath(), requestBody,contentType,headers);
        } catch (IOException io) {
            LOGGER.warn("Bad request.", io);
            return new ResponseBadRequest();
        }
        catch (IllegalArgumentException ilegal) {
            LOGGER.warn("Bad content type", ilegal);
            return new ResponseUnsupportedMediaType();
        }

        Response badAuth = getBadHeaders(method, headers, contentType, request);
        if (badAuth != null){
            return badAuth;
        }

        return dispatch(request, method);
    }

    private Request getRequest(final String path,
                               InputStream requestBody,
                               ContentType contentType,
                               Headers headers) throws IOException {

        Map<String, String> pathParams =
                RequestUtils.parsePathParams(path, this.getPathParams(), this.getURLPattern());
        return this.getRequest(pathParams, path, requestBody,contentType,headers);

    }

    private Response dispatch(final Request request, final String method) {
        if (METHOD_GET.equals(method)) {
            return doGet(request);
        } else if (METHOD_POST.equals(method)) {
            return doPost(request);
        } else if (METHOD_PUT.equals(method)) {
            return doPut(request);
        } else {
            //only delete
            return doDelete(request);
        }
    }


    public void setSessionService(SessionServiceInterface sessionService) {
        this.sessionService = sessionService;
    }
}
