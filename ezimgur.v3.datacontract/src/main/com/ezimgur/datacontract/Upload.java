package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/27/12
 * Time: 10:55 PM
 */
public class Upload {

    public String id;
    @SerializedName("deletehash")
    public String deleteHash;
    public String link;

}
