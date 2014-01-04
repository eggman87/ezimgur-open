package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:14 PM
 *
 * Use collections.sort to sort by points - at the time of writing this the API and imgur website returns
 * captions unsorted by points.
 */
public class Comment extends NotificationContent implements Parcelable, Comparable<Comment>{

    public String id;
    @SerializedName("image_id")
    public String imageId;
    public String comment;
    public String author;
    @SerializedName("author_id")
    public int authorId;
    @SerializedName("on_album")
    public boolean onAlbum;
    @SerializedName("album_cover")
    public String albumCoverId;
    public int ups;
    public int downs;
    public int points;
    @SerializedName("datetime")
    public Date dateCreated;
    @SerializedName("parent_id")
    public String parentId;
    public boolean deleted;
    public String vote;
    public List<Comment> children;

    @Override
    public int compareTo(Comment o) {
        if (this.points < o.points)
            return 1;
        else if (this.points > o.points)
            return -1 ;
        else
            return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(imageId);
        dest.writeString(comment);
        dest.writeString(author);
        dest.writeInt(authorId);
        dest.writeByte((byte) (onAlbum ? 1:0));
        dest.writeString(albumCoverId);
        dest.writeInt(ups);
        dest.writeInt(downs);
        dest.writeInt(points);
        dest.writeLong(dateCreated.getTime());
        dest.writeString(parentId);
        dest.writeByte((byte) (deleted ? 1:0));
        dest.writeString(vote);
        dest.writeTypedList(children);
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            Comment comment = new Comment();
            comment.id = in.readString();
            comment.imageId = in.readString();
            comment.comment = in.readString();
            comment.author = in.readString();
            comment.authorId = in.readInt();
            comment.onAlbum = in.readByte() == 1;
            comment.albumCoverId = in.readString();
            comment.ups = in.readInt();
            comment.downs = in.readInt();
            comment.points = in.readInt();
            comment.dateCreated = new Date(in.readLong());
            comment.parentId = in.readString();
            comment.deleted = in.readByte() == 1;
            comment.vote = in.readString();
            comment.children = new ArrayList<Comment>();
            in.readTypedList(comment.children, Comment.CREATOR);
            return comment;
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
    /*

id	string	The ID for the comment
image_id	string	The ID of the image that the comment is for
caption	string	The comment itself.
author	string	Username of the author of the comment
author_id	integer	The account ID for the author
on_album	boolean	If this comment was done to an album
album_cover	string	The ID of the album cover image, this is what should be displayed for album comments
ups	intger	Number of upvotes for the comment
downs	integer	The number of downvotes for the comment
points	float	the number of upvotes - downvotes
datetime	integer	Timestamp of creation, epoch time
parent_id	integer	If this is a reply, this will be the value of the comment_id for the caption this a reply for.
deleted	boolean	Marked true if this caption has been deleted
children	Array of comments	All of the replies for this comment, this is optional and only appears some of the time

        "data": {
        "id": 1110,
        "image_id": "K84kO",
        "caption": "This is a Test Caption sent via the API!",
        "author": "joshTest",
        "author_id": 384077,
        "on_album":false,
        "album_cover":null,
        "ups": 5,
        "downs": 0,
        "points": 5,
        "datetime": 1346977487,
        "parent_id": null,
        "deleted": true

    }, {"data":[{"id":11833407,"image_id":"KXqnw","comment":"Being from Ireland myself, I apologize to everyone for these absolute wankers.  Embarrassment overload."
    ,"author":"ShaunDuffySD","author_id":2614954,"on_album":false,"album_cover":null,
    "ups":455,"downs":9,"points":446,"datetime":1355601514,"parent_id":0,"deleted":false,
    "vote":null,
     */
}
