package com.onoguera.loginwebapp.view;

import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by oliver on 3/06/16.
 *
 */
public abstract class Response {

    protected int httpStatus;
    protected String output;
    protected ContentType contentType = ContentType.APPLICATION_JSON;


    public Response(int httpStatus, String output) {
        this.httpStatus = httpStatus;
        this.output = output;

    }

    public Response(int httpStatus, ContentType contentType) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;

    }

    public byte[] getBytes() throws UnsupportedEncodingException {
        return output.getBytes(Charset.defaultCharset());
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getHttpStatus() {
        return httpStatus;
    }



    public String getContentType() {
        return contentType.toString();
    }

    public void setHeadersResponse(Headers headers){}

}
