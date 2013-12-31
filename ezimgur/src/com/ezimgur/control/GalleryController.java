package com.ezimgur.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import com.ezimgur.datacontract.*;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.persistance.datasource.GalleryDataSource;
import com.ezimgur.task.GetGalleryTask;
import com.ezimgur.task.LoadCaptionTask;
import com.ezimgur.view.adapter.CaptionAdapter;
import com.ezimgur.view.event.GalleryCaptionLoadEvent;
import com.ezimgur.view.event.GalleryLoadEvent;
import roboguice.event.EventManager;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:55 PM
 */
public class GalleryController implements IGalleryController {

    private EventManager manager;

    private int currentPage;
    private GallerySort currentSort;
    private boolean isSearchGallery;
    private Gallery currentGallery;

    private Context context;
    private static final String TAG = "EzImgur.GalleryController";

    @Override
    public void onCreate(Context context, EventManager manager) {
        this.context = context;
        this.manager = manager;
    }

    @Override
    public void onResume(Context context) {
        this.context = context;
    }

    @Override
    public void onPause(Context context) {
        this.context = null;
    }

    public void loadGallery(final String galleryName,final int pageNumber, GallerySort sort) {
        loadGallery(galleryName, pageNumber, sort, false, false, false);
    }

    public void loadGallery(final String galleryName,final int pageNumber, GallerySort sort, boolean overrideSort, final boolean makeDefault, final boolean saveSubReddit) {
        Log.d(TAG, String.format("loading %s at page %s with sort %s", galleryName, pageNumber, sort));

        //hack for showing new on subreddits.
        if (galleryName.contains("r/") && !overrideSort)
            sort = GallerySort.TIME;

        final GallerySort finalSort = sort;
        new GetGalleryTask(context, galleryName, pageNumber, sort){
            @Override
            protected void onSuccess(List<GalleryItem> gallery) throws Exception {
                if (this.context == null)
                    return;

                isSearchGallery = false;
                super.onSuccess(gallery);
                currentGallery = new Gallery();
                currentGallery.galleryName = galleryName;
                currentGallery.imageList = gallery;

                mEventManager.fire(new GalleryLoadEvent(galleryName, gallery));
                Toast.makeText(context,
                        String.format("Loaded %s with %s images.", currentGallery.galleryName, currentGallery.imageList.size()),
                        Toast.LENGTH_LONG).show();

                currentPage = pageNumber;
                currentSort = finalSort;
                //initAfterGalleryLoad();

                if (saveSubReddit && currentGallery.imageList!= null && currentGallery.imageList.size() > 0) {
                    GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());
                    dataSource.createSavedGallery(galleryName);
                }

                if (makeDefault){
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.context);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("default_gallery", galleryName);
                    edit.putInt("default_sort", finalSort.ordinal());
                    edit.commit();
                }
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                String cause = "unknown";
                if (e != null)
                    cause = e.getMessage();
                String fault = cause == null ? "unknown":cause;

                Toast.makeText(context, "unable to talk to imgur. cause: " + fault, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }


    public void loadCaptions(final GalleryItemComposite item, final int targetPosition) {

        new LoadCaptionTask(context, item.galleryItem.id){
            @Override
            protected void onSuccess(List<Comment> comments) throws Exception {
                super.onSuccess(comments);
                item.comments = comments;

                //imgur api does not return comments sorted by points - we need to sort
                Collections.sort(item.comments);

                mEventManager.fire(new GalleryCaptionLoadEvent(item, true, targetPosition));
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                String cause = "unknown";
                if (e != null)
                    cause = e.getMessage();
                String fault = cause == null ? "unknown":cause;

                mEventManager.fire(new GalleryCaptionLoadEvent(item, false, targetPosition));
                Toast.makeText(context, "unable to load comments. cause: " + fault, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }
}
