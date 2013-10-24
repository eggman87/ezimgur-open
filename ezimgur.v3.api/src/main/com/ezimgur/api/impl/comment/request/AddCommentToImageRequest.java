package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.comment.request.payload.AddCommentPayload;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:44 PM
 */
public class AddCommentToImageRequest extends ApiRequest<AddCommentPayload, ResponseContainer.AddCommentResponseContainer> {

    public AddCommentToImageRequest(String imageId, String comment){
        this.itemToSend = new AddCommentPayload();
        this.itemToSend.imageId = imageId;
        this.itemToSend.comment = comment;
    }

    public AddCommentToImageRequest(String imageId, String comment, String parentId) {
        this.itemToSend = new AddCommentPayload();
        this.itemToSend.imageId = imageId;
        this.itemToSend.comment = comment;
        this.itemToSend.parentId = parentId;
    }

    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.URL_COMMENT_ADD;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.AddCommentResponseContainer> getResponseClass() {
        return ResponseContainer.AddCommentResponseContainer.class;
    }

    @Override
    protected Class<AddCommentPayload> getRequestClass() {
        return AddCommentPayload.class;
    }
}
