package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.GalleryItem;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/6/13
 * Time: 7:51 PM
 */
public class LoadGalleryItemTask extends LoadingTask<GalleryItem> {

    @Inject GalleryApi mGalleryApi;
    private String mGalleryItemId;

    protected LoadGalleryItemTask(Context context, String galleryItemId) {
        super(context);
        mGalleryItemId = galleryItemId;
    }

    @Override
    public GalleryItem call() throws Exception {
        return mGalleryApi.getGalleryItemById(mGalleryItemId);
    }
}
