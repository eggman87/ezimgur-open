package com.ezimgur.api.impl.image;

import com.ezimgur.api.ImageApi;
import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.http.HttpConnection;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.image.request.FavoriteImageRequest;
import com.ezimgur.api.impl.image.request.GetImageRequest;
import com.ezimgur.api.impl.image.request.UploadImageRequest;
import com.ezimgur.api.impl.image.request.payload.UploadImagePayload;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.datacontract.Upload;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 7:35 PM
 */
public class ImageApiImpl extends ApiBase implements ImageApi {
    @Override
    public Image getImageById(String imageId) throws ApiException{

        GetImageRequest request = new GetImageRequest(imageId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public boolean deleteImageByDeleteHash(String deleteHash) {
        return false;
    }

    @Override
    public Upload uploadImage(UploadImagePayload payload) throws ApiException {
        HttpConnection connection = new HttpConnection();

        UploadImageRequest request = new UploadImageRequest(payload);
        connection.sendMultiPartPostRequest(request);

        if (!request.isSuccessful())
            throw new ApiException(request.getResponseError());

        return request.getItemToReceive().data;
    }

    @Override
    public boolean updateImage(String imageId, String title, String description) {
        return false;
    }

    @Override
    public String getHttpUrlForImage(Image image, ImageSize size) {
        return String.format(ImgurApiConstants.URL_IMAGE_FORMAT, image.id, size.getSuffix(), getExtensionForImage(image));
    }

    @Override
    public String getExtensionForImage(Image image) {

        String ext = ".jpg";
        if (image.mimeType.equals("image/png")){
            ext = ".png";
        } else if (image.mimeType.equals("image/gif")){
            if (image.movieUrl != null) {
                ext = ".gifv";
            } else {
                ext = ".gif";
            }
        }
        return ext;
    }

    @Override
    public String getImgurPageUrlForImage(String galleryName, String imageId) {
        String nameToUse = galleryName;
        if (galleryName != null && !galleryName.contains("r/"))
            nameToUse = "gallery";
        return String.format(ImgurApiConstants.URL_IMAGE_PAGE_FORMAT, nameToUse, imageId);
    }

    @Override
    public void favoriteImage(String imageId) throws ApiException{
        FavoriteImageRequest request = new FavoriteImageRequest(imageId);
        submitApiRequest(request);
    }
}
