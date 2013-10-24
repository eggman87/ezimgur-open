package com.ezimgur.api.impl.account.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.account.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 9:06 PM
 */
public class GetAccountImagesRequest extends ApiGetRequest<ResponseContainer.GetAccountImagesContainer> {

    private String userName;
    private int page;

    public GetAccountImagesRequest(String userName, int page) {
        this.userName = userName;
        this.page = page;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ACCOUNT_GETIMAGES, userName, page);
    }

    @Override
    protected Class<ResponseContainer.GetAccountImagesContainer> getResponseClass() {
        return ResponseContainer.GetAccountImagesContainer.class;
    }
}
