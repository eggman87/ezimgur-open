package com.ezimgur.view.event;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/4/13
 * Time: 10:55 PM
 */
public class UpVoteItemEvent {
    private String mGalleryItemId;

    public UpVoteItemEvent(String galleryItemId) {
        mGalleryItemId = galleryItemId;
    }

    public String getItemId(){
        return mGalleryItemId;
    }
}
