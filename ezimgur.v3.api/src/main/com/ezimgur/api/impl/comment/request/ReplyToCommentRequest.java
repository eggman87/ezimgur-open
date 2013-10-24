package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.comment.request.payload.AddCommentPayload;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 5:44 PM
 */
public class ReplyToCommentRequest extends ApiRequest<AddCommentPayload, ResponseContainer.AddCommentResponseContainer> {

    private String parentCommentId;

    public ReplyToCommentRequest(String parentCommentId, String imageId, String commentText) {
        this.parentCommentId = parentCommentId;
        this.itemToSend = new AddCommentPayload();
        this.itemToSend.imageId = imageId;
        this.itemToSend.comment = commentText;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_REPLY, parentCommentId);
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
