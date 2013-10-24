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
public class LoadGalleryImageRequest extends ApiGetRequest<ResponseContainer.GalleryImageContainer> {

    private String imageId;

    public LoadGalleryImageRequest(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_LOAD_IMAGE, imageId);
    }

    @Override
    protected Class<ResponseContainer.GalleryImageContainer> getResponseClass() {
        return ResponseContainer.GalleryImageContainer.class;
    }
}
