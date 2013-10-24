package com.ezimgur.api.impl.image.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.datacontract.Basic;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 4/27/13
 * Time: 7:54 PM
 */
public class FavoriteImageRequest extends ApiRequest<Object, FavoriteImageRequest.FavoriteImageResponse> {

    private String mImageId;

    public FavoriteImageRequest(String imageId) {
        mImageId = imageId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_FAVORITE_ITEM, mImageId);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<FavoriteImageResponse> getResponseClass() {
        return FavoriteImageResponse.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }

    public class FavoriteImageResponse extends Basic<String> {

    }
}
