package com.ezimgur.api.impl.authentication.response;

import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.datacontract.Basic;
import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 12:33 AM
 */
public class ResponseContainer {

    /* get token response does not follow imgur standard response model...which basically means i need,
     * to duplicate the properties of authentication token so it works with my http connection bounds. \
     * This is just a quick fix to get my app out...need to revisit a better way later.
     */
    public class GetTokenResponse extends Basic<AuthenticationToken>{

        @SerializedName("access_token")
        public String accessToken;
        @SerializedName("expires_in")
        public long expires;
        @SerializedName("account_username")
        public String accountUserName;
        @SerializedName("token_type")
        public String tokenType;
        @SerializedName("refresh_token")
        public String refreshToken;
    }
}
