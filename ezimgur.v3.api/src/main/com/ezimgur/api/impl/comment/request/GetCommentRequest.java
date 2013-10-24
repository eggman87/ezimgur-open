package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:11 PM
 */
public class GetCommentRequest extends ApiGetRequest <ResponseContainer.CommentContainer> {

    private String commentId;

    public GetCommentRequest(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_GET, commentId);
    }

    @Override
    protected Class<ResponseContainer.CommentContainer> getResponseClass() {
        return ResponseContainer.CommentContainer.class;
    }
}
