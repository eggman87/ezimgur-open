package com.ezimgur.view.adapter;

import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.view.builder.UiBuilder;
import com.ezimgur.view.utils.EzImageLoader;
import com.ezimgur.view.utils.ViewUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 10:07 PM
 */
public class AlbumImageAdapter extends BaseAdapter {

    private ImageApi mImageApi;
    private List<Image> mImages;

    public AlbumImageAdapter(ImageApi imageApi, ArrayList<Image> images) {
        mImages = images;
        mImageApi = imageApi;
    }

    public void setImages(List<Image> images) {
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Image getItem(int i) {
        return mImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        final ViewHolder viewHolder;
        if (convertView == null)  {
            convertView = UiBuilder.inflate(viewGroup.getContext(), R.layout.view_album_image, null);

            viewHolder = new ViewHolder();
            viewHolder.imgAlbumImage = (ImageView) convertView.findViewById(R.id.iv_album_image);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.v_ai_tv_image_title);
            viewHolder.txtAuthor = (TextView) convertView.findViewById(R.id.v_ai_tv_image_author);
            viewHolder.txtHash = (TextView) convertView.findViewById(R.id.v_ai_tv_image_hash);
            viewHolder.txtDimensions = (TextView) convertView.findViewById(R.id.v_ai_tv_image_dimensions);
            viewHolder.txtViews = (TextView) convertView.findViewById(R.id.v_ai_tv_image_views);
            viewHolder.txtDateCreated = (TextView) convertView.findViewById(R.id.v_ai_tv_image_date_created);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Image image = mImages.get(i);

        ImageLoader loader = EzImageLoader.getImageLoaderInstance(viewGroup.getContext());
        loader.displayImage(mImageApi.getHttpUrlForImage(image, ImageSize.SMALL_SQUARE), viewHolder.imgAlbumImage);

        viewHolder.txtTitle.setText(image.title);

        if (image.accountUrl != null)
            viewHolder.txtAuthor.setText("posted by " + image.accountUrl);
        else {
            viewHolder.txtAuthor.setText(image.description);
        }
        viewHolder.txtHash.setText(image.id);
        viewHolder.txtDimensions.setText(String.format("%s X %s ", image.width, image.height));
        viewHolder.txtViews.setText(String.format("%s views", ViewUtils.commify(String.valueOf(image.views))));
        viewHolder.txtDateCreated.setText(DateUtils.getRelativeTimeSpanString(image.dateCreated.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));

        return convertView;
    }

    static class ViewHolder {
        ImageView imgAlbumImage;
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtHash;
        TextView txtDimensions;
        TextView txtViews;
        TextView txtDateCreated;
    }
}
