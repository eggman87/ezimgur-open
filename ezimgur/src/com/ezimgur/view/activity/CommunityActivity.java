package com.ezimgur.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.view.event.OpenMessageDetailEvent;
import com.ezimgur.view.fragment.CommunityFragmentAdapter;
import com.viewpagerindicator.TitlePageIndicator;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 8:52 PM
 */
public class CommunityActivity extends BaseActivity {

    @InjectView(R.id.comm_tpi)TitlePageIndicator mTitleIndicator;
    @InjectView(R.id.comm_vp)ViewPager mViewPager;

    private CommunityFragmentAdapter mFragmentAdapter;

    private int mCurrentPage = 0;

    public static final String EXTRA_COMPOSE_TO = "extra_compose_to";
    private static final String MENU_NEW_MESSAGE = "Create New Message";

    @Override
    public void onBackPressed() {
        if (!mFragmentAdapter.onBackPressed(mViewPager.getCurrentItem()))
            super.onBackPressed();
    }

    public void onOpenMessageDetailEvent(@Observes OpenMessageDetailEvent event){
        mFragmentAdapter.goToMessageDetail(event.messageId);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportProgressBarIndeterminateVisibility(false);

        mFragmentAdapter = new CommunityFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentAdapter);
        mTitleIndicator.setViewPager(mViewPager);

        mTitleIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != mCurrentPage){
                    mCurrentPage = position;
                    invalidateOptionsMenu();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_community;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            String composeTo = extras.getString(EXTRA_COMPOSE_TO);
            mFragmentAdapter.composeMessage(composeTo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mCurrentPage == 0){
            menu.add(MENU_NEW_MESSAGE)
                    .setIcon(android.R.drawable.ic_menu_add)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals(MENU_NEW_MESSAGE)){
            if (!EzApplication.hasToken()) {
                Toast.makeText(this, "you need to login to create a new message", Toast.LENGTH_SHORT).show();
            } else {
                mFragmentAdapter.goToMessageDetail(0);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
