package com.ezimgur.datacontract;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:08 PM
 */
public class GalleryTotals {
    @SerializedName("total_gallery_comments")
    public int totalComments;
    @SerializedName("total_gallery_likes")
    public int totalLikes;
    @SerializedName("total_gallery_submissions")
    public int totalSubmissions;

    /*

total_gallery_comments	int	Total number of comments the user has made in the request
total_gallery_likes	int	Total number of images liked by the user in the request
total_gallery_submissions	int	Total number of images submitted by the user.

        "data": {
        "total_gallery_comments": 40,
        "total_gallery_likes": 23,
        "total_gallery_submissions": 4,
    },
     */
}
