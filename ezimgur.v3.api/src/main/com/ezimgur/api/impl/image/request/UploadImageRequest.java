package com.ezimgur.api.impl.image.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiMultiPartPostRequest;
import com.ezimgur.api.impl.image.request.payload.UploadImagePayload;
import com.ezimgur.api.impl.image.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 8:51 PM
 */
public class UploadImageRequest extends ApiMultiPartPostRequest<UploadImagePayload, ResponseContainer.UploadImageResponse> {

    public UploadImageRequest(UploadImagePayload requestItem) {
        super(requestItem);
    }

    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.URL_IMAGE_UPLOAD;
    }

    @Override
    protected Class<ResponseContainer.UploadImageResponse> getResponseClass() {
        return ResponseContainer.UploadImageResponse.class;
    }
}
