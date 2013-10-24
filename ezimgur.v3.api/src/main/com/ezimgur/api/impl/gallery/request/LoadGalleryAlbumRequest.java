package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:07 PM
 */
public class LoadGalleryAlbumRequest extends ApiGetRequest<ResponseContainer.GalleryAlbumContainer> {

    private String albumId;

    public LoadGalleryAlbumRequest(String albumId) {
        this.albumId = albumId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_LOAD_ALBUM, albumId);
    }

    @Override
    protected Class<ResponseContainer.GalleryAlbumContainer> getResponseClass() {
        return ResponseContainer.GalleryAlbumContainer.class;
    }
}
