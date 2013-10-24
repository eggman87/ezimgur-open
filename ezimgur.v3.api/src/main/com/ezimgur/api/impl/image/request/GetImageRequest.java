package com.ezimgur.api.impl.image.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiGetRequest;
import com.ezimgur.api.impl.image.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/30/12
 * Time: 1:55 PM
 */
public class GetImageRequest extends ApiGetRequest<ResponseContainer.GetImageResponse> {

    private String imageId;

    public GetImageRequest(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_IMAGE_GET, imageId);
    }

    @Override
    protected Class<ResponseContainer.GetImageResponse> getResponseClass() {
        return ResponseContainer.GetImageResponse.class;
    }
}
