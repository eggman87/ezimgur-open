package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 5:39 PM
 */
public class ReportGalleryItemRequest extends ApiRequest<Object, ResponseContainer.VoteResponse>{

    private String galleryItemId;

    public ReportGalleryItemRequest(String galleryItemId) {
        this.galleryItemId = galleryItemId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_REPORT_ITEM, galleryItemId);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.VoteResponse> getResponseClass() {
        return ResponseContainer.VoteResponse.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
