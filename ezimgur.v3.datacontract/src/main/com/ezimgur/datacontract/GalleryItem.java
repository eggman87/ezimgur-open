package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:55 PM
 */
public class GalleryItem implements Parcelable{

    public String id;
    public String title;
    public String description;
    @SerializedName("datetime")
    public Date dateCreated;
    public String bandwidth;
    public int ups;
    public int downs;
    public int score;
    public long views;
    @SerializedName("reddit_comments")
    public String redditCommentsLink;
    @SerializedName("account_url")
    public String accountUrl;
    public String vote;
    @SerializedName("is_album")
    public boolean isAlbum;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeByte((byte) (this instanceof GalleryImage ? 1:0));
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeLong(dateCreated.getTime());
        out.writeString(bandwidth);
        out.writeInt(ups);
        out.writeInt(downs);
        out.writeInt(score);
        out.writeLong(views);
        out.writeString(redditCommentsLink);
        out.writeString(accountUrl);
        out.writeString(vote);
        out.writeByte((byte) (isAlbum ? 1:0));
    }

    public GalleryItem(){

    }

    public GalleryItem (Parcel in){
        id = in.readString();
        title = in.readString();
        description = in.readString();
        dateCreated = new Date(in.readLong());
        bandwidth = in.readString();
        ups = in.readInt();
        downs = in.readInt();
        score = in.readInt();
        views = in.readLong();
        redditCommentsLink = in.readString();
        accountUrl = in.readString();
        vote = in.readString();
        isAlbum = in.readByte() == 1;
    }

    public static final Creator<GalleryItem> CREATOR = new Creator<GalleryItem>() {
        @Override
        public GalleryItem createFromParcel(Parcel in) {
            boolean isImage = in.readByte() == 1;

            if (isImage){
                return GalleryImage.CREATOR.createFromParcel(in);
            } else {
                return GalleryAlbum.CREATOR.createFromParcel(in);
            }
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[size];
        }
    };
}
