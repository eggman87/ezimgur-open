package com.ezimgur.api.impl.authentication.request.payload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/20/12
 * Time: 4:35 PM
 */
public class RefreshTokenPayload {
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("client_id")
    public String clientId;
    @SerializedName("client_secret")
    public String clientSecret;
    @SerializedName("grant_type")
    public String grantType;

    /*
    refresh_token	The refresh token returned from the authorization code exchange
client_id	The client_id obtained during application registration
client_secret	The client secret obtained during application registration
grant_type	 As defined in the OAuth2 specification, this field must contain a value of refresh_token
     */
}
