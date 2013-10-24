package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.datacontract.Album;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/31/12
 * Time: 1:01 PM
 */
public class LoadLocalAlbumTask extends LoadingTask<Album> {

    private String mAlbumId;

    protected LoadLocalAlbumTask(Context context, String albumId) {
        super(context);
        mAlbumId = albumId;
    }

    @Override
    public Album call() throws Exception {
        AlbumsManager albumsManager = new AlbumsManager(getContext());
        Album album = albumsManager.getAlbumWithImagesById(mAlbumId);
        return album;
    }
}
