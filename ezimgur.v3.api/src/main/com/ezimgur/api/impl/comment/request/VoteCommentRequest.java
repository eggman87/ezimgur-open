package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.comment.response.ResponseContainer;
import com.ezimgur.datacontract.VoteType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 5:24 PM
 */
public class VoteCommentRequest extends ApiRequest<Object, ResponseContainer.VoteCommentResponseContainer> {

    private String commentId;
    private VoteType voteType;

    public VoteCommentRequest(String commentId, VoteType type) {
        this.commentId = commentId;
        this.voteType = type;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_VOTE, commentId, voteType.toString().toLowerCase());
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.VoteCommentResponseContainer> getResponseClass() {
        return ResponseContainer.VoteCommentResponseContainer.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
