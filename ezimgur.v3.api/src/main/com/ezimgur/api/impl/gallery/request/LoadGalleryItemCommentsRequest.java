package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 2:26 PM
 */
public class LoadGalleryItemCommentsRequest extends ApiGetRequest<ResponseContainer.GalleryItemCommentsContainer> {

    private String galleryItemId;

    public LoadGalleryItemCommentsRequest(String galleryItemId) {
        this.galleryItemId = galleryItemId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_ITEM_COMMENTS, galleryItemId);
    }

    @Override
    protected Class<ResponseContainer.GalleryItemCommentsContainer> getResponseClass() {
        return ResponseContainer.GalleryItemCommentsContainer.class;
    }
}
