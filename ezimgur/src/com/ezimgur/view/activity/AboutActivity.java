package com.ezimgur.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.data.AboutItem;
import com.ezimgur.view.builder.UiBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 8:53 PM
 */
public class AboutActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout container = (LinearLayout) findViewById(R.id.scr_about_content_container);

        List<AboutItem> aboutItems = new ArrayList<AboutItem>();
        aboutItems.add(new AboutItem(getResources().getString(R.string.about_intro_title), getResources().getString(R.string.about_intro_body)));
        aboutItems.add(new AboutItem(getResources().getString(R.string.about_protips_title),getResources().getString(R.string.about_protipps_body)));
        aboutItems.add(new AboutItem(getResources().getString(R.string.about_contributors_title), getResources().getString(R.string.about_contributors_body)));
        aboutItems.add(new AboutItem(getResources().getString(R.string.about_credits_title), getResources().getString(R.string.about_credits_body)));
        aboutItems.add(new AboutItem(getResources().getString(R.string.about_disclaimer_title), getResources().getString(R.string.about_disclaimer_body)));

        for (AboutItem item : aboutItems){
            container.addView(getView(item));
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_about;
    }


    private View getView(AboutItem aboutItem) {
        final View view = UiBuilder.inflate(this, R.layout.view_about_item, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        TextView title = (TextView) view.findViewById(R.id.view_about_title);
        TextView body = (TextView) view.findViewById(R.id.view_about_body);

        title.setText(aboutItem.getTitle());
        body.setText(aboutItem.getBody());

        return view;
    }
}
