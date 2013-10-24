package com.ezimgur.api.impl.authentication;

import com.ezimgur.api.AuthenticationApi;
import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.http.HttpConnection;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.authentication.request.GetTokenRefreshRequest;
import com.ezimgur.api.impl.authentication.request.GetTokenRequest;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.datacontract.OAuthRequestType;
import com.ezimgur.instrumentation.Log;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 12:09 AM
 */
public class AuthenticationApiImpl extends ApiBase implements AuthenticationApi {

    private static final String TAG = "EzImgur.AuthenticationApiImpl";

    @Override
    public String getOAuthUrl(String callbackUrl, OAuthRequestType type) throws ApiException {
        return String.format(ImgurApiConstants.OAUTH_URL, ImgurApiConstants.CLIENT_ID, callbackUrl, type.toString().toLowerCase());
    }

    @Override
    public AuthenticationToken getTokenFromAuthenticationCode(String authenticationCode, String callbackUrl) throws ApiException {
        HttpConnection connection = new HttpConnection();

        GetTokenRequest request = new GetTokenRequest(authenticationCode, callbackUrl);
        connection.sendUrlEncodedPostRequest(request);

        if (request.getItemToReceive() != null){
            AuthenticationToken token = new AuthenticationToken();
            token.accessToken = request.getItemToReceive().accessToken;
            token.expires = request.getItemToReceive().expires;
            token.accountUserName = request.getItemToReceive().accountUserName;
            token.tokenType = request.getItemToReceive().tokenType;
            token.refreshToken = request.getItemToReceive().refreshToken;
            return token;
        }

        throw new ApiException("Unable to parse token");
    }

    @Override
    public AuthenticationToken getTokenFromTokenResponse(String uriFragment) throws ApiException {

        AuthenticationToken token = new AuthenticationToken();

        String[] properties = uriFragment.split("[&]");
        for (String property : properties){
            String[] prop = property.split("=");
            if (prop[0].equalsIgnoreCase("access_token"))
                token.accessToken = prop[1];
            else if (prop[0].equalsIgnoreCase("token_type"))
                token.tokenType = prop[1];
            else if (prop[0].equals("expires_in")){
                long expiresValue = Long.parseLong(prop[1]);
                Log.d(TAG, "expires value="+expiresValue);
                token.expires = (expiresValue * 1000) + (System.currentTimeMillis());
            }
            else if (prop[0].equals("refresh_token"))
                token.refreshToken = prop[1];
            else if (prop[0].equals("account_username"))
                token.accountUserName = prop[1];
        }
        HttpConnection.setAuthenticationToken(token);
        return token;
        //https://example.com/oauthcallback#access_token=ACCESS_TOKEN&token_type=Bearer&expires_in=3600
    }

    @Override
    public AuthenticationToken refreshAuthenticationToken(String refreshToken) throws ApiException {

        GetTokenRefreshRequest request = new GetTokenRefreshRequest(refreshToken);

        HttpConnection connection = new HttpConnection();
        connection.sendPostRequest(request);

        AuthenticationToken newToken = new AuthenticationToken();
        newToken.refreshToken = request.getItemToReceive().refreshToken;
        newToken.accountUserName = request.getItemToReceive().accountUserName;
        Log.d(TAG, "expires value="+ request.getItemToReceive().expires);
        newToken.expires = (request.getItemToReceive().expires *1000) + (System.currentTimeMillis());
        newToken.accessToken = request.getItemToReceive().accessToken;

        return newToken;
    }

    @Override
    public void setCurrentAuthenticationToken(AuthenticationToken authenticationToken) throws ApiException {
        if (authenticationToken.accountUserName == null || authenticationToken.accessToken == null)
            throw new ApiException("When setting token information, username and accesstoken must be present");
        HttpConnection.setAuthenticationToken(authenticationToken);
    }

    @Override
    public void clearCurrentAuthenticationToken() {
        HttpConnection.setAuthenticationToken(null);
    }
}
