package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AccountApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Album;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 2:28 PM
 */
public class LoadAlbumsTask extends LoadingTask<List<Album>> {

    @Inject private AccountApi mAccountApi;

    private int mPage;
    private int mCount;

    protected LoadAlbumsTask(Context context, int page, int count) {
        super(context);

        mPage = page;
        mCount = count;
    }

    @Override
    public List<Album> call() throws Exception {

        return mAccountApi.getAccountAlbums(EzApplication.getAccountUserName(), mPage);
    }
}
