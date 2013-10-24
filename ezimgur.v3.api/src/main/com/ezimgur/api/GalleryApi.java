package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:46 PM
 */
public interface GalleryApi {

    /**
     * Gets a image from the gallery by id.
     * @param imageId
     * @return The image if it is found, otherwise ApiException will be thrown.
     * @throws ApiException
     */
    public GalleryImage getGalleryImageById(String imageId) throws ApiException;

    /**
     * Gets a album from the gallery by id.
     * @param albumId
     * @return The album if it is found, otherwise ApiException will be thrown.
     * @throws ApiException
     */
    public GalleryAlbum getGalleryAlbumById(String albumId) throws ApiException;

    /**
     * Gets a gallery item from gallery by item id.
     * @param galleryItemId
     * @return a gallery item which will be either a instance of album or image.
     * @throws ApiException
     */
    public GalleryItem getGalleryItemById(String galleryItemId) throws ApiException;

    /**
     * Gets the gallery item along with the item comments. This makes two HTTP calls due to there not
     * being a single api method for this.
     *
     * @param galleryItemId
     * @return
     * @throws ApiException
     */
    public GalleryItemComposite getGalleryItemWithCommentsById(String galleryItemId) throws ApiException;

    /**
     * Adds an image to the gallery. Currently this does not work as the API does not explain what this is for.
     *
     * @param imageId
     * @param title
     * @return GalleryImage that has been added.
     * @throws ApiException
     */
    public boolean addImageToGallery(String imageId, String title) throws ApiException;

    /**
     * Adds an album to the gallery. Currently this does not work as the API does not explain what this is for.
     *
     * @param albumId
     * @param title
     * @return the added gallery album.
     * @throws ApiException
     */
    public boolean addAlbumToGallery(String albumId, String title) throws ApiException;

    /**
     * Search the gallery for provided search terms.
     * @param searchTerms
     * @return A list of gallery items that will be a image or album at runtime.
     * @throws ApiException
     */
    public List<GalleryItem> searchGallery(String searchTerms) throws ApiException;

    /**
     * Loads a gallery with the given name, sort, and page.
     * @param galleryName
     * @param sort
     * @param page
     * @return returns a list of gallery items that will be a image or album at runtime.
     * @throws ApiException
     */
    public List<GalleryItem> getGalleryItems(String galleryName, GallerySort sort, int page) throws ApiException;

    /**
     * Reports a image to imgur as spam or abuse.
     * @param galleryItemId
     * @return A vote as the image currently stands.
     */
    public Vote reportGalleryItem(String galleryItemId) throws ApiException;

    /**
     * Gets the voting information for a imgur gallery item.
     * @param galleryItemId
     * @return The vote information.
     */
    public Vote getVotesForGalleryItem(String galleryItemId) throws ApiException;

    /**
     * Submits a vote for a gallery item on behalf of the current authenticated user.
     * @param galleryItemId
     * @param type
     * @return a boolean value that will be true if casting the vote was successful.
     */
    public boolean submitVoteForGalleryItem(String galleryItemId, VoteType type) throws ApiException;

    /**
     * Loads a list of comments for the specified gallery item.
     * @param galleryItemId
     * @return  A list of comments.
     * @throws ApiException
     */
    public List<Comment> getCommentsForGalleryItem(String galleryItemId) throws ApiException;

    /**
     * Submits a comment for a given gallery item. Requires authentication.
     * @param galleryItemId
     * @param commentText
     * @return
     * @throws ApiException
     */
    public Comment submitCommentForGalleryItem(String galleryItemId, String commentText) throws ApiException;

    /**
     * Submits a comment reply to a parent comment for a given gallery item. Requires authentication.
     * @param galleryItemId
     * @param parentCommentId
     * @param commentText
     * @return
     * @throws ApiException
     */
    public Comment submitReplyCommentForGalleryItem(String galleryItemId, String parentCommentId, String commentText) throws ApiException;

}
