package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/17/12
 * Time: 9:09 PM
 */
public class LoadGalleryItemRequest extends ApiGetRequest<ResponseContainer.GalleryItemContainer> {

    private String galleryItemId;

    public LoadGalleryItemRequest(String galleryItemId) {
        this.galleryItemId = galleryItemId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_ITEM_GET, galleryItemId);
    }

    @Override
    protected Class<ResponseContainer.GalleryItemContainer> getResponseClass() {
        return ResponseContainer.GalleryItemContainer.class;
    }
}
