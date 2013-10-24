package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.GalleryAlbum;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/17/12
 * Time: 10:16 PM
 */
public class LoadGalleryAlbumTask extends LoadingTask<GalleryAlbum> {

    @Inject
    GalleryApi mGalleryApi;
    private String mAlbumName;

    protected LoadGalleryAlbumTask(Context context, String albumName) {
        super(context);
        mAlbumName = albumName;
    }

    @Override
    public GalleryAlbum call() throws Exception {

        return mGalleryApi.getGalleryAlbumById(mAlbumName);
    }
}
