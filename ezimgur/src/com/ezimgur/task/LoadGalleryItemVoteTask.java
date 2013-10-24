package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.Vote;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/20/12
 * Time: 6:21 PM
 */
public class LoadGalleryItemVoteTask extends LoadingTask<Vote> {

    @Inject GalleryApi mGalleryApi;
    private String mGalleryItemId;

    protected LoadGalleryItemVoteTask(Context context, String galleryItemId) {
        super(context);

        mGalleryItemId = galleryItemId;
    }

    @Override
    public Vote call() throws Exception {
        return mGalleryApi.getVotesForGalleryItem(mGalleryItemId);
    }
}
