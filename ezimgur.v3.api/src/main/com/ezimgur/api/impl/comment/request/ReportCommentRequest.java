package com.ezimgur.api.impl.comment.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.comment.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 5:30 PM
 */
public class ReportCommentRequest extends ApiRequest<Object, ResponseContainer.ReportCommentResponseContainer> {

    private String commentId;

    public ReportCommentRequest(String commentId) {
        this.commentId = commentId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_COMMENT_REPORT, commentId);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.ReportCommentResponseContainer> getResponseClass() {
        return ResponseContainer.ReportCommentResponseContainer.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }
}
