package com.ezimgur.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.GalleryItemComposite;
import com.ezimgur.view.fragment.GalleryItemFragment;

import java.util.List;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:47 PM
 */
public class ImagesViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<GalleryItemComposite> items;

    public ImagesViewPagerAdapter(FragmentManager fm, List<GalleryItemComposite> items) {
        super(fm);
        this.items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return GalleryItemFragment.newInstance(items.get(position));
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
