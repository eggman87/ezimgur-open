package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/15/12
 * Time: 11:28 PM
 */
public class AuthenticationToken {

    private static final String TAG = "AuthenticationToken";

    @SerializedName("access_token")
    public String accessToken;
    public long expires;
    @SerializedName("account_username")
    public String accountUserName;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("refresh_token")
    public String refreshToken;

    public boolean isExpired() {
        if (expires < System.currentTimeMillis())
            return true;
        return false;
    }

    public boolean isAuthenticated() {
        if (!isExpired() && accessToken != null)
            return true;
        return false;
    }

    /*
    {"access_token":null,"expires":-1355617767,"account_username":null},
    {"access_token":"85f2b86623b2bcc2087733d3a9f76bee1deb4b77","expires_in":3600,"token_type":"bearer","scope":null,"refresh_token":"a5510b8c6c3d3efee23b4011d8cf50b27a4b0950"}
     */
}
