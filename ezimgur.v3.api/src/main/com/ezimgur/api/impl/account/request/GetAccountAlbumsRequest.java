package com.ezimgur.api.impl.account.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.account.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 8:55 PM
 */
public class GetAccountAlbumsRequest extends ApiGetRequest<ResponseContainer.GetAccountAlbumContainer> {

    private String userName;
    private int page;

    public GetAccountAlbumsRequest(String userName, int page) {
        this.userName = userName;
        this.page = page;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ACCOUNT_GETALBUMS, userName, page);
    }

    @Override
    protected Class<ResponseContainer.GetAccountAlbumContainer> getResponseClass() {
        return ResponseContainer.GetAccountAlbumContainer.class;
    }
}
