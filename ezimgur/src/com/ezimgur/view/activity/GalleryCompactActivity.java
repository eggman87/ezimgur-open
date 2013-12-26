package com.ezimgur.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import com.ezimgur.R;
import com.ezimgur.control.IGalleryController;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.GalleryItemComposite;
import com.ezimgur.datacontract.GallerySort;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.InitApplicationTask;
import com.ezimgur.view.adapter.CaptionAdapter;
import com.ezimgur.view.adapter.ImagesViewPagerAdapter;
import com.ezimgur.view.event.GalleryCaptionLoadEvent;
import com.ezimgur.view.event.GalleryLoadEvent;
import com.ezimgur.view.event.InitApplicationEvent;
import com.ezimgur.view.fragment.ItemDetailsFragment;
import roboguice.event.Observes;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:32 PM
 */
public class GalleryCompactActivity extends BaseActivity {

    @Inject IGalleryController controller;
    @InjectView(R.id.screen_gallery_compact_vp_images) ViewPager viewPager;
    @InjectView(R.id.screen_gallery_compact_lv_captions) ListView listCaptions;
    @InjectFragment(R.id.screen_gallery_compact_frag_item_details)ItemDetailsFragment detailsFragment;

    private String currentGallery;
    private List<GalleryItemComposite> composites;
    private int currentPosition;
    private int currentDays;

    private static final String BUNDLE_STATE_GALLERY = "bundle_state_gallery";
    private static final String BUNDLE_STATE_CURRENT_DAYS = "bundle_state_daysago";
    private static final String BUNDLE_STATE_CURRENT_INDEX = "bundle_state_selection";
    private static final String BUNDLE_STATE_CURRENT_NAME = "bundle_state_name";
    private static final String TAG = "EzImgur.GalleryCompactActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        controller.onCreate(this, eventManager);

        //init application before loading the gallery (validates our oauth token and such)...
        if (savedInstanceState == null)
        {
            isRefreshingToken = true;
            new InitApplicationTask(this).execute();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_gallery_compact;
    }

    @Override
    protected void onResume() {
        super.onResume();

        controller.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        controller.onPause(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(BUNDLE_STATE_GALLERY, (ArrayList<GalleryItemComposite>) composites);
        outState.putInt(BUNDLE_STATE_CURRENT_INDEX, currentPosition);
        outState.putString(BUNDLE_STATE_CURRENT_NAME, currentGallery);
        outState.putInt(BUNDLE_STATE_CURRENT_DAYS, currentDays);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        composites = savedInstanceState.getParcelableArrayList(BUNDLE_STATE_GALLERY);
        currentPosition = savedInstanceState.getInt(BUNDLE_STATE_CURRENT_INDEX);
        currentGallery = savedInstanceState.getString(BUNDLE_STATE_CURRENT_NAME);
        currentDays = savedInstanceState.getInt(BUNDLE_STATE_CURRENT_DAYS);

        onCompositesReady();
        getSupportActionBar().setTitle(currentGallery);
        controller.loadCaptions(composites.get(currentPosition), currentPosition);
        detailsFragment.setGalleryItem(composites.get(currentPosition).galleryItem);
    }

    public void onApplicationInitialized(@Observes InitApplicationEvent event){
        Log.d(TAG, "application initialized - loading gallery for first time");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String galleryName = pref.getString("default_gallery", "hot");
        GallerySort sort = GallerySort.values()[pref.getInt("default_sort", 0)];


        controller.loadGallery(galleryName, 0, sort);
        isRefreshingToken = false;
    }


    public void onGalleryeLoad(@Observes GalleryLoadEvent event) {
        currentGallery = event.galleryName;
        getSupportActionBar().setTitle(currentGallery);


        List<GalleryItem> loadedItems = event.galleryItems;
        composites = new ArrayList<GalleryItemComposite>(loadedItems.size());
        for (GalleryItem item : loadedItems) {
            GalleryItemComposite composite = new GalleryItemComposite();
            composite.galleryItem = item;
            composites.add(composite);
        }

        onCompositesReady();
    }

    public void onCaptionsLoaded(@Observes GalleryCaptionLoadEvent event) {
        if (event.success) {
            if (event.targetPostition == viewPager.getCurrentItem()) {
                GalleryItemComposite target = event.composite;
                listCaptions.setAdapter(new CaptionAdapter(target.galleryItem.id, target.comments, getSupportFragmentManager()));
            }
        }
    }

    private void onCompositesReady() {
        //load first set of captions
        controller.loadCaptions(composites.get(0), 0);
        detailsFragment.setGalleryItem(composites.get(0).galleryItem);

        final ImagesViewPagerAdapter adapter = new ImagesViewPagerAdapter(getSupportFragmentManager(), composites);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                controller.loadCaptions(composites.get(position), position);
                detailsFragment.setGalleryItem(composites.get(position).galleryItem);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
