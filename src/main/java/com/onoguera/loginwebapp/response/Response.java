package com.onoguera.loginwebapp.response;

import com.sun.net.httpserver.Headers;
import org.apache.http.entity.ContentType;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by oliver on 3/06/16.
 *
 */
public abstract class Response {

    protected int httpStatus;
    protected String output;
    protected ContentType contentType;
    private Charset defaultCharset = StandardCharsets.UTF_8;


    public Response(int httpStatus, String output) {
        this.httpStatus = httpStatus;
        this.output = output;
        this.contentType = ContentType.APPLICATION_JSON;
    }


    public Response(int httpStatus, ContentType contentType) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;

    }

    public byte[] getBytes() throws UnsupportedEncodingException {
        return output.getBytes( defaultCharset);
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
