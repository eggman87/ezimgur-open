package com.ezimgur.datacontract;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/20/12
 * Time: 5:24 PM
 */
public class GalleryItemComposite implements Parcelable {
    public GalleryItem galleryItem;
    public Vote vote;
    public List<Comment> comments;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(galleryItem, 0);
        dest.writeParcelable(vote, 0);
        dest.writeTypedList(comments);
    }

    public static final Creator<GalleryItemComposite> CREATOR = new Creator<GalleryItemComposite>() {
        @Override
        public GalleryItemComposite createFromParcel(Parcel in) {
            GalleryItemComposite composite = new GalleryItemComposite();
            composite.galleryItem = in.readParcelable(GalleryItem.class.getClassLoader());
            composite.vote = in.readParcelable(Vote.class.getClassLoader());
            composite.comments = in.readArrayList(Comment.class.getClassLoader());
            return composite;
        }

        @Override
        public GalleryItemComposite[] newArray(int size) {
            return new GalleryItemComposite[size];
        }
    };
}
