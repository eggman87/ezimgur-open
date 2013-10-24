package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:57 PM
 *
 * When calling a account API that takes 'username' as a parameter, pass in the account url of the current authenticated
 * user to get the current authenticated users data for that method. You must have a valid authentication token set.
 */
public interface AccountApi {

    /**
     * Get account info for a given username.
     * @param username
     * @return
     */
    public Account getAccount(String username);

    /**
     * Gets the images upvoted for the given username.
     * @return
     */
    public List<GalleryItem> getLikedItems(String username) throws ApiException;

    /**
     * Gets a list of images the user for the given username has submitted to the gallery.
     * @return
     */
    public List<Image> getSubmittedImages(String username, int page);

    /**
     * Gets the current users account settings.
     * @return
     */
    public AccountSettings getAccountSettings(String userName);

    /**
     * Get account stats for the target user.
     * @param username
     * @return
     */
    public AccountStats getAccountStats(String username);

    /**
     * Get the gallery totals for a given account.
     * @param username
     * @return
     */
    public GalleryTotals getAccountTotals(String username);

    /**
     * Get albums for a given account.
     * @param username
     * @return
     */
    public List<Album> getAccountAlbums(String username, int page) throws ApiException;

    /**
     * Get account album ids.
     * @param username
     * @return
     */
    public List<String> getAccountAlbumIds(String username);

    /**
     * Get account album count for a given user account.
     * @param username
     * @return
     */
    public int getAccountAlbumCount(String username);

    /**
     * Deletes a album by an id. Will only delete the album if it belongs to the current authenticated user.
     * @param albumId
     * @return
     */
    public boolean deleteAlbum(String albumId);

    /**
     * Returns the comments that the user has created.
     * @param username
     * @return
     */
    public List<Comment> getCommentsForAccount(String username);

    /**
     * Gets a list of comment ids for a given user account.
     * @param username
     * @return
     */
    public List<String> getCommentIdsForAccount(String username);

    /**
     * Get the total count of comments made by an account.
     * @param username
     * @return
     */
    public int getCommentCountForAccount(String username);

    /**
     * Delete comment for an account.
     * @return
     */
    public boolean deleteCommentForCurrentAccount();

    /**
     * Gets all images associated with the account.
     * @param username
     * @return
     */
    public List<Image> getAssociatedImages(String username, int page) throws ApiException;

    /**
     * Get images associated to the given user account.
     * @param username
     * @return
     */
    public List<String> getAssociatedImageIds(String username);

    /**
     * Get the total count of associated images for a given user account.
     * @param username
     * @return
     */
    public int getAssociatedImageCount(String username);

    /**
     * Delets a image based on a given image delete hash (not id).
     * @param deleteHash
     * @return
     */
    public boolean deleteImage(String deleteHash);

    /**
     * Gets a list of account notifications for the current authenticated user.
     * @return
     */
    public List<Notification> getAccountNotifications() throws ApiException;

    /**
     * Gets a list of account messages for the current authenticated user.
     * @return
     */
    public List<Message> getAccountMessages();

    /**
     * Sends a message to the provided username on behalf of the current authenticated user.
     * @param toUsername
     * @param message
     * @return
     */
    public boolean sendMessageToUser(String toUsername, Message message);

    /**
     * Get reply notifications for the current authenticated user.
     * @return
     */
    public List<Notification> getReplyNotifications();
}
