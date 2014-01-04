package com.ezimgur.datacontract;

import android.os.Parcel;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 7:54 PM
 */
public class GalleryAlbum extends GalleryItem {

    public String cover;
    @SerializedName("album_images")
    public List<Image> images;
    @SerializedName("album_images_count")
    public int imageCount;

    public GalleryAlbum(Parcel in) {
        super(in, true);

        cover = in.readString();
        images = new ArrayList<Image>();
        in.readTypedList(images, Image.CREATOR);

        imageCount = in.readInt();
    }

    public GalleryAlbum(Parcel in, boolean alreadyIgnoredFirstByte) {
        super(in);

        cover = in.readString();
        images = new ArrayList<Image>();
        in.readTypedList(images, Image.CREATOR);

        imageCount = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);

        out.writeString(cover);
        out.writeTypedList(images);
        out.writeInt(imageCount);
    }

    public Album toAlbum() {
        Album album = new Album();
        album.cover = cover;
        album.images = images;
        album.title = title;
        album.dateCreated = dateCreated;
        album.id = id;
        return  album;
    }

    public static final Creator<GalleryAlbum> CREATOR = new Creator<GalleryAlbum>() {

        @Override
        public GalleryAlbum createFromParcel(Parcel in) {
            return new GalleryAlbum(in);
        }

        @Override
        public GalleryAlbum[] newArray(int size) {
            return new GalleryAlbum[size];
        }
    };
    /*

id	string	The ID for the image
title	string	The title of the image in the request
datetime	int	Time inserted into the request, epoch time
cover	string	ID of the cover image in album.
album_images	Array of Images	An array of all the images in the album
album_images_count	integer	The total number of images in the album
views	integer	The number of image views
bandwidth	integer	Bandwidth consumed by the image in bytes
ups	integer	Upvotes for the image
downs	integer	Number of downvotes for the image
score	integer	Imgur popularity score
is_album	boolean	if it's an album or not

    "data": {
        "id":"ew1AZ",
        "title":"Album Test",
        "datetime": 0123456789,
        "cover":"55zeM",
        "album_images":[{
            "hash":"55zeM",
            "title":"",
            "description":"",
            "width":2560,
            "height":1600,
            "size":541577,
            "ext":".jpg",
            "animated":0,
            "datetime": 0123456789
        }],
        "album_images_count":1,
        "bandwidth":"0.00 KB",
        "ups":1,
        "downs":0,
        "score":1,
        "views":3
    },
     */
}
