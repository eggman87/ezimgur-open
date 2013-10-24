package com.ezimgur.api;

import com.ezimgur.api.exception.ApiException;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.datacontract.OAuthRequestType;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 11:25 PM
 */
public interface AuthenticationApi {

    /**
     * Will get a oauth URL for you to redirect a user to. Pass in a callback url for where the imgur site
     * should redirect the user after authorizing your app.
     * @param callbackUrl
     * @return A Oauth url to redirect to.
     */
    public String getOAuthUrl(String callbackUrl, OAuthRequestType type) throws ApiException;

    /**
     * Will get a authentication token from a verifier.
     * @param authenticationCode
     * @return
     * @throws ApiException
     */
    public AuthenticationToken getTokenFromAuthenticationCode(String authenticationCode, String callbackUrl) throws ApiException;

    /**
     * Gets a authentication token out of a URI fragment that is returned on the OAUTH callback.
     * @param uriFragment
     * @throws ApiException
     */
    public AuthenticationToken getTokenFromTokenResponse(String uriFragment) throws ApiException;

    /**
     * Refreshes a authentication token based on the passed in refresh token value.
     * @param refreshToken
     * @return
     * @throws ApiException
     */
    public AuthenticationToken refreshAuthenticationToken(String refreshToken) throws ApiException;

    /**
     * Will set the current session authentication token for the app. All calls made after the token is set
     * will be made using the supplied authentication token. You need to call this before making any API requests
     * that require authentication.
     * @param authenticationToken
     * @throws ApiException
     */
    public void setCurrentAuthenticationToken(AuthenticationToken authenticationToken) throws ApiException;

    /**
     * Get rid of the current auth token - essentially log the current user out.
     */
    public void clearCurrentAuthenticationToken();
}
