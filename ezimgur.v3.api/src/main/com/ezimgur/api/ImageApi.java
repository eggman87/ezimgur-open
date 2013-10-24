package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.image.request.payload.UploadImagePayload;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.datacontract.Upload;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 3:59 PM
 */
public interface ImageApi {

    /**
     * Get information about an image.
     * @param imageId
     * @return
     */
    public Image getImageById(String imageId) throws ApiException;

    /**
     * Delete an image with the deletehash that is returned to image owners.
     * @param deleteHash
     * @return
     */
    public boolean deleteImageByDeleteHash(String deleteHash);

    /**
     * @param payload
     * @return
     */
    public Upload uploadImage(UploadImagePayload payload) throws ApiException;

    /**
     * Update a image title and description.
     * @param title
     * @param description
     * @return
     */
    public boolean updateImage(String imageId, String title, String description);

    /**
     * Will give you a image link for a given image and desired size.
     * @param image
     * @param size
     * @return
     */
    public String getHttpUrlForImage(Image image, ImageSize size);

    /**
     * Get image extension.
     * @param image
     * @return
     */
    public String getExtensionForImage(Image image);

    /**
     * Will return a imgur page link for a given gallery and image.
     * @param galleryName
     * @param imageId
     * @return
     */
    public String getImgurPageUrlForImage(String galleryName, String imageId);

    /**
     * Favorites an image.
     * @param imageId
     * @return
     */
    public void favoriteImage(String imageId) throws ApiException;
}
