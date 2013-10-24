package com.ezimgur.api.http;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 2:48 PM
 */
public class ResponseError {

    public String error;
    public String request;
    public String parameters;
    public String method;
    public int statusCode;

    /*
        "data": {
        "error": "Unauthorized",
        "request": "/3/request/QzaoW",
        "parameters": "",
        "method": "POST"
    },
     */

    public class ResponseErrorContainer {
        public ResponseError data;
    }

}
