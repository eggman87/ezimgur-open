package com.ezimgur.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.component.EzMenuItem;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/28/12
 * Time: 9:37 PM
 */
public class MenuAdapter extends BaseAdapter {

    private List<EzMenuItem> mMenuItems;

    public MenuAdapter(List<EzMenuItem> menuItems) {
        mMenuItems = menuItems;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public EzMenuItem getItem(int i) {
        return mMenuItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mMenuItems.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = UiBuilder.inflate(viewGroup.getContext(), R.layout.view_menu_item, null);

        EzMenuItem menuItem = mMenuItems.get(i);

        TextView txtMenuTitle = (TextView) v.findViewById(R.id.tv_menu_item);
        txtMenuTitle.setText(menuItem.title);

        if (menuItem.selected) {
            //v.setBackgroundColor(viewGroup.getResources().getColor(R.color.Black));
            txtMenuTitle.setTextColor(viewGroup.getResources().getColor(R.color.ezimgur_red));
        }


        return v;
    }
}
