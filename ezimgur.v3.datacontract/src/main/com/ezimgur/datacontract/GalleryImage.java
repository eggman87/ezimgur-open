package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 8:04 PM
 */
public class GalleryImage extends GalleryItem  implements Parcelable{

    @SerializedName("type")
    public String mimeType;
    public boolean animated;
    public int width;
    public int height;
    public int size;

    public Image toImage() {
        Image image = new Image();
        image.id = id;
        image.animated = animated;
        image.bandwidth = bandwidth;
        image.dateCreated = dateCreated;
        image.height = height;
        image.mimeType = mimeType;
        image.size = size;
        image.title = title;
        image.views = views;
        image.width = width;
        image.accountUrl = accountUrl;

        return image;
    }

    public GalleryImage (Parcel in){
        super(in);

        mimeType = in.readString();
        animated = in.readByte() == 1;
        width = in.readInt();
        height = in.readInt();
        size = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);

        out.writeString(mimeType);
        out.writeByte((byte) (animated ? 1:0));
        out.writeInt(width);
        out.writeInt(height);
        out.writeInt(size);
    }

    public static final Creator<GalleryImage> CREATOR = new Creator<GalleryImage>() {
        @Override
        public GalleryImage createFromParcel(Parcel parcel) {

            return new GalleryImage(parcel);
        }

        @Override
        public GalleryImage[] newArray(int size) {
            return new GalleryImage[size];
        }
    };

    /*

id	string	The ID for the image
title	string	The title of the image in the request
datetime	int	Time inserted into the request, epoch time
type	string	Image MIME type.
animated	boolean	is the image animated
width	integer	The width of the image in pixels
height	integer	The height of the image in pixels
size	integer	The size of the image in bytes
views	integer	The number of image views
account_url	string	The username of the account that uploaded it, or null.
bandwidth	integer	Bandwidth consumed by the image in bytes
ups	integer	Upvotes for the image
downs	integer	Number of downvotes for the image
score	integer	Imgur popularity score
is_album	boolean	if it's an album or not

        "data": {
        "id": "OUHDm",
        "title": "My most recent drawing. Spent over 100 hours. I'm pretty proud of it.",
        "datetime": 1349051625,
        "type": "image/jpeg",
        "animated": false,
        "width": 2490,
        "height": 3025,
        "size": 618969,
        "views": 625622,
        "bandwidth": 387240623718,
        "account_url":"saponifi3d",
        "ups": 1889,
        "downs": 58,
        "score": 18935622
    },
     */
}
