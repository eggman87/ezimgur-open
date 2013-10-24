package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.Comment;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/18/12
 * Time: 9:39 AM
 */
public class LoadCaptionTask extends LoadingTask<List<Comment>> {
    @Inject GalleryApi mGalleryApi;
    private String mGalleryItemId;

    public LoadCaptionTask(Context context, String galleryItemId) {
        super(context);
        mGalleryItemId = galleryItemId;
    }

    @Override
    public List<Comment> call() throws Exception {
        return mGalleryApi.getCommentsForGalleryItem(mGalleryItemId);
    }
}
