package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AccountApi;
import com.ezimgur.datacontract.GalleryItem;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 10:15 PM
 */
public class LoadLikedItemsTask extends LoadingTask<List<GalleryItem>> {

    @Inject
    AccountApi mAccountApi;
    private String userName;

    public LoadLikedItemsTask(Context context, String userName) {
        super(context);
        this.userName = userName;
    }

    @Override
    public List<GalleryItem> call() throws Exception {
        return mAccountApi.getLikedItems(userName);
    }
}
