package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiDeleteRequest;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 4:38 PM
 */
public class DeleteCommentRequest extends ApiDeleteRequest<ResponseContainer.DeleteCommentResponseContainer> {

    private String commentId;

    public DeleteCommentRequest(String commentId){
        this.commentId = commentId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_DELETE, commentId);
    }

    @Override
    protected Class<ResponseContainer.DeleteCommentResponseContainer> getResponseClass() {
        return ResponseContainer.DeleteCommentResponseContainer.class;
    }
}
