package com.ezimgur.task;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import com.ezimgur.api.AccountApi;
import com.ezimgur.api.AuthenticationApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.persistance.datasource.GalleryDataSource;
import com.ezimgur.view.event.InitApplicationEvent;
import com.ezimgur.view.fragment.DialogLoading;
import com.ezimgur.view.utils.EzImageLoader;
import com.google.inject.Inject;
import roboguice.event.EventManager;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 11:04 AM
 */
public class InitApplicationTask extends LoadingTask<Boolean> {

    private DialogLoading mDialog;
    @Inject private FragmentManager mFragmentManager;
    @Inject private EventManager mEventManager;
    @Inject private AuthenticationApi mAuthenticationApi;
    @Inject private AccountApi mAccountApi;

    private static final String TAG = "EzImgur.InitApplicationTask";

    public InitApplicationTask(Context context) {
        super(context);
    }

    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        mDialog = DialogLoading.newInstance("Please Wait...",
                "EzImgur is firing up some app components and talking to imgur...please wait.");
        try {
            mDialog.show(mFragmentManager, TAG);
        } catch (Exception e){
            //bug in fragment transaction manager compat libary http://code.google.com/p/android/issues/detail?id=23096
        }
    }

    @Override
    public Boolean call() throws Exception {
        //any application specific setup needed to be completed before running app needs to go here.
        EzImageLoader.initImageLoader(getContext());
        Log.d(TAG, "initApplicationBegin");
        if (EzApplication.hasToken()) {
            Log.d(TAG, "force refreshing auth token because of app restart");

            //make sure our access token is good to go...
            AuthenticationToken newToken = mAuthenticationApi.refreshAuthenticationToken(EzApplication.getRefreshToken());

            mAuthenticationApi.setCurrentAuthenticationToken(newToken);
            EzApplication.setAuthenticationToken(getContext(), newToken);
        }

        int version = EzApplication.getInitializedVersion(getContext());

        if (version  == 0){
            Log.d(TAG, "initializing application with seed data...");

            GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());
            dataSource.createSavedGallery("hot");
            dataSource.createSavedGallery("top");
            dataSource.createSavedGallery("user");
            dataSource.createSavedGallery("r/AdviceAnimals");
            dataSource.createSavedGallery("r/aww");
            dataSource.createSavedGallery("r/fffffffuuuuuuuuuuuu");
            dataSource.createSavedGallery("r/food");
            dataSource.createSavedGallery("r/funny");
            dataSource.createSavedGallery("r/gifs");
            dataSource.createSavedGallery("r/pics");
            dataSource.createSavedGallery("r/todayilearned");
            dataSource.createSavedGallery("r/reactiongifs");
            dataSource.createSavedGallery("r/wallpaper");
            dataSource.createSavedGallery("random");


            EzApplication.setHasBeenInitialized(context, 1);
        } else if (version == 1) {
            GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());
            dataSource.deleteSavedGallery("new");
            dataSource.createSavedGallery("user");
            dataSource.createSavedGallery("random");

            EzApplication.setHasBeenInitialized(context, 2);
        } else if (version == 2) {
            GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());

            dataSource.createSavedGallery("random");
            EzApplication.setHasBeenInitialized(context, 3);
        }

        return true;
    }

    @Override
    protected void onFinally() throws RuntimeException {
        super.onFinally();
        mDialog.dismiss();
    }

    @Override
    protected void onSuccess(Boolean result) throws Exception {
        super.onSuccess(result);
        EzApplication.initialized = true;
        mEventManager.fire(new InitApplicationEvent(result));
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);
        Log.e(TAG, "onException", e);
        mEventManager.fire(new InitApplicationEvent(false));
    }


}
