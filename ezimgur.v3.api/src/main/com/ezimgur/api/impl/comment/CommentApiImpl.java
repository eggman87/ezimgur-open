package com.ezimgur.api.impl.comment;

import com.ezimgur.api.CommentApi;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.comment.request.*;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.VoteType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:09 PM
 */
public class CommentApiImpl extends ApiBase implements CommentApi {

    @Override
    public Comment getCommentById(String commentId) throws ApiException {

        GetCommentRequest request = new GetCommentRequest(commentId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Comment getCommentWithAllRepliesById(String commentId) throws ApiException {

        GetCommentWithRepliesRequest request = new GetCommentWithRepliesRequest(commentId);
        submitApiRequest(request);

        return request.getItemToReceive().data.get(0);
    }

    @Override
    public String addCommentToGalleryItem(String galleryItemId, String commentText) throws ApiException {

        AddCommentToImageRequest request = new AddCommentToImageRequest(galleryItemId, commentText);
        submitApiRequest(request);

        return request.getItemToReceive().data.id;
    }

    @Override
    public String addCommentToGalleryItem(String galleryItemId, String commentText, String parentId) throws ApiException {

        AddCommentToImageRequest request = new AddCommentToImageRequest(galleryItemId, commentText, parentId);
        submitApiRequest(request);

        return request.getItemToReceive().data.id;
    }

    @Override
    public boolean deleteCommentById(String commentId) throws ApiException {

        DeleteCommentRequest request = new DeleteCommentRequest(commentId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public boolean voteForComment(String commentId, VoteType type) throws ApiException {

        VoteCommentRequest request = new VoteCommentRequest(commentId, type);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public boolean reportComment(String commentId) throws ApiException {

        ReportCommentRequest request = new ReportCommentRequest(commentId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public String replyToComment(String commentId, String imageId, String commentText) throws ApiException {

        ReplyToCommentRequest request = new ReplyToCommentRequest(commentId, imageId, commentText);
        submitApiRequest(request);

        return request.getItemToReceive().data.id;
    }
}
