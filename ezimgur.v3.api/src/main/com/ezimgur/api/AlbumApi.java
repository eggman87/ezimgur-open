package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Image;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:59 PM
 */
public interface AlbumApi {

    /**
     * Gets a album for a provided album id.
     *
     * @param albumId
     * @return
     */
    public Album getAlbumById(String albumId) throws ApiException;

    /**
     * Gets images for a album.
     * @param albumId
     * @return
     */
    public List<Image> getImagesForAlbum(String albumId) throws ApiException;

    /**
     * gets a image for a specified album.
     * @param imageId
     * @param albumId
     * @return
     */
    public Image getImageFromAlbum(String imageId, String albumId);

    /**
     * Creates a new album, all parameters are optional, pass null if you dont want to provide.
     * @param title
     * @param description
     * @param cover
     * @param imageIds
     * @return
     */
    public boolean createNewAlbum(String title, String description, String cover, List<String> imageIds);

    /**
     * Updates a album, all parameters are optional, pass null if you dont want to update a parameter.
     * @param title
     * @param description
     * @param cover
     * @param imageIds
     * @return
     */
    public boolean updateAlbum(String title, String description, String cover, List<String> imageIds);

    /**
     * Deletes a album with the given id. You must be logged in as the owner of the album to delete.
     * @param albumId
     * @return
     */
    public boolean deleteAlbum(String albumId);

    /**
     * Sets the images for an album, removes all other images and only uses the images in this request
     * @param imageIds
     * @return
     */
    public boolean setAlbumImages(List<String> imageIds);

    /**
     * Takes parameter, ids[], as an array of ids to add to the album.
     * @param imageIds
     * @return
     */
    public boolean addAlbumImages(List<String> imageIds);

    /**
     * Takes parameter, ids[], as an array of ids and removes from the album.
     * @param imagesId
     * @return
     */
    public boolean removeImagesFromAlbum(List<String> imagesId);

    /**
     * Returns the imgur http url for the given album.
     * @param albumId
     * @return
     */
    public String getHttpUrlForAlbum(String albumId);

    /**
     * Favorites an album.
     * @param albumId
     * @return
     */
    public void favoriteAlbum(String albumId) throws ApiException;


}
