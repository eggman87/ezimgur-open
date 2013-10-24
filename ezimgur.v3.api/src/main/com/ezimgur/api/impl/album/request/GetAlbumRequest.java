package com.ezimgur.api.impl.album.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.album.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/5/13
 * Time: 11:46 PM
 */
public class GetAlbumRequest extends ApiGetRequest<ResponseContainer.GetAlbumContainer> {

    private String mAlbumId;

    public GetAlbumRequest(String albumId) {
        mAlbumId = albumId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ALBUM_GET_ALBUM, mAlbumId);
    }

    @Override
    protected Class<ResponseContainer.GetAlbumContainer> getResponseClass() {
        return ResponseContainer.GetAlbumContainer.class;
    }
}
