package com.ezimgur.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ezimgur.R;
import com.ezimgur.control.IGalleryController;
import com.ezimgur.data.GalleryManager;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.GalleryItemComposite;
import com.ezimgur.datacontract.GallerySort;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.persistance.datasource.GalleryDataSource;
import com.ezimgur.task.InitApplicationTask;
import com.ezimgur.task.SearchGalleryTask;
import com.ezimgur.view.adapter.CaptionAdapter;
import com.ezimgur.view.adapter.ImagesViewPagerAdapter;
import com.ezimgur.view.event.*;
import com.ezimgur.view.fragment.*;
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
public class GalleryCompactActivity extends BaseActivity implements DialogChangeGallery.OnChangeGalleryListener {

    @Inject IGalleryController controller;
    @InjectView(R.id.screen_gallery_compact_vp_images) ViewPager viewPager;
    @InjectView(R.id.screen_gallery_compact_lv_captions) ListView listCaptions;
    @InjectView(R.id.banana_for_scale)ImageView imgBananaForScale;
    @InjectFragment(R.id.screen_gallery_compact_frag_item_details)ItemDetailsFragment detailsFragment;

    private String currentGallery;
    private List<GalleryItemComposite> composites;
    private GallerySort currentSort = GallerySort.TOP;
    private int currentPosition;
    private int currentDays;
    private boolean isSearchGallery;
    private boolean isBananaListenerSet;
    private boolean saveSubReddit;

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

        if (composites != null && composites.size() > 0) {
            onCompositesReady();
            getSupportActionBar().setTitle(currentGallery);
            controller.loadCaptions(composites.get(currentPosition), currentPosition);
            detailsFragment.setGalleryItem(composites.get(currentPosition).galleryItem);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        menu.add(MENU_REFRESH)
                .setIcon(android.R.drawable.ic_popup_sync);
        menu.add(CONTEXT_MENU_COMMENT_ITEM)
                .setIcon(android.R.drawable.ic_menu_edit)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(MENU_CHANGE_GALLERY)
                .setIcon(android.R.drawable.ic_menu_gallery)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(MENU_CHANGE_DAYSAGO)
                .setIcon(android.R.drawable.ic_menu_today)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(MENU_BANANA_FOR_SCALE)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(MENU_RELOAD_COMMENTS)
                .setIcon(android.R.drawable.ic_popup_sync);
        com.actionbarsherlock.widget.SearchView searchView = new com.actionbarsherlock.widget.SearchView(getSupportActionBar().getThemedContext());
        final MenuItem searchItem = menu.add(MENU_SEARCH_GALLERY);
        searchItem.setIcon(android.R.drawable.ic_menu_search)
                .setActionView(searchView)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        searchView.setQueryHint("search gallery…");
        searchView.setOnQueryTextListener(new com.actionbarsherlock.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Toast.makeText(getApplicationContext(), "searching for " + query, Toast.LENGTH_LONG).show();
                new SearchGalleryTask(getApplicationContext(), query){

                    @Override
                    protected void onSuccess(List<GalleryItem> galleryItems) throws Exception {
                        super.onSuccess(galleryItems);

                        if (galleryItems != null && galleryItems.size() > 0){
                            Toast.makeText(getContext(), galleryItems.size() + " images found!", Toast.LENGTH_LONG).show();
                            searchItem.collapseActionView();

                            setItemsToComposites(galleryItems);
                            currentGallery = "search - " + query;
                            getSupportActionBar().setTitle(currentGallery);
                            isSearchGallery = true;
                            onCompositesReady();

                            if (saveSubReddit ) {
                                GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());
                                dataSource.createSavedGallery(currentGallery);
                                saveSubReddit = false;
                            }

                        } else {
                            Toast.makeText(getContext(), "No images found for search. Please try other terms.", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence title = item.getTitle();
        if (title == MENU_REFRESH)  {
            if (isSearchGallery)
                Toast.makeText(this, "you can not refresh a search, change gallery or research", Toast.LENGTH_SHORT).show();
            else
               controller.loadGallery(currentGallery == null ? "hot" : currentGallery, currentDays, currentSort);
        } else if (title == MENU_CHANGE_GALLERY) {
            changeGallery();
        } else if (title == MENU_CHANGE_DAYSAGO) {
            changeDaysAgo();
        } else if (title == CONTEXT_MENU_COMMENT_ITEM) {
            openItemDetailFragment();
        } else if (title == MENU_RELOAD_COMMENTS) {
            controller.loadCaptions(composites.get(currentPosition), currentPosition);
        } else if (title == MENU_SEARCH_GALLERY) {
            if (!item.isActionViewExpanded())
                item.expandActionView();
        } else if (title == MENU_BANANA_FOR_SCALE){
            toggleBananaForScale();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onApplicationInitialized(@Observes InitApplicationEvent event){
        Log.d(TAG, "application initialized - loading gallery for first time");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String galleryName = pref.getString("default_gallery", "hot");
        GallerySort sort = GallerySort.values()[pref.getInt("default_sort", 0)];

        currentSort = sort;
        currentDays = 0;

        controller.loadGallery(galleryName, 0, sort);
        isRefreshingToken = false;
    }

    public void onGalleryeLoad(@Observes GalleryLoadEvent event) {
        currentGallery = event.galleryName;
        getSupportActionBar().setTitle(currentGallery);
        setItemsToComposites(event.galleryItems);
        onCompositesReady();
    }

    private void setItemsToComposites(List<GalleryItem> items) {
        composites = new ArrayList<GalleryItemComposite>(items.size());
        for (GalleryItem item : items) {
            GalleryItemComposite composite = new GalleryItemComposite();
            composite.galleryItem = item;
            composites.add(composite);
        }
    }

    public void onCaptionsLoaded(@Observes GalleryCaptionLoadEvent event) {
        if (event.success) {
            if (event.targetPostition == viewPager.getCurrentItem()) {
                GalleryItemComposite target = event.composite;
                listCaptions.setAdapter(new CaptionAdapter(target.galleryItem.id, target.comments, getSupportFragmentManager()));
                listCaptions.setOnItemLongClickListener(captionLongPressListener);
            }
        }
    }

    AdapterView.OnItemLongClickListener captionLongPressListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            DialogCommentDetail dialog = DialogCommentDetail.newInstance(composites.get(currentPosition).galleryItem.id, composites.get(currentPosition).comments.get(position));

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);

            dialog.show(transaction, DialogCommentDetail.TAG);

            return true;
        }
    };

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

    private void openItemDetailFragment() {
        if (composites == null)
            return;
        DialogGalleryItemDetail dialogFragment = DialogGalleryItemDetail.newInstance(composites.get(currentPosition).galleryItem, currentGalleryHasComments());
        dialogFragment.show(getSupportFragmentManager(), DialogGalleryItemDetail.TAG);
    }

    private boolean currentGalleryHasComments() {
        return GalleryManager.galleryHasComments(currentGallery);
    }

    private void changeDaysAgo() {
        DialogChangeDays dialogFragment = DialogChangeDays.newInstance(currentDays, currentSort);
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

    private void changeGallery() {
        DialogChangeGallery changeDialog = DialogChangeGallery.newInstance(
                currentGallery == null ? "hot" : currentGallery, currentSort);
        changeDialog.show(getSupportFragmentManager(), TAG);
    }

    private void toggleBananaForScale(){

        imgBananaForScale.setVisibility(imgBananaForScale.getVisibility() == View.GONE ? View.VISIBLE:View.GONE);
        imgBananaForScale.bringToFront();
        if (!isBananaListenerSet) {
            imgBananaForScale.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int eid = event.getAction();
                    switch (eid) {
                        case MotionEvent.ACTION_MOVE:

                            RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) imgBananaForScale.getLayoutParams();
                            int x = (int) event.getRawX();
                            int y = (int) event.getRawY();
                            mParams.leftMargin = x-20;
                            mParams.topMargin = y-20;
                            imgBananaForScale.setLayoutParams(mParams);


                            break;

                        default:
                            break;
                    }
                    return true;
                }
            });
            isBananaListenerSet = true;
        }
    }

    @Override
    public void changeGalleryClicked(String galleryName, boolean saveSubReddit, GallerySort sort, boolean makeDefault) {
        this.saveSubReddit = saveSubReddit;
        currentSort = sort;
        controller.loadGallery(galleryName, 0, sort, true, makeDefault);
    }

    public void onDaysAgoSelectionChanged(@Observes ChangeDaysAgoEvent event) {
        controller.loadGallery(currentGallery == null ? "hot" : currentGallery, event.getDaysAgo(), currentSort, true, false);
    }

    public void onCommentSubmitted(@Observes CommentSubmittedEvent event) {
        controller.loadCaptions(composites.get(currentPosition), currentPosition);
    }
}
