package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;
import com.ezimgur.datacontract.VoteType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 1:19 PM
 */
public class VoteGalleryItemRequest extends ApiRequest<Object, ResponseContainer.BasicBooleanResponse> {

    private String galleryItemId;
    private VoteType voteType;

    public VoteGalleryItemRequest(String galleryItemId, VoteType voteType) {
        this.galleryItemId = galleryItemId;
        this.voteType = voteType;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_GALLERY_SUBMIT_VOTE, galleryItemId, voteType.toString().toLowerCase());
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.BasicBooleanResponse> getResponseClass() {
        return ResponseContainer.BasicBooleanResponse.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
