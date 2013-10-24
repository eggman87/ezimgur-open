package com.ezimgur.view.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ezimgur.R;
import com.ezimgur.api.AuthenticationApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.view.fragment.FragmentPagerAdapterAccount;
import com.google.inject.Inject;
import com.viewpagerindicator.TitlePageIndicator;
import roboguice.inject.InjectView;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 6:11 PM
 */
public class AccountActivity extends BaseActivity {

    private FragmentPagerAdapterAccount mPagerAdapter;

    @Inject AuthenticationApi mAuthenticationApi;
    @InjectView (R.id.account_fragment_pager) ViewPager mAccountPager;
    @InjectView (R.id.account_pager_indicator)TitlePageIndicator mTabIndicator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(EzApplication.getAccountUserName());

        setSupportProgressBarIndeterminateVisibility(false);

        mPagerAdapter = new FragmentPagerAdapterAccount(getSupportFragmentManager());
        mAccountPager.setAdapter(mPagerAdapter);
        mTabIndicator.setViewPager(mAccountPager);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_account;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle().equals("logout")){
            EzApplication.logoutUser(this);
            mAuthenticationApi.clearCurrentAuthenticationToken();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("logout").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
        return super.onCreateOptionsMenu(menu);
    }
}
