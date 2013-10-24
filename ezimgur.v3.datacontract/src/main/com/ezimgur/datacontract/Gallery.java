package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/17/12
 * Time: 9:50 PM
 */
public class Gallery implements Parcelable {
    public String galleryName;
    public List<GalleryItem> imageList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(galleryName);
        out.writeTypedList(imageList);
    }

    public static final Creator<Gallery> CREATOR = new Creator<Gallery>() {
        @Override
        public Gallery createFromParcel(Parcel in) {

            Gallery gallery = new Gallery();
            gallery.galleryName = in.readString();
            gallery.imageList = new ArrayList<GalleryItem>();
            in.readTypedList(gallery.imageList, GalleryItem.CREATOR);

            return gallery;
        }

        @Override
        public Gallery[] newArray(int size) {
            return new Gallery[size];
        }
    };
}
