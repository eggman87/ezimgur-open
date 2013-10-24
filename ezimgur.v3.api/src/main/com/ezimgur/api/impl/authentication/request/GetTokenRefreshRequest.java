package com.ezimgur.api.impl.authentication.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiRequest;
import com.ezimgur.api.impl.authentication.request.payload.RefreshTokenPayload;
import com.ezimgur.api.impl.authentication.response.ResponseContainer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/20/12
 * Time: 4:31 PM
 */
public class GetTokenRefreshRequest extends ApiRequest<RefreshTokenPayload, ResponseContainer.GetTokenResponse> {

    public GetTokenRefreshRequest(String refreshTokenValue) {
        itemToSend = new RefreshTokenPayload();
        itemToSend.refreshToken = refreshTokenValue;
        itemToSend.clientId = ImgurApiConstants.CLIENT_ID;
        itemToSend.clientSecret = ImgurApiConstants.CLIENT_SECRET;
        itemToSend.grantType = "refresh_token";
    }

    @Override
    public String getRequestUrl() {
        return ImgurApiConstants.OAUTH_REFRESH_TOKEN_URL;
    }

    @Override
    public RequestMethod getRequestMethod() {
        return RequestMethod.POST;
    }

    @Override
    protected Class<ResponseContainer.GetTokenResponse> getResponseClass() {
        return ResponseContainer.GetTokenResponse.class;
    }

    @Override
    protected Class<RefreshTokenPayload> getRequestClass() {
        return RefreshTokenPayload.class;
    }
}
