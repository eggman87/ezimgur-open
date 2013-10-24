package com.ezimgur.task;

import android.content.Context;
import com.ezimgur.api.AccountApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Image;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 9:42 PM
 */
public class LoadAccountImagesTask extends LoadingTask <List<Image>> {

    @Inject AccountApi mAccountApi;
    private int mPage;
    private int mCount;

    protected LoadAccountImagesTask(Context context, int page, int count) {
        super(context);
        mPage = page;
        mCount = count;
    }

    @Override
    public List<Image> call() throws Exception {
        return mAccountApi.getAssociatedImages(EzApplication.getAccountUserName(), mPage);
    }

}
