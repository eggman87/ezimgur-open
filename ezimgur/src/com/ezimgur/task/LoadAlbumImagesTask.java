package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AlbumApi;
import com.ezimgur.datacontract.Image;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 2:49 PM
 */
public class LoadAlbumImagesTask extends LoadingTask <List<Image>> {

    @Inject private AlbumApi mAlbumApi;

    private String mAlbumName;
    private int mPage;
    private int mCount;

    public LoadAlbumImagesTask(Context context, int page, int count, String albumName) {
        super(context);

        mAlbumName = albumName;
        mPage = page;
        mCount = count;
    }

    @Override
    public List<Image> call() throws Exception {
        return mAlbumApi.getImagesForAlbum(mAlbumName);
        //return mImgurApi.getAccountImagesForAlbum(mAlbumName, mPage, mCount);
    }
}
