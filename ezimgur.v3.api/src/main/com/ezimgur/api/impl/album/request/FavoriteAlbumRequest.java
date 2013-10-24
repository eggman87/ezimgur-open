package com.ezimgur.api.impl.album.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.datacontract.Basic;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 4/27/13
 * Time: 7:49 PM
 */
public class FavoriteAlbumRequest extends ApiRequest<Object, FavoriteAlbumRequest.FavoriteResponse> {

    private String mAlbumId;

    public FavoriteAlbumRequest(String albumId){
        mAlbumId = albumId;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.URL_FAVORITE_ALBUM, mAlbumId);
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<FavoriteResponse> getResponseClass() {
        return FavoriteResponse.class;
    }

    @Override
    protected Class<Object> getRequestClass() {
        return Object.class;
    }

    public class FavoriteResponse extends Basic<String>{

    }
}
