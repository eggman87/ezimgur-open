package com.ezimgur.api.impl.album.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.album.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 9:23 PM
 */
public class GetImagesForAlbumRequest extends ApiGetRequest<ResponseContainer.GetAlbumImagesContainer> {

    private String albumId;

    public GetImagesForAlbumRequest(String albumId) {
        this.albumId = albumId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_ALBUM_GET_IMAGES, albumId);
    }

    @Override
    protected Class<ResponseContainer.GetAlbumImagesContainer> getResponseClass() {
        return ResponseContainer.GetAlbumImagesContainer.class;
    }
}
