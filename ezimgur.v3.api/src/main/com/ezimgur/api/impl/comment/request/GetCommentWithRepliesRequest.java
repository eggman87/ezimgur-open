package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:36 PM
 */
public class GetCommentWithRepliesRequest extends ApiGetRequest<ResponseContainer.CommentWithRepliesContainer> {

    private String commentId;

    public GetCommentWithRepliesRequest(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_GET_WITH_REPLIES, commentId);
    }

    @Override
    protected Class<ResponseContainer.CommentWithRepliesContainer> getResponseClass() {
        return ResponseContainer.CommentWithRepliesContainer.class;
    }
}
