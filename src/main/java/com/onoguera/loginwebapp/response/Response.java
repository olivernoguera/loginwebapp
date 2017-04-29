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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Response response = (Response) o;

        if (httpStatus != response.httpStatus) return false;
        if (output != null ? !output.equals(response.output) : response.output != null) return false;
        if (contentType != null ? !contentType.equals(response.contentType) : response.contentType != null)
            return false;
        return defaultCharset != null ? defaultCharset.equals(response.defaultCharset) : response.defaultCharset == null;

    }

    @Override
    public int hashCode() {
        int result = httpStatus;
        result = 31 * result + (output != null ? output.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        result = 31 * result + (defaultCharset != null ? defaultCharset.hashCode() : 0);
        return result;
    }
}
