package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AlbumApi;
import com.ezimgur.datacontract.Album;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/8/12
 * Time: 8:51 PM
 */
public class LoadAlbumTask extends LoadingTask<Album> {

    @Inject AlbumApi mAlbumApi;
    private String mAlbumName;

    protected LoadAlbumTask(Context context, String albumName) {
        super(context);
        mAlbumName = albumName;
    }

    @Override
    public Album call() throws Exception {
        return mAlbumApi.getAlbumById(mAlbumName);
    }
}
