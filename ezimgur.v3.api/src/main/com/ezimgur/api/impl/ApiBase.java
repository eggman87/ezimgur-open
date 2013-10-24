package com.ezimgur.api.impl;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.http.HttpConnection;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 12:48 AM
 */
public class ApiBase {

    protected void checkSuccess(ApiRequest request) throws ApiException {
        if (!request.isSuccessful())
            throw new ApiException(request.getResponseError(), request.getStatusCode());
    }

    protected void submitApiRequest(ApiRequest request) throws ApiException{
        HttpConnection connection = new HttpConnection();
        if (request.getRequestMethod().equals(ApiRequest.RequestMethod.GET))
            connection.sendGetRequest(request);
        else if (request.getRequestMethod().equals(ApiRequest.RequestMethod.POST))
            connection.sendPostRequest(request);
        else if (request.getRequestMethod().equals(ApiRequest.RequestMethod.DELETE))
            connection.sendDeleteRequest(request);

        checkSuccess(request);
    }
}
