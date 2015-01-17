package com.ezimgur.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.AuthenticationToken;
import com.ezimgur.task.RefreshTokenTask;
import com.ezimgur.view.event.MenuSelectEvent;
import com.ezimgur.view.event.OnTaskLoadEvent;
import com.ezimgur.view.fragment.MenuFragment;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 9/30/12
 * Time: 7:47 PM
 */
public abstract class BaseActivity extends RoboSherlockFragmentActivity {

    public static final String MENU_REFRESH = "reload gallery";
    public static final String MENU_CHANGE_GALLERY = "change gallery";
    public static final String MENU_CHANGE_DAYSAGO = "change days ago";
    public static final String MENU_RELOAD_COMMENTS = "reload comments";
    public static final String MENU_SEARCH_GALLERY = "search";
    public static final String MENU_SHARE_IMAGE = "Share Image";
    public static final String MENU_BANANA_FOR_SCALE = "banana for scale";

    public static final String CONTEXT_MENU_COPY = "copy image url";
    public static final String CONTEXT_MENU_SHARE = "share Image";
    public static final String CONTEXT_MENU_SAVE = "save Image";
    public static final String CONTEXT_MENU_FAV_IMAGE = "add to favorites";
    public static final String CONTEXT_MENU_OPEN_IN_WEB = "view fullsize";
    public static final String CONTEXT_MENU_COMMENT_ITEM = "comment";
    public static final String CONTEXT_MENU_REDDIT_COMMENTS = "view reddit comments";
    public static final String CONTEXT_MENU_IMGUR_PAGE = "view imgur page";
    public static final String CONTEXT_MENU_COPY_ALBUM_URL = "copy album url";
    public static final String CONTEXT_MENU_COPY_PAGE_URL = "copy imgur page url";
    public static final String CONTEXT_MENU_VIEW_LARGE_IMAGE = "open unscaled image";

    private static final String TAG = "EzImgur.BaseActivity";

    private static ImageLoadingListener sImageLoadingListener;
    protected boolean isRefreshingToken;

    @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @InjectView(R.id.left_drawer)LinearLayout mDrawerMenuLayout;
    private FrameLayout mContentFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        View baseView = View.inflate(this, R.layout.screen_base, null);
        mContentFrame = (FrameLayout) baseView.findViewById(R.id.content_frame);
        View.inflate(this, getContentViewId(), mContentFrame);

        setContentView(baseView);

        MenuFragment menuFragment = new MenuFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.left_drawer, menuFragment);
        transaction.commit();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close)
        {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle("ezimgur");
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("menu");
            }

        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    protected abstract int getContentViewId();

    @Override
    protected void onResume() {
        super.onResume();

        refreshAuthenticationTokenIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (mDrawerLayout.isDrawerOpen(mDrawerMenuLayout))
                mDrawerLayout.closeDrawer(mDrawerMenuLayout);
            else
                mDrawerLayout.openDrawer(mDrawerMenuLayout);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected boolean useActionBarProgress() {
        return true;
    }

    private int mLoadingCount = 0;
    public synchronized void onLoadObserved(@Observes OnTaskLoadEvent event) {
        if (!useActionBarProgress()){
            return;
        }

        if (event.getType().equals(OnTaskLoadEvent.TaskLoading.LOAD_STARTED)) {
            if (mLoadingCount == 0)
                setSupportProgressBarIndeterminateVisibility(true);
            mLoadingCount ++;
        } else if (event.getType().equals(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED)
                || event.getType().equals(OnTaskLoadEvent.TaskLoading.LOAD_CANCELED)) {
            mLoadingCount --;
        }
        if (mLoadingCount == 0) {
            setSupportProgressBarIndeterminateVisibility(false);
        }
    }

    public void onMenuSelect(@Observes final MenuSelectEvent event) {
        mDrawerLayout.closeDrawer(mDrawerMenuLayout);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), event.getActivityToLaunch());
                startActivity(intent);
            }
        }, 500);
    }

    public ImageLoadingListener getMainImageLoadingListener(final EventManager manager) {
        return getDefaultImageLoadingListener(manager);
    }

    public static ImageLoadingListener getDefaultImageLoadingListener(final EventManager manager) {
        if (sImageLoadingListener == null) {
            sImageLoadingListener = new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                    manager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_STARTED));
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    manager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED));
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    manager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED));
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                    manager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED));
                }
            };
        }
        return sImageLoadingListener;
    }

    protected void refreshAuthenticationTokenIfNeeded(){

        if (EzApplication.hasToken() && !EzApplication.isAuthenticated() && !isRefreshingToken) {
            isRefreshingToken = true;
            new RefreshTokenTask(this, EzApplication.getRefreshToken()) {

                @Override
                protected void onSuccess(AuthenticationToken authenticationToken) throws Exception {
                    super.onSuccess(authenticationToken);

                    EzApplication.setAuthenticationToken(getContext(), authenticationToken);
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    super.onFinally();
                    isRefreshingToken = false;
                }
            }.execute();
        }
    }
}
