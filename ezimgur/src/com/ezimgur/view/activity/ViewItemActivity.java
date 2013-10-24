package com.ezimgur.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import com.ezimgur.R;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.Image;
import com.ezimgur.task.GetImageTask;
import com.ezimgur.task.LoadAlbumImagesTask;
import com.ezimgur.task.LoadAlbumTask;
import com.ezimgur.task.LoadCaptionTask;
import com.ezimgur.view.adapter.CaptionAdapter;
import com.ezimgur.view.event.CommentSubmittedEvent;
import com.ezimgur.view.fragment.DialogCommentDetail;
import com.ezimgur.view.fragment.ImageViewerFragment;
import com.ezimgur.view.fragment.ItemDetailsFragment;
import roboguice.event.Observes;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/5/13
 * Time: 5:40 PM
 */
public class ViewItemActivity extends BaseActivity {

    public static final String BUNDLE_ITEM_ID = "vi_act_item_id";
    public static final String BUNDLE_ITEM_TYPE = "vi_act_item_type";
    public static final String BUNDLE_ITEM_HAS_COMMENTS = "vi_act_item_has_comments";
    public static final String BUNDLE_ITEM_CONTAINER = "vi_act_item_container";

    @InjectFragment(R.id.frag_image_viewer)ImageViewerFragment mFragmentImageViewer;
    @InjectFragment(R.id.frag_item_details)ItemDetailsFragment mItemDetails;
    @InjectView (R.id.lv_captions_list)ListView mListCaptions;
    @InjectView(R.id.dw_captions) @Nullable SlidingDrawer mDrawerCaptions;

    private String mItemId;
    private ItemType mItemType;
    private boolean mHasComments;
    private String itemContainer;

    private CaptionAdapter mCaptionAdapter;

    private static final String TAG = "EzImgur.ViewItemActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null) {
            mItemId = intentBundle.getString(BUNDLE_ITEM_ID);
            mItemType = ItemType.values()[intentBundle.getInt(BUNDLE_ITEM_TYPE)];
            mHasComments = intentBundle.getBoolean(BUNDLE_ITEM_HAS_COMMENTS);
        }

        loadItemDetailsFromNetwork();
        getSupportActionBar().setTitle(mItemId);
        mItemDetails.setGalleryItemLoadedListener(getItemLoadedListener());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_view_item;
    }


    private void loadItemDetailsFromNetwork() {
        switch (mItemType) {
            case ALBUM:
                loadAlbumDetails();
                break;
            case IMAGE:
                loadImageDetails();
                break;
        }
    }

    private void loadAlbumDetails() {
        new LoadAlbumTask(this, mItemId) {
            @Override
            protected void onSuccess(final Album album) throws Exception {
                super.onSuccess(album);

                getSupportActionBar().setTitle("viewing album "+album.id);
                new LoadAlbumImagesTask(getContext(), 1, 100, album.id) {
                    @Override
                    protected void onSuccess(List<Image> images) throws Exception {
                        super.onSuccess(images);
                        album.images = images;
                        mFragmentImageViewer.setCurrentAlbum(album);
                        mFragmentImageViewer.setSingleItemHasComments(mHasComments);
                        checkLoadComments();

                        mItemDetails.setCurrentAlbum(album);
                    }
                }.execute();
            }
        }.execute();

    }

    private void loadImageDetails() {
        new GetImageTask(this, mItemId) {

            @Override
            protected void onSuccess(Image image) throws Exception {
                super.onSuccess(image);
                getSupportActionBar().setTitle("viewing image " + image.id);
                mFragmentImageViewer.setCurrentImage(image);
                mFragmentImageViewer.setSingleItemHasComments(mHasComments);
                checkLoadComments();
                mItemDetails.setCurrentImage(image);
            }
        }.execute();
    }

    private void checkLoadComments() {
        if (mHasComments)
            loadCommentsForItem();
        else
            setDrawerCaptionsVisibiliy(View.GONE);
    }

    private void setDrawerCaptionsVisibiliy(int visibiliy) {
        if (mDrawerCaptions != null)
            mDrawerCaptions.setVisibility(visibiliy);
    }

    private void loadCommentsForItem() {
        new LoadCaptionTask(this, mItemId) {
            @Override
            protected void onSuccess(List<Comment> comments) throws Exception {
                super.onSuccess(comments);
                setCaptions(comments);
            }
        }.execute();
    }

    private void setCaptions(List<Comment> comments) {
            setDrawerCaptionsVisibiliy(View.VISIBLE);
            if (mCaptionAdapter == null) {
                mCaptionAdapter = new CaptionAdapter(mItemId, comments, getSupportFragmentManager());
                mListCaptions.setAdapter(mCaptionAdapter);
                attachCommentLongPressListeners();
            } else {
                mCaptionAdapter.setCaptions(mItemId, comments);
            }
    }

    private void attachCommentLongPressListeners() {
        AdapterView.OnItemLongClickListener listener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DialogCommentDetail dialog = DialogCommentDetail.newInstance(mItemId, mCaptionAdapter.getItem(position));

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);

                dialog.show(transaction, DialogCommentDetail.TAG);

                return true;
            }
        };

        mListCaptions.setOnItemLongClickListener(listener);
    }

    public void onCommentSubmitted(@Observes CommentSubmittedEvent event) {
        loadCommentsForItem();
    }

    private ItemDetailsFragment.GalleryItemLoadedListener getItemLoadedListener() {
        return new ItemDetailsFragment.GalleryItemLoadedListener() {
            @Override
            public void galleryItemLoaded(GalleryItem item) {
                mFragmentImageViewer.setCurrentGalleryItem(item);
                mHasComments = true;
                setDrawerCaptionsVisibiliy(View.VISIBLE);
                loadCommentsForItem();
            }
        };
    }

    public enum ItemType {
        ALBUM,
        IMAGE
    }
}
