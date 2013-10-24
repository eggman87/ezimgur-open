package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 7:01 PM
 */
public class LoadGalleryItemVotesRequest extends ApiGetRequest <ResponseContainer.VoteResponse> {

    private String galleryItemId;

    public LoadGalleryItemVotesRequest(String galleryItemId) {
        this.galleryItemId = galleryItemId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_GET_VOTE, this.galleryItemId);
    }

    @Override
    protected Class<ResponseContainer.VoteResponse> getResponseClass() {
        return ResponseContainer.VoteResponse.class;
    }
}
