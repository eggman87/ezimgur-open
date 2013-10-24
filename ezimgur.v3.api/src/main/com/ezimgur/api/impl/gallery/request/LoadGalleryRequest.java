package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;
import com.ezimgur.datacontract.GallerySort;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 10:00 PM
 */
public class LoadGalleryRequest extends ApiGetRequest<ResponseContainer.GalleryLoadContainer> {

    private String galleryName;
    private GallerySort sort;
    private int page;

    public LoadGalleryRequest(String galleryName, GallerySort sort, int page) {
        this.galleryName = galleryName;
        this.sort = sort;
        this.page = page;
    }

    @Override
    public String getRequestUrl() {
       return String.format(ImgurApiConstants.URL_GALLERY_LOAD, galleryName.toLowerCase(), GallerySort.getSortStringForTargetType(sort, !galleryName.contains("r/")), page);
    }

    @Override
    protected Class<ResponseContainer.GalleryLoadContainer> getResponseClass() {
        return ResponseContainer.GalleryLoadContainer.class;
    }
}
