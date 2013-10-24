package com.ezimgur.api.impl.authentication.request;

import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.http.ApiUrlEncodedPostRequest;
import com.ezimgur.api.impl.authentication.response.ResponseContainer;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 12:26 AM
 */
public class GetTokenRequest extends ApiUrlEncodedPostRequest<ResponseContainer.GetTokenResponse> {

    private String authorizationCode;
    private String redirectUrl;

    public GetTokenRequest(String authorizationCode, String redirectUrl) {

        this.authorizationCode = authorizationCode;
        this.redirectUrl = redirectUrl;

//        itemToSend = new GetTokenPayload();
//        itemToSend.authorizationCode = authorizationCode;
//        itemToSend.clientId = ImgurApiConstants.CLIENT_ID;
//        itemToSend.clientSecret = ImgurApiConstants.CLIENT_SECRET;
//        itemToSend.grantType = "refresh_token";
//        itemToSend.code = authorizationCode;
    }

    @Override
    public String getRequestUrl() {
        return String.format(ImgurApiConstants.OAUTH_REFRESH_TOKEN_URL);
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
    public List<NameValuePair> getUrlEncodedEntityValues() {
        List<NameValuePair> values = new ArrayList<NameValuePair>();
        values.add(new BasicNameValuePair("grant_type", "token"));
        values.add(new BasicNameValuePair("code", authorizationCode));
        values.add(new BasicNameValuePair("redirect_url", redirectUrl));
        values.add(new BasicNameValuePair("client_id", ImgurApiConstants.CLIENT_ID));
        return values;
    }

//    @Override
//    protected Class<GetTokenPayload> getRequestClass() {
//        return GetTokenPayload.class;
//    }
}
