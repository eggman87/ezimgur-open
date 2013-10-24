package com.ezimgur.task;

import android.content.Context;
import android.widget.Toast;
import com.ezimgur.api.GalleryApi;
import com.ezimgur.datacontract.VoteType;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/20/12
 * Time: 4:10 PM
 */
public class VoteOnImageTask extends LoadingTask <Boolean> {

    @Inject GalleryApi mGalleryApi;
    private VoteType mVoteType;
    private String mGalleryItemId;

    protected VoteOnImageTask(Context context, String galleryItemId, VoteType voteType) {
        super(context);

        mVoteType = voteType;
        mGalleryItemId = galleryItemId;
    }

    @Override
    public Boolean call() throws Exception {
        return mGalleryApi.submitVoteForGalleryItem(mGalleryItemId, mVoteType);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);
        Toast.makeText(getContext(), "unable to vote for image : "+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
