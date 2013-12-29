package com.ezimgur.view.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.data.SettingsManager;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.VoteType;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.LoadGalleryItemTask;
import com.ezimgur.task.VoteOnImageTask;
import com.ezimgur.view.activity.CommunityActivity;
import com.ezimgur.view.event.AlbumTotalCountEvent;
import com.ezimgur.view.event.OnSelectImageEvent;
import com.ezimgur.view.event.UpVoteItemEvent;
import com.ezimgur.view.utils.ViewUtils;
import com.ezimgur.view.utils.VoteUtils;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import roboguice.event.Observes;
import roboguice.inject.InjectView;

import java.util.Date;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/6/13
 * Time: 6:48 PM
 */
public class ItemDetailsFragment extends RoboSherlockFragment {

    @InjectView(R.id.dw_image_content)RelativeLayout mDrawerContent;
    @InjectView(R.id.tv_image_caption)TextView mTextImageCaption;
    @InjectView(R.id.tv_image_author)TextView mTextImageAuthor;
    @InjectView(R.id.tv_image_upvotes)TextView mTextImageUpVotes;
    @InjectView(R.id.tv_image_downvotes)TextView mTextImageDownVotes;
    @InjectView(R.id.tv_image_views)TextView mTextImageViews;
    @InjectView(R.id.tv_image_points)TextView mTextImagePoints;
    @InjectView(R.id.tv_image_timeago)TextView mTextTimeAgo;
    @InjectView(R.id.tv_album_index_indicator)TextView mTextAlbumIndex;
    @InjectView(R.id.img_upvotes) ImageView mTextUpVoteImage;
    @InjectView(R.id.img_downvotes) ImageView mTextDownVoteImage;
    @InjectView(R.id.caption_handle) ImageView mCaptionHandle;

    private GalleryItemLoadedListener mItemLoadedListener;
    private ItemDetails mItemDetails;
    private GalleryItem mGalleryItem;
    private boolean mIsGalleryItem;
    private boolean mIsAlbum;
    private int mAlbumIndex = 1;
    private int mTotalCount;

    private static final String TAG = "EzImgur.ItemDetailsFragment";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SettingsManager manager = new SettingsManager(getActivity());
        boolean oldLayout = manager.getValue(SettingsManager.SETTING_USE_OLD_LAYOUT, ViewUtils.isTabletInLandscapeMode(getActivity()));
        if (oldLayout)
            mCaptionHandle.setVisibility(View.GONE);
    }

    public void setCurrentImage(Image image) {
        mItemDetails = new ItemDetails();
        mItemDetails.id = image.id;
        mItemDetails.author = image.accountUrl;
        mItemDetails.dateCreated = image.dateCreated;
        mItemDetails.title = image.title;
        mItemDetails.views = image.views;

        tryLoadGalleryItem();
    }

    public void setCurrentAlbum(Album album) {
        mItemDetails = new ItemDetails();
        mItemDetails.id = album.id;
        mItemDetails.dateCreated = album.dateCreated;
        mItemDetails.title = album.title;
        mItemDetails.images = album.images;
        mIsAlbum = true;

        tryLoadGalleryItem();
    }

    public void setGalleryItem(GalleryItem item) {
        mGalleryItem = item;
        mItemDetails = new ItemDetails();
        mItemDetails.id = item.id;
        mItemDetails.vote = item.vote;
        mItemDetails.ups = item.ups;
        mItemDetails.downs = item.downs;
        mItemDetails.author = item.accountUrl;
        mItemDetails.points = item.score;
        mItemDetails.title = item.title;
        mItemDetails.dateCreated = item.dateCreated;
        mItemDetails.views = item.views;
        if (item.isAlbum) {
            mIsAlbum = true;
        } else {
            mIsAlbum = false;
            mTextAlbumIndex.setText(" ");
        }

        setupView();
    }

    public void setAlbumTotalCount(int count) {
        mTotalCount = count;
    }

    private void setupView() {
        mTextImageCaption.setText(mItemDetails.title);
        mTextImagePoints.setText(mItemDetails.points + " points");
        mTextImageCaption.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder popupBuilder = new AlertDialog.Builder(getActivity());
                TextView captionText =  new TextView(getActivity());
                captionText.setTextSize(20);
                captionText.setText(mItemDetails.title);
                captionText.setGravity(Gravity.CENTER_HORIZONTAL);
                popupBuilder.setView(captionText);
                popupBuilder.create().show();
                return true;
            }
        });

        mTextImageViews.setText(ViewUtils.commify(mItemDetails.views + ""));
        if (mItemDetails.dateCreated != null)
            mTextTimeAgo.setText(DateUtils.getRelativeTimeSpanString(mItemDetails.dateCreated.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS));
        mTextImageAuthor.setText(mItemDetails.author);
        if (mItemDetails.author != null && mItemDetails.author.length() > 0){
            mTextImageAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence[] items = new CharSequence[1];
                    items[0] = "Send Message";

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    AlertDialog dialog = builder
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), CommunityActivity.class);
                                    intent.putExtra(CommunityActivity.EXTRA_COMPOSE_TO, mItemDetails.author);
                                    getActivity().startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }
            });
        }
        setVotesData();
        if (mIsAlbum) {
           setAlbumImageIndex(1);
        }

        attachListeners();
    }

    public void onImagePositionChanged(@Observes OnSelectImageEvent event) {
        if (event.isAlbumChange()) {
            setAlbumImageIndex(event.getPosition()+1);
        }
    }


    public void onAlbumTotalCountEvent(@Observes AlbumTotalCountEvent event) {
        if (mGalleryItem.id.equals(event.albumId)) {
            mTotalCount = event.totalCount;
            setAlbumImageIndex(event.currentIndex + 1);
        }
    }

    public void onUpvoteItemEvent(@Observes UpVoteItemEvent event) {
        mGalleryItem.vote = VoteUtils.getUpVoteString();
        mGalleryItem.ups += 1;
        setVotesData();
        setUpVoteColor(true);
    }

    public int getMeasuredHeight(){
        return mDrawerContent.getMeasuredHeight();
    }

    private void tryLoadGalleryItem() {
        new LoadGalleryItemTask(getActivity(), mItemDetails.id) {

            @Override
            protected void onSuccess(GalleryItem galleryItem) throws Exception {
                super.onSuccess(galleryItem);
                mItemDetails.title = galleryItem.title;
                mItemDetails.author = galleryItem.accountUrl;
                mItemDetails.vote = galleryItem.vote;
                mItemDetails.views = galleryItem.views;
                mItemDetails.points = galleryItem.score;
                mItemDetails.downs = galleryItem.downs;
                mItemDetails.ups = galleryItem.ups;
                mItemDetails.dateCreated = galleryItem.dateCreated;

                if (mItemLoadedListener != null)
                    mItemLoadedListener.galleryItemLoaded(galleryItem);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                mIsGalleryItem = false;
            }

            @Override
            protected void onFinally() throws RuntimeException {
                super.onFinally();

                setupView();
            }
        }.execute();
    }

    private void attachListeners() {

        View.OnClickListener upVoteListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (EzApplication.isAuthenticatedWithWarning(getActivity()))
                    upVoteImage();
            }
        };
        View.OnClickListener downVoteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EzApplication.isAuthenticatedWithWarning(getActivity()))
                    downVoteImage();
            }
        };

        mTextUpVoteImage.setOnClickListener(upVoteListener);
        mTextImageUpVotes.setOnClickListener(upVoteListener);

        mTextDownVoteImage.setOnClickListener(downVoteListener);
        mTextImageDownVotes.setOnClickListener(downVoteListener);
    }

    public void upVoteImage() {
        new VoteOnImageTask(getActivity(), mItemDetails.id, VoteType.UP){

            @Override
            protected void onSuccess(Boolean success) throws Exception {
                super.onSuccess(success);

                VoteType type = VoteUtils.getVoteTypeFromString(mItemDetails.vote);
                if (success && type != VoteType.UP) {
                    mItemDetails.vote = VoteUtils.getUpVoteString();
                    mItemDetails.ups += 1;
                    if (type == VoteType.DOWN)
                        mItemDetails.downs -= 1;
                    setVotesData();
                    setUpVoteColor(true);
                } else if (success && type == VoteType.UP) {
                    mItemDetails.vote = "";
                    mItemDetails.ups -= 1;
                    setUpVoteColor(false);
                    setVotesData();
                }
                mGalleryItem.vote = mItemDetails.vote;
            }
        }.execute();
    }

    public void downVoteImage() {
        new VoteOnImageTask(getActivity(), mItemDetails.id, VoteType.DOWN){

            @Override
            protected void onSuccess(Boolean success) throws Exception {
                super.onSuccess(success);

                VoteType type = VoteUtils.getVoteTypeFromString(mItemDetails.vote);
                if (success && type != VoteType.DOWN) {
                    mItemDetails.vote = VoteUtils.getDownVoteString();
                    mItemDetails.downs += 1;
                    if (type == VoteType.UP)
                        mItemDetails.ups -= 1;
                    setVotesData();
                    setDownVoteColor(true);
                } else if (success && type == VoteType.DOWN) {
                    mItemDetails.vote = "";
                    mItemDetails.downs -= 1;
                    setDownVoteColor(false);
                    setVotesData();
                }
                mGalleryItem.vote = mItemDetails.vote;
            }
        }.execute();
    }

    public void setGalleryItemLoadedListener(GalleryItemLoadedListener listener) {
        Log.d(TAG, "setting gallery item loaded listener");
        mItemLoadedListener = listener;
    }

    private void setVotesData() {
        mTextImageUpVotes.setText(mItemDetails.ups + " ");
        mTextImageDownVotes.setText(mItemDetails.downs + " ");

        setVotesViewState();
    }

    private void setVotesViewState() {
        VoteType type = VoteUtils.getVoteTypeFromString(mItemDetails.vote);

        switch (type){
            case UP:
                setUpVoteColor(true);
                setDownVoteColor(false);
                break;
            case DOWN:
                setDownVoteColor(true);
                setUpVoteColor(false);
                break;
            default:
                setUpVoteColor(false);
                setDownVoteColor(false);
        }
    }

    private void setUpVoteColor(boolean active) {
        if (active)
            mTextUpVoteImage.setImageResource(R.drawable.upvote);
        else
           mTextUpVoteImage.setImageResource(R.drawable.upvote_empty);
    }

    private void setDownVoteColor(boolean active) {
        if (active)
            mTextDownVoteImage.setImageResource(R.drawable.downvote);
        else
            mTextDownVoteImage.setImageResource(R.drawable.downvote_empty);
    }

    private void setAlbumImageIndex(int selectedIndex) {
        if (mItemDetails.images != null) {
            mTotalCount = mItemDetails.images.size();
        }
            String indexText = String.format("%s/%s", selectedIndex,mTotalCount);
            mTextAlbumIndex.setText(indexText);
    }

    protected class ItemDetails {
        public String id;
        public String vote;
        public int downs;
        public int ups;
        public String author;
        public String title;
        public long views;
        public long points;
        public Date dateCreated;
        public List<Image> images;
    }

    public interface GalleryItemLoadedListener {
        void galleryItemLoaded(GalleryItem item);
    }
}
