package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.CommentApi;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/22/12
 * Time: 1:59 PM
 */
public class SubmitCommentTask extends LoadingTask<String> {

    @Inject CommentApi mCommentApi;
    private String mGalleryItemId;
    private String mCommentText;
    private String mParentCommentId;

    public SubmitCommentTask(Context context, String galleryItemId, String commentText) {
        super(context);
        mGalleryItemId = galleryItemId;
        mCommentText = commentText;
    }

    public SubmitCommentTask(Context context, String galleryItemId, String commentText, String parentCommentId) {
        super(context);
        mGalleryItemId = galleryItemId;
        mCommentText = commentText;
        mParentCommentId = parentCommentId;
    }

    @Override
    public String call() throws Exception {
        return mCommentApi.addCommentToGalleryItem(mGalleryItemId, mCommentText, mParentCommentId);
    }
}
