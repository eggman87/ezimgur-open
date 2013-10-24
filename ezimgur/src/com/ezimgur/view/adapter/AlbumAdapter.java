package com.ezimgur.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.utils.EzImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 2:11 PM
 */
public class AlbumAdapter extends BaseAdapter {

    private ImageApi mImageApi;
    private List<Album> mAlbums;

    public AlbumAdapter(ImageApi imageApi, List<Album> albums) {
        mAlbums = albums;
        mImageApi = imageApi;
    }

    public void setAlbums(List<Album> albums) {
        mAlbums = albums;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAlbums.size();
    }

    @Override
    public Album getItem(int i) {
        return mAlbums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Context context = viewGroup.getContext();
        View v = UiBuilder.inflate(context, R.layout.view_album_grid_item, null);

        ImageView imgAlbum = (ImageView) v.findViewById(R.id.iv_albumGridItemImage);
        TextView txtAlbumName = (TextView) v.findViewById(R.id.tv_albumGridItemTitle);

        ImageLoader loader = EzImageLoader.getImageLoaderInstance(context);

        Album album = mAlbums.get(i);

        if (album.images != null) {

            if (album.images.size() > 0) {
                loader.displayImage(mImageApi.getHttpUrlForImage(album.images.get(0), ImageSize.SMALL_SQUARE), imgAlbum);
            } else {
                imgAlbum.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
            }
        }

        txtAlbumName.setText(album.title);

        return v;
    }
}
