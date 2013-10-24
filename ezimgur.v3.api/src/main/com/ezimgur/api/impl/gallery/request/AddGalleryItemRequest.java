package com.ezimgur.api.impl.gallery.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.gallery.request.payload.AddGalleryItemPayload;
import com.ezimgur.api.impl.gallery.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 2:18 PM
 */
public class AddGalleryItemRequest extends ApiRequest<AddGalleryItemPayload, ResponseContainer.BasicBooleanResponse> {

    private String itemId;

    public AddGalleryItemRequest(String itemId, String itemTitle) {
        this.itemId = itemId;
        this.itemToSend = new AddGalleryItemPayload();
        this.itemToSend.title = itemTitle;
        this.itemToSend.terms = 1;
    }

    @Override
    public String getRequestUrl() {
        return  String.format(ImgurApiConstants.URL_GALLERY_ADD_ITEM, itemId);
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
    protected Class<AddGalleryItemPayload> getRequestClass() {
        return AddGalleryItemPayload.class;
    }
}
