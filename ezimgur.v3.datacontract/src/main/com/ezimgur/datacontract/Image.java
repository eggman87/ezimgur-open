package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:28 PM
 */
public class Image implements Parcelable {
    public String id;
    public String title;
    public String description;
    @SerializedName("datetime")
    public Date dateCreated;
    @SerializedName("type")
    public String mimeType;
    public boolean animated;
    public int width;
    public int height;
    public int size;
    public long views;
    public String bandwidth;
    @SerializedName("deletehash")
    public String deleteHash;
    public String accountUrl;
    @SerializedName("mp4")
    public String movieUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(description);
        out.writeLong(dateCreated.getTime());
        out.writeString(mimeType);
        out.writeByte((byte) (animated ? 1:0));
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(size);
        out.writeLong(views);
        out.writeString(bandwidth);
        out.writeString(deleteHash);
        out.writeString(accountUrl);
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            Image image = new Image();
            image.id = in.readString();
            image.title = in.readString();
            image.description = in.readString();
            image.dateCreated = new Date(in.readLong());
            image.mimeType = in.readString();
            image.animated = in.readByte() == 1;
            image.width = in.readInt();
            image.height = in.readInt();
            image.size = in.readInt();
            image.views = in.readLong();
            image.bandwidth = in.readString();
            image.deleteHash = in.readString();
            image.accountUrl = in.readString();

            return image;
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };



    /*

id	string	The ID for the image
link	string	The direct link to the the image
title	string	The title of the image.
description	string	Description of the image.
datetime	int	Time inserted into the request, epoch time
type	string	Image MIME type.
animated	boolean	is the image animated
width	integer	The width of the image in pixels
height	integer	The height of the image in pixels
size	integer	The size of the image in bytes
views	integer	The number of image views
bandwidth	integer	Bandwidth consumed by the image in bytes
deletehash	string	OPTIONAL, the deletehash, if you're logged in as the image owner

        "data": {
        "id": "SbBGk",
        "title": null,
        "description": null,
        "datetime": 1341533193,
        "type": "image/jpeg",
        "animated": false,
        "width": 2559,
        "height": 1439,
        "size": 521916,
        "views": 1,
        "bandwidth": 521916,
        "deletehash": "eYZd3NNJHsbreDa"


    },
     */
}
