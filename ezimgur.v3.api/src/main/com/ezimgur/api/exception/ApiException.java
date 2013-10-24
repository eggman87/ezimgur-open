package com.ezimgur.api.exception;

import com.ezimgur.api.http.ResponseError;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 10:13 PM
 */
public class ApiException extends Exception {

    private ResponseError error;
    private static final String RESPONSE_ERROR_FORMAT = "Error: %s | Method: %s | Request: %s";
    private int statusCode;

    public ApiException(ResponseError error) {
        super(String.format(RESPONSE_ERROR_FORMAT, error.error, error.method, error.request));
        this.error = error;
    }

    public ApiException(String msg, Throwable tr){
        super(msg, tr);
    }

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(ResponseError error, int statusCode) {
        super(String.format(RESPONSE_ERROR_FORMAT, error.error, error.method, error.request));
        this.statusCode = statusCode;
        this.error = error;
    }

    public String getErrorOnly(){
        if (error != null)
            return error.error;
        else
            return "Unknown";
    }

    public ResponseError getApiError() {
        return error;
    }

    public int getStatusCode()  {
        return statusCode;
    }
}
