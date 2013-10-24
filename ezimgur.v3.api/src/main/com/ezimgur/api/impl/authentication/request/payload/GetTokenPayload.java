package com.ezimgur.api.impl.authentication.request.payload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 12:29 AM
 */
public class GetTokenPayload {

    @SerializedName("refresh_token")
    public String authorizationCode;
    @SerializedName("client_id")
    public String clientId;
    @SerializedName("client_secret")
    public String clientSecret;
    @SerializedName("grant_type")
    public String grantType;
    public String code;


    /*{"refresh_token":"$authorization_code","client_id":"$client_id","client_secret":"$client_secret","grant_type":"refresh_token"}"*/
}
