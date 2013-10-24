package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AlbumApi;
import com.ezimgur.api.ImageApi;
import com.google.inject.Inject;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 4/27/13
 * Time: 7:57 PM
 */
public class FavoriteItemTask extends LoadingTask<Boolean> {

    @Inject ImageApi mImageApi;
    @Inject AlbumApi mAlbumApi;

    private boolean mIsAlbum;
    private String mItemId;

    public FavoriteItemTask(Context context, String itemId, boolean isAlbum) {
        super(context);
        mItemId = itemId;
        mIsAlbum = isAlbum;
    }

    @Override
    public Boolean call() throws Exception {
        if (mIsAlbum){
            mAlbumApi.favoriteAlbum(mItemId);
        } else {
            mImageApi.favoriteImage(mItemId);
        }

        return true;
    }
}
