package com.ezimgur.view.activity;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.data.GalleryManager;
import com.ezimgur.data.SettingsManager;
import com.ezimgur.datacontract.*;
import com.ezimgur.datacontract.Gallery;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.persistance.datasource.GalleryDataSource;
import com.ezimgur.task.GetGalleryTask;
import com.ezimgur.task.InitApplicationTask;
import com.ezimgur.task.LoadCaptionTask;
import com.ezimgur.task.SearchGalleryTask;
import com.ezimgur.view.adapter.CaptionAdapter;
import com.ezimgur.view.adapter.ThumbnailsAdapter;
import com.ezimgur.view.component.VerticalTextView;
import com.ezimgur.view.event.ChangeDaysAgoEvent;
import com.ezimgur.view.event.CommentSubmittedEvent;
import com.ezimgur.view.event.InitApplicationEvent;
import com.ezimgur.view.event.OnSelectImageEvent;
import com.ezimgur.view.fragment.*;
import com.ezimgur.view.utils.ViewUtils;
import com.google.inject.Inject;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends BaseActivity implements DialogChangeGallery.OnChangeGalleryListener {

    private Gallery mCurrentGallery;
    private GalleryItemComposite mCurrentGalleryItem;
    private CaptionAdapter mCaptionAdapter;
    private boolean mSaveSubReddit = false;

    private boolean mIsSearchGallery = false;
    private int mCurrentPage = 1;
    private GallerySort mCurrentSort = GallerySort.TOP;
    private boolean mIsTabletInLandscape;
    private boolean mFullScreenMode;

    @Inject private ImageApi mImageApi;
    @Inject protected EventManager mEventManager;

    @InjectView(R.id.main_vp_thumbnails)android.widget.Gallery mThumbsPager;
    @InjectView(R.id.dw_image)SlidingDrawer mDrawerImage;
    @InjectView(R.id.dw_captions) @Nullable SlidingDrawer mDrawerCaptions;
    @InjectView(R.id.lv_captions_list)ListView mListCaptions;
    @InjectView(R.id.dw_image_title)VerticalTextView mTextImageDrawerTitle;
    @InjectView(R.id.btn_reload_captions) Button mBtnReloadCaptions;
    @InjectView(R.id.main_container) RelativeLayout mMainContainer;
    @InjectView(R.id.dw_captions_content)FrameLayout mCaptionsContainer;
    @InjectView(R.id.banana_for_scale)ImageView mImgBananaForScale;

    @InjectFragment(R.id.frag_image_viewer)ImageViewerFragment mFragmentImageViewer;
    @InjectFragment(R.id.frag_item_details)ItemDetailsFragment mFragmentItemDetails;

    private static final String BUNDLE_STATE_GALLERY = "bundle_state_gallery";
    private static final String BUNDLE_STATE_CURRENT_DAYS = "bundle_state_daysago";
    private static final String BUNDLE_STATE_CURRENT_INDEX = "bundle_state_selection";
    private static final String BUNDLE_STATE_CURRENT_NAME = "bundle_state_name";
    private static final String TAG = "EzImgur.GalleryActivity";

    @Override
    protected void onResume() {
        super.onResume();

        updateViewFromSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //can not inject content view because we are using sliding menu...
        //setContentView(R.layout.screen_main);

        if (savedInstanceState == null)
        {
            isRefreshingToken = true;
            new InitApplicationTask(this).execute();
        }

        attachViewListeners();
        setupAnyHacks();
        mMainContainer.invalidate();

        mIsTabletInLandscape = ViewUtils.isTabletInLandscapeMode(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_main;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && (mDrawerCaptions != null && mDrawerCaptions.isOpened())) {
            mDrawerCaptions.animateClose();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_STATE_GALLERY, mCurrentGallery);
        outState.putInt(BUNDLE_STATE_CURRENT_INDEX, mThumbsPager.getSelectedItemPosition());
        outState.putString(BUNDLE_STATE_CURRENT_NAME, "gallery");
        outState.putInt(BUNDLE_STATE_CURRENT_DAYS, 0);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        int selectedPosition = savedInstanceState.getInt(BUNDLE_STATE_CURRENT_INDEX);

        Gallery savedGallery = savedInstanceState.getParcelable(BUNDLE_STATE_GALLERY);
        if (savedGallery != null) {
            setGallery(savedGallery.galleryName, savedGallery.imageList);
            if (mCurrentGallery != null && mCurrentGallery.imageList != null)  {
                mThumbsPager.setAdapter(new ThumbnailsAdapter(this, mCurrentGallery.imageList, mEventManager, mImageApi));
                mFragmentImageViewer.setImageGallery(mCurrentGallery, selectedPosition);
                mDrawerImage.animateOpen();
                mThumbsPager.setSelection(selectedPosition, true);
                loadGalleryItemComments();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CharSequence title = item.getTitle();
        if (title == MENU_REFRESH)  {
            if (mIsSearchGallery)
                Toast.makeText(this, "you can not refresh a search, change gallery or research", Toast.LENGTH_SHORT).show();
            else
                loadGallery(mCurrentGallery == null ? "hot":mCurrentGallery.galleryName, mCurrentPage, mCurrentSort);
        } else if (title == MENU_CHANGE_GALLERY) {
            changeGallery();
        } else if (title == MENU_CHANGE_DAYSAGO) {
            changeDaysAgo();
        } else if (title == CONTEXT_MENU_COMMENT_ITEM) {
                openItemDetailFragment();
        } else if (title == MENU_RELOAD_COMMENTS) {
            loadGalleryItemComments();
        } else if (title == MENU_SEARCH_GALLERY) {
            if (!item.isActionViewExpanded())
                item.expandActionView();
        } else if (title == MENU_BANANA_FOR_SCALE){
            toggleBananaForScale();
        }

        return super.onOptionsItemSelected(item);
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
                    Toast.makeText(getApplicationContext(), "searching for "+query, Toast.LENGTH_LONG).show();
                    new SearchGalleryTask(getApplicationContext(), query){

                        @Override
                        protected void onSuccess(List<GalleryItem> galleryItems) throws Exception {
                            super.onSuccess(galleryItems);

                            if (galleryItems != null && galleryItems.size() > 0){
                                Toast.makeText(getContext(), galleryItems.size() + " images found!", Toast.LENGTH_LONG).show();
                                searchItem.collapseActionView();
                                setGallery("search - "+query, galleryItems);
                                initAfterGalleryLoad();
                                mIsSearchGallery = true;
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
    public void changeGalleryClicked(String galleryName, boolean saveSubReddit, GallerySort sort, boolean makeDefault) {
        mSaveSubReddit = saveSubReddit;
        loadGallery(galleryName, 0, sort, true, makeDefault);
    }

    private boolean mBananaListnerSet = false;

    private void toggleBananaForScale(){

        mImgBananaForScale.setVisibility(mImgBananaForScale.getVisibility() == View.GONE ? View.VISIBLE:View.GONE);
        mImgBananaForScale.bringToFront();
        if (!mBananaListnerSet) {
            mImgBananaForScale.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    int eid = event.getAction();
                    switch (eid) {
                        case MotionEvent.ACTION_MOVE:

                            RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) mImgBananaForScale.getLayoutParams();
                            int x = (int) event.getRawX();
                            int y = (int) event.getRawY();
                            mParams.leftMargin = x-20;
                            mParams.topMargin = y-20;
                            mImgBananaForScale.setLayoutParams(mParams);


                            break;

                        default:
                            break;
                    }
                    return true;
                }
            });
            mBananaListnerSet = true;
        }
    }

    public void onDaysAgoSelectionChanged(@Observes ChangeDaysAgoEvent event) {
        loadGallery(mCurrentGallery == null ? "hot":mCurrentGallery.galleryName, event.getDaysAgo(), mCurrentSort, true, false);
    }

    public void onCommentSubmitted(@Observes CommentSubmittedEvent event) {
        loadGalleryItemComments();
    }

    public void onApplicationInitialized(@Observes InitApplicationEvent event){
        Log.d(TAG, "application initialized - loading gallery for first time");

        mSaveSubReddit = true;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String galleryName = pref.getString("default_gallery", "hot");
        GallerySort sort = GallerySort.values()[pref.getInt("default_sort", 0)];


        loadGallery(galleryName, 0, sort);
        isRefreshingToken = false;
    }

    public void onImagePositionChanged(@Observes OnSelectImageEvent event) {
        List<Comment> oldComments = new ArrayList<Comment>();
        if (event.isAlbumChange() && mCurrentGalleryItem != null) {
            oldComments = mCurrentGalleryItem.comments;
        }

        mCurrentGalleryItem = new GalleryItemComposite();
        mCurrentGalleryItem.galleryItem = mFragmentImageViewer.getCurrentGalleryItem();

        if (mCurrentGalleryItem.galleryItem.isAlbum)
            mFragmentItemDetails.setAlbumTotalCount(mFragmentImageViewer.getCurrentAlbumCount());
        mFragmentItemDetails.setGalleryItem(mCurrentGalleryItem.galleryItem);

        GalleryAlbum album = null;
        if (mCurrentGalleryItem.galleryItem instanceof GalleryAlbum)
            album = (GalleryAlbum) mCurrentGalleryItem.galleryItem;

        if (event.isAlbumChange() && album != null) {
            mCurrentGalleryItem.comments = oldComments;
        } else {
            setCaptionData();
        }

        //fixing a bug in android gallery widget that fuzzes up selection on it.
        if (!event.isFling())
            mThumbsPager.setSelection(event.getPosition() - (event.getPosition() - mThumbsPager.getSelectedItemPosition()), true);
        else
            mThumbsPager.setSelection(event.getPosition());

        if (mCurrentGalleryItem.galleryItem.isAlbum)
            mTextImageDrawerTitle.setText("ALBUM");
        else
            mTextImageDrawerTitle.setText("IMAGE");
    }

    public void loadGalleryItemCommentsFromButton(View view) {
        if (mBtnReloadCaptions != null)
            mBtnReloadCaptions.setVisibility(View.INVISIBLE);
        loadGalleryItemComments();
    }

    private void changeGallery() {
        DialogChangeGallery changeDialog = DialogChangeGallery.newInstance(
                mCurrentGallery == null ? "hot" : mCurrentGallery.galleryName, mCurrentSort);
        changeDialog.show(getSupportFragmentManager(), TAG);
    }

    private void changeDaysAgo() {
        DialogChangeDays dialogFragment = DialogChangeDays.newInstance(mCurrentPage, mCurrentSort);
        dialogFragment.show(getSupportFragmentManager(), TAG);
    }

    private void loadGallery(final String galleryName,final int pageNumber, GallerySort sort) {
        loadGallery(galleryName, pageNumber, sort, false, false);
    }

    private void loadGallery(final String galleryName,final int pageNumber, GallerySort sort, boolean overrideSort, final boolean makeDefault) {
        Log.d(TAG, String.format("loading %s at page %s with sort %s", galleryName, pageNumber, sort));

        //hack for showing new on subreddits.
        if (galleryName.contains("r/") && !overrideSort)
            sort = GallerySort.TIME;

        final GallerySort finalSort = sort;
        new GetGalleryTask(this, galleryName, pageNumber, sort){
            @Override
            protected void onSuccess(List<GalleryItem> gallery) throws Exception {
                mIsSearchGallery = false;
                super.onSuccess(gallery);

                setGallery(galleryName, gallery);
                Toast.makeText(GalleryActivity.this,
                        String.format("Loaded %s with %s images.",mCurrentGallery.galleryName, mCurrentGallery.imageList.size()),
                        Toast.LENGTH_LONG ).show();

                mCurrentPage = pageNumber;
                mCurrentSort = finalSort;
                initAfterGalleryLoad();

                if (mSaveSubReddit && mCurrentGallery.imageList!= null && mCurrentGallery.imageList.size() > 0) {
                    GalleryDataSource dataSource = GalleryDataSource.newInstance(getContext());
                    dataSource.createSavedGallery(galleryName);
                    mSaveSubReddit = false;
                }

                if (makeDefault){
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GalleryActivity.this);
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

                Toast.makeText(GalleryActivity.this, "unable to talk to imgur. cause: " + fault, Toast.LENGTH_LONG).show();
            }
        }.execute();
    }

    private void setGallery(String galleryName, List<GalleryItem> gallery){
        mCurrentGallery = new Gallery();
        mCurrentGallery.galleryName = galleryName;
        mCurrentGallery.imageList = gallery;
    }

    private ThumbnailsAdapter mThumbsAdapter;
    private void initAfterGalleryLoad() {
        mThumbsAdapter = new ThumbnailsAdapter(this, mCurrentGallery.imageList, mEventManager, mImageApi);
        mThumbsPager.setAdapter(mThumbsAdapter);
        mFragmentImageViewer.setImageGallery(mCurrentGallery);
        if (!mDrawerImage.isOpened() && !mFullScreenMode)
            mDrawerImage.animateOpen();
        setCaptionViewState();
    }

    private void attachViewListeners() {
        mThumbsPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "item clicked @ "+i);
               mFragmentImageViewer.setCurrentImageByPositiion(i);
            }
        });

        attacheCommentLongPressListeners();
    }

    private void setupAnyHacks(){
        setupCaptionViewOverrunHack();
    }

    private void setCaptionData() {
        if (currentGalleryHasComments()) {
            loadGalleryItemComments();
        }
    }

    /*
       Hack for caption listview vertical text view going past listview and not being cleared from
       the image sliding drawer.
    */
    private void setupCaptionViewOverrunHack(){
        AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                mDrawerImage.invalidate();
            }
        };

        mListCaptions.setOnScrollListener(listener);
    }

    private boolean currentGalleryHasComments() {
        return GalleryManager.galleryHasComments(mCurrentGallery.galleryName);
    }

    /*
    Updates which caption list view is shown based on if tablet
    */
    private void setCaptionViewState() {
        if (currentGalleryHasComments()){
            if (mDrawerCaptions != null)
                mDrawerCaptions.setVisibility(View.VISIBLE);
            setCaptionContainerVisibility(View.VISIBLE);
            mListCaptions.setVisibility(View.VISIBLE);

        } else {
            if (mDrawerCaptions != null)
                mDrawerCaptions.setVisibility(View.GONE);
            setCaptionContainerVisibility(View.GONE);
            mListCaptions.setVisibility(View.GONE);
        }
    }

    private void setCaptionContainerVisibility(int view) {
        if (mIsTabletInLandscape)
            mCaptionsContainer.setVisibility(view);
    }

    private void loadGalleryItemComments() {
        mBtnReloadCaptions.setVisibility(View.INVISIBLE);

        if (mCurrentGalleryItem == null)
            return;

        mListCaptions.setVisibility(View.INVISIBLE);
        new LoadCaptionTask(this, mCurrentGalleryItem.galleryItem.id){
            @Override
            protected void onSuccess(List<Comment> comments) throws Exception {
                super.onSuccess(comments);
                mCurrentGalleryItem.comments = comments;

                //imgur api does not return comments sorted by points - we need to sort
                Collections.sort(mCurrentGalleryItem.comments);

                if (mCaptionAdapter == null) {
                    mCaptionAdapter = new CaptionAdapter(mCurrentGalleryItem.galleryItem.id, mCurrentGalleryItem.comments, getSupportFragmentManager());
                    mListCaptions.setAdapter(mCaptionAdapter);
                } else {
                    mCaptionAdapter.setCaptions(mCurrentGalleryItem.galleryItem.id, mCurrentGalleryItem.comments);
                    mCaptionAdapter.notifyDataSetChanged();
                }

                setCaptionViewState();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                String cause = "unknown";
                if (e != null)
                    cause = e.getMessage();
                String fault = cause == null ? "unknown":cause;

                Toast.makeText(GalleryActivity.this, "unable to load comments. cause: " + fault, Toast.LENGTH_LONG).show();
                mBtnReloadCaptions.setVisibility(View.VISIBLE);
            }
        }.execute();
    }


    private void attacheCommentLongPressListeners() {
        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogCommentDetail dialog = DialogCommentDetail.newInstance(mCurrentGalleryItem.galleryItem.id, mCurrentGalleryItem.comments.get(position));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);

                dialog.show(transaction, DialogCommentDetail.TAG);

                return true;
            }
        };

        mListCaptions.setOnItemLongClickListener(listener);
    }

    private void openItemDetailFragment() {
        if (mCurrentGalleryItem == null)
            return;
        DialogGalleryItemDetail dialogFragment = DialogGalleryItemDetail.newInstance(mCurrentGalleryItem.galleryItem, currentGalleryHasComments());
        dialogFragment.show(getSupportFragmentManager(), DialogGalleryItemDetail.TAG);
    }

    private void updateViewFromSettings() {
        /*
        * when making gallery invisible - it causes problems on tablet...have to use height to make invisible which is not ideal and led to hacks in adapter -
        * need to investigate better ways of view management
        */

        SettingsManager manager = new SettingsManager(this);

        mFullScreenMode = manager.getValue(SettingsManager.SETTING_FULL_SCREEN_MODE, false);
        boolean sitBelow = manager.getValue(SettingsManager.SETTING_IMAGE_BELOW, false);

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mFragmentImageViewer.getView().getLayoutParams();

        if (mFullScreenMode) {
            ViewGroup.LayoutParams params = mThumbsPager.getLayoutParams();
            params.height = 0;
            if (!sitBelow)
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            else
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        } else {
            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIsTabletInLandscape ? 80:60, r.getDisplayMetrics());

            ViewGroup.LayoutParams params = mThumbsPager.getLayoutParams();
            params.height = (int) px;
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

            if (mThumbsAdapter != null)
                mThumbsAdapter.notifyDataSetChanged();
        }

        if (sitBelow)
            lp.addRule(RelativeLayout.BELOW, mDrawerImage.getId());
        else
            lp.addRule(RelativeLayout.BELOW, mThumbsPager.getId());

        if (sitBelow && mFullScreenMode && !mDrawerImage.isOpened())
            mDrawerImage.animateOpen();

        mFragmentImageViewer.getView().setLayoutParams(lp);
    }
}
