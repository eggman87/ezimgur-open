package com.ezimgur.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.datacontract.*;
import com.ezimgur.view.activity.BaseActivity;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.utils.EzImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.event.EventManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/26/13
 * Time: 10:06 PM
 */
public class CompositeThumbnailAdapter extends BaseAdapter {

    private ImageApi mImageApi;
    protected EventManager mEventManager;
    private HashMap<String, WeakReference<View>> mCachedViews;
    private List<GalleryItemComposite> mImages;
    private ImageLoader mImageLoader;
    private static final String TAG = "EzImgur.ThumbnailsAdpater";

    public CompositeThumbnailAdapter(Context context, List<GalleryItemComposite> images, EventManager eventManager, ImageApi imageApi) {
        mCachedViews = new HashMap<String, WeakReference<View>>();
        mImages = images;
        mEventManager = eventManager;
        mImageApi = imageApi;
        mImageLoader = EzImageLoader.getImageLoaderInstance(context);
    }
    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public GalleryItemComposite getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return empty view if height is zero - hack due to tablet issue where visibility can not be set (see gallery activity).
        if (parent.getLayoutParams().height == 0) {
            WeakReference<View> view = mCachedViews.get("empty_view");
            if (view != null && view.get() != null)
                return  view.get();
            View emptyView = UiBuilder.inflate(parent.getContext(), R.layout.view_thumbnail, null);
            mCachedViews.put("empty_view", new WeakReference<View>(emptyView));
            return emptyView;
        }

        GalleryItem targetImage = mImages.get(position).galleryItem;
        //request does not use convertview performance optimizations, so lets use a weak ref cache to help speed.
        WeakReference<View> cachedView = mCachedViews.get(targetImage.id);


        if (cachedView != null && cachedView.get() != null) {
            return cachedView.get();
        } else
        if (convertView != null ) {
            return convertView;
        } else {
            View v = UiBuilder.inflate(parent.getContext(), R.layout.view_thumbnail, null);
            final ImageView imageView = (ImageView) v.findViewById(R.id.view_img_view_thumbnail);

            boolean isAlbum = targetImage instanceof GalleryAlbum;
            String targetHash = isAlbum ? ((GalleryAlbum)targetImage).cover : targetImage.id;

            Image image = new Image();
            image.id = targetHash;
            image.mimeType = "image/jpeg";

            final String imageUrl = mImageApi.getHttpUrlForImage(image, ImageSize.SMALL_SQUARE);
            mImageLoader.displayImage(imageUrl, imageView, BaseActivity.getDefaultImageLoadingListener(mEventManager));

            mCachedViews.put(targetImage.id, new WeakReference<View>(v));
            return  v;
        }
    }
}
