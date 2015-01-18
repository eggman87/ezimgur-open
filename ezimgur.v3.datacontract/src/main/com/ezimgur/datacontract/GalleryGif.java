package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class GalleryGif extends GalleryImage implements Parcelable {

    public boolean looping;
    @SerializedName("mp4")
    public String movieUrl;

    @Override
    public Image toImage() {
        Image image = super.toImage();
        image.movieUrl = movieUrl;
        return image;
    }

    public GalleryGif(Parcel in) {
        super(in);

        looping = in.readByte() == 1;
        movieUrl = in.readString();
    }

    public GalleryGif(Parcel in, boolean alreadyIgnoredFirstByte) {
        super(in, alreadyIgnoredFirstByte);

        looping = in.readByte() == 1;
        movieUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        super.writeToParcel(out, i);

        out.writeByte((byte) (looping ? 1:0));
        out.writeString(movieUrl);
    }

    public static final Creator<GalleryGif> CREATOR = new Creator<GalleryGif>() {
        @Override
        public GalleryGif createFromParcel(Parcel parcel) {

            return new GalleryGif(parcel);
        }

        @Override
        public GalleryGif[] newArray(int size) {
            return new GalleryGif[size];
        }
    };
}
