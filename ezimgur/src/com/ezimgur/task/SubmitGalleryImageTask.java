package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/31/12
 * Time: 7:36 PM
 */
public class SubmitGalleryImageTask extends LoadingTask<Boolean> {

    @Inject GalleryApi mGalleryApi;
    private String mId;
    private String mTitle;

    protected SubmitGalleryImageTask(Context context, String itemId, String title) {
        super(context);
        mId = itemId;
        mTitle = title;
    }

    @Override
    public Boolean call() throws Exception {
        return mGalleryApi.addImageToGallery(mId, mTitle);
    }
}
