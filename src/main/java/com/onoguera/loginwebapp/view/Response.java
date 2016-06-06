package com.onoguera.loginwebapp.view;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by oliver on 3/06/16.
 */
public abstract class Response {

    protected int httpStatus;
    protected String output;
    protected String contentType = "text/plain; charset=UTF-8";

    public Response(int httpStatus, String output) {

        this.httpStatus = httpStatus;
        this.output = output;
    }

    public Response(int httpStatus) {
        this.httpStatus = httpStatus;
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
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
