package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:10 PM
 */
public class AccountStats {

    @SerializedName("total_images")
    public int totalImages;
    @SerializedName("total_albums")
    public int totalAlbums;
    @SerializedName("disk_used")
    public String diskSpaceUsed;
    @SerializedName("bandwidth_used")
    public String bandwidthUsed;
    @SerializedName("top_images")
    public List<Image> topImages;
    @SerializedName("top_albums")
    public List<Album> topAlbums;
    @SerializedName("top_gallery_comments")
    public List<Comment> topComments;


    /*
total_images	integer	The amount of images associated with the account
total_albums	integer	The amount of albums associated with the account
disk_used	string	The amount of disk space used by the images
bandwidth_used	string	The amount of bandwidth used by the account
top_images	Array of images	The most popular Images in the account
top_albums	Array of albums	The most popular albums in the account
top_gallery_comments	Array of comments	The most popular request comments created by the user

    "data" : {
        "total_images"         : 17,
        "total_albums"         : 1,
        "disk_used"            : "8.92 MB",
        "bandwidth_used"       : "5.73 GB"
        "top_images"           : [],
        "top_albums"           : [],
        "top_gallery_comments" : [],
    }
     */
}
