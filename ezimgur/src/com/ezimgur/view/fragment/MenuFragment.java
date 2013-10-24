package com.ezimgur.view.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Account;
import com.ezimgur.view.activity.*;
import com.ezimgur.view.adapter.MenuAdapter;
import com.ezimgur.view.component.EzMenuItem;
import com.ezimgur.view.event.MenuSelectEvent;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import roboguice.event.EventManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/23/12
 * Time: 11:38 PM
 */
public class MenuFragment extends RoboSherlockFragment {

    @Inject protected EventManager mEventManager;
    private TextView mTextLoggedIn;
    private LinearLayout mLoginContainer;

    private static final String TAG = "EzImgur.MenuFragment";

    public static  MenuFragment  instance (String parentSimpleClassName) {
        MenuFragment fragment = new MenuFragment();

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextLoggedIn != null) {
           setLoginText();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        ListView listItems = (ListView) view.findViewById(R.id.listMenuItems);
        mLoginContainer = (LinearLayout) view.findViewById(R.id.menu_account_container);
        mTextLoggedIn = (TextView) view.findViewById(R.id.tv_menu_account_loggedin);
        ImageView imgIcon = (ImageView) view.findViewById(R.id.menu_zebgur_icon);
        setLoginText();

        Drawable icon = getResources().getDrawable(R.drawable.zebgur_mascot_large);
        icon.setAlpha(40);
        imgIcon.setImageDrawable(icon);

        final List<EzMenuItem> menuItems = new ArrayList<EzMenuItem>(6);
        Activity currentAct = getActivity();

        menuItems.add(new EzMenuItem(1, "GALLERY", currentAct instanceof GalleryActivity, GalleryActivity.class));
        menuItems.add(new EzMenuItem(2, "UPLOAD", currentAct instanceof UploadActivity, UploadActivity.class));
        menuItems.add(new EzMenuItem(3, "MY IMAGES", currentAct instanceof MyImagesActivity || currentAct instanceof AlbumImagesActivity || currentAct instanceof ViewItemActivity, MyImagesActivity.class));
        menuItems.add(new EzMenuItem(4, "COMMUNITY", currentAct instanceof CommunityActivity, CommunityActivity.class));
        menuItems.add(new EzMenuItem(5, "SETTINGS", currentAct instanceof SettingsActivity, SettingsActivity.class));
        menuItems.add(new EzMenuItem(6, "ABOUT", currentAct instanceof AboutActivity, AboutActivity.class));

        listItems.setAdapter(new MenuAdapter(menuItems));

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EzMenuItem menuItem = menuItems.get(i);
                if (!menuItem.selected)
                    mEventManager.fire(new MenuSelectEvent(menuItem.activityToLaunch));
            }
        });

        return view;
    }

    private void setLoginText() {
        if (EzApplication.hasToken()) {
            Account account = EzApplication.getAccount();
            String userName = (account == null) ? "someone":account.url;
            mTextLoggedIn.setText(userName);

            mLoginContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    DialogUserProfile dialogFragment = DialogUserProfile.newInstance();
//                    dialogFragment.show(getFragmentManager(), TAG);
                    mEventManager.fire(new MenuSelectEvent(AccountActivity.class));
                }
            });
        }  else {
            mTextLoggedIn.setText(R.string.menuNotLoggedIn);
            mLoginContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (EzApplication.isAuthenticated()){

                    } else {
                        mEventManager.fire(new MenuSelectEvent(LoginActivity.class));
                    }

                }
            });
        }
    }
}
