package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.VoteType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:59 PM
 */
public interface CommentApi {

    /**
     * Gets a comment by a specified comment id. This will not get the replies for a given comment.
     * @param commentId
     * @return A comment if one exists, otherwise a APIException.
     * @throws ApiException
     */
    public Comment getCommentById(String commentId) throws ApiException;

    /**
     * Gets a comment with all comment replies for a given id.
     * @param commentId
     * @return
     * @throws ApiException
     */
    public Comment getCommentWithAllRepliesById(String commentId) throws ApiException;

    /**
     * Adds a comment to a image for the current user.
     * @param galleryItemId
     * @param commentText
     * @return the added comment id.
     * @throws ApiException
     */
    public String addCommentToGalleryItem(String galleryItemId, String commentText) throws ApiException;

    /**
     * Adds a comment to a image for the current user. Pass in parent id of a comment if this is a reply.
     * @param galleryItemId
     * @param commentText
     * @param parentId
     * @return the added comment id.
     * @throws ApiException
     */
    public String addCommentToGalleryItem(String galleryItemId, String commentText, String parentId) throws ApiException;

    /**
     * Deletes a comment by id.
     * @param commentId
     * @return a boolean value indicating if the deletion was successful.
     * @throws ApiException
     */
    public boolean deleteCommentById(String commentId) throws ApiException;

    /**
     * Votes for a comment with the given id.
     * @param commentId
     * @param type
     * @return a boolean value indicating if the vote was cast successfully.
     * @throws ApiException
     */
    public boolean voteForComment(String commentId, VoteType type) throws ApiException;

    /**
     * Reports a comment as spam or abuse.
     * @param commentId
     * @return a boolean value indicating if reporting was successful or not.
     * @throws ApiException
     */
    public boolean reportComment(String commentId) throws ApiException;

    /**
     * Replies to a comment for a given image.
     * @param commentId the id of the comment you are replying to.
     * @param imageId  the image id the comment you are replying to belongs to.
     * @param commentText  your comment.
     * @return a boolean value indicating success.
     * @throws ApiException
     */
    public String replyToComment(String commentId, String imageId, String commentText) throws ApiException;


}
