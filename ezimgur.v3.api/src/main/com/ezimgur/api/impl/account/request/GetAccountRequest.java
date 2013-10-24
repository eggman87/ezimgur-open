package com.ezimgur.api.impl.account.request;

import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.account.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 8:52 PM
 */
public class GetAccountRequest extends ApiGetRequest<ResponseContainer.GetAccountContainer> {

    public GetAccountRequest(String username) {

    }

    @Override
    public String getRequestUrl() {
        return null;
    }

    @Override
    protected Class<ResponseContainer.GetAccountContainer> getResponseClass() {
        return null;
    }
}
