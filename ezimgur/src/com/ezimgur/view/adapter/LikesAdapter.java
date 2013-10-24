package com.ezimgur.view.adapter;

import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.datacontract.*;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.utils.EzImageLoader;
import com.ezimgur.view.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/29/12
 * Time: 1:34 PM
 */
public class LikesAdapter extends BaseAdapter {

    ImageApi mImageApi;
    private List<GalleryItem> mLikedItems;

    public LikesAdapter(ImageApi imageApi, List<GalleryItem> likedItems) {
        mImageApi = imageApi;
        mLikedItems = likedItems;
    }

    public void setLikedItems(List<GalleryItem> likedItems) {
        mLikedItems = likedItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mLikedItems.size();
    }

    @Override
    public GalleryItem getItem(int i) {
        return mLikedItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = UiBuilder.inflate(viewGroup.getContext(), R.layout.view_liked_item, null);

            viewHolder = new ViewHolder();
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.v_li_tv_title);
            viewHolder.txtAuthor = (TextView) convertView.findViewById(R.id.v_li_tv_author);
            viewHolder.imgThumb = (ImageView) convertView.findViewById(R.id.v_li_image);
            viewHolder.txtHash = (TextView) convertView.findViewById(R.id.v_li_tv_image_hash);
            viewHolder.txtDimensions = (TextView) convertView.findViewById(R.id.v_li_tv_image_dimensions);
            viewHolder.txtViews = (TextView) convertView.findViewById(R.id.v_li_tv_image_views);
            viewHolder.txtDateCreated = (TextView) convertView.findViewById(R.id.v_li_tv_image_date_created);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GalleryItem targetItem = mLikedItems.get(position);

        viewHolder.txtTitle.setText(targetItem.title);
        viewHolder.txtAuthor.setText(targetItem.accountUrl);
        viewHolder.txtHash.setText(targetItem.id);
        viewHolder.txtDateCreated.setText(DateUtils.getRelativeTimeSpanString(targetItem.dateCreated.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        viewHolder.txtViews.setText(ViewUtils.commify(String.valueOf(targetItem.views)));

        boolean isAlbum = targetItem instanceof GalleryAlbum;
        String targetHash = targetItem.id;
        if (isAlbum) {
            GalleryAlbum album = (GalleryAlbum) targetItem;
            viewHolder.txtDimensions.setText(album.imageCount + "images");
            targetHash = album.cover;
        } else {
            GalleryImage galleyImage = (GalleryImage) targetItem;
            viewHolder.txtDimensions.setText(String.format("%s X %s", galleyImage.width, galleyImage.height));
        }

        Image image = new Image();
        image.id = targetHash;
        image.mimeType = "image/jpeg";

        String url = mImageApi.getHttpUrlForImage(image, ImageSize.SMALL_SQUARE);
        ImageLoader loader = EzImageLoader.getImageLoaderInstance(viewGroup.getContext());
        loader.displayImage(url, viewHolder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtHash;
        TextView txtAuthor;
        TextView txtDimensions;
        TextView txtDateCreated;
        TextView txtViews;
        ImageView imgThumb;
    }
}
