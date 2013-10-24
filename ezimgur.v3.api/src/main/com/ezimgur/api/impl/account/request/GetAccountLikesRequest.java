package com.ezimgur.api.impl.account.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.account.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 10:11 PM
 */
public class GetAccountLikesRequest extends ApiGetRequest <ResponseContainer.GetAccountLikesResponse> {

    private String userName;

    public GetAccountLikesRequest(String userName) {
        this.userName = userName;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ACCOUNT_GET_LIKES, userName);
    }

    @Override
    protected Class<ResponseContainer.GetAccountLikesResponse> getResponseClass() {
        return ResponseContainer.GetAccountLikesResponse.class;
    }
}
