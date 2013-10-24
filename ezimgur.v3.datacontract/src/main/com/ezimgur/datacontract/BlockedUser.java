package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:08 PM
 */
public class BlockedUser {

    @SerializedName("blocked_id")
    public int id;
    @SerializedName("blocked_url")
    public String url;
}
