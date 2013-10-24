package com.ezimgur.api.impl.comment.request.payload;

import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:44 PM
 */
public class AddCommentPayload {

    @SerializedName("image_id")
    public String imageId;
    public String comment;
    @SerializedName("parent_id")
    public String parentId;
    /*
    image_id	required	The ID of the image in the gallery that you wish to comment on
comment	required	The comment text, this is what will be displayed
parent_id	optional	The ID of the parent comment, this is an alternative method to create a reply.
     */
}
