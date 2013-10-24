package com.ezimgur.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.ezimgur.R;
import com.ezimgur.api.AlbumApi;
import com.ezimgur.api.ImageApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.data.GalleryManager;
import com.ezimgur.datacontract.*;
import com.ezimgur.file.FileManager;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.FavoriteItemTask;
import com.ezimgur.task.LoadAlbumImagesTask;
import com.ezimgur.view.activity.BaseActivity;
import com.ezimgur.view.component.TouchImageView;
import com.ezimgur.view.component.TouchWebView;
import com.ezimgur.view.event.ImageViewerFlingEvent;
import com.ezimgur.view.event.OnSelectImageEvent;
import com.ezimgur.view.listener.SwipeGestureDetector;
import com.ezimgur.view.utils.EzImageLoader;
import com.ezimgur.view.utils.ViewUtils;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.event.EventManager;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/2/12
 * Time: 8:01 PM
 */
public class ImageViewerFragment extends RoboSherlockFragment {

    @Inject protected ImageApi mImageApi;
    @Inject protected AlbumApi mAlbumApi;
    @Inject protected EventManager mEventManager;
    @InjectView(R.id.tiv_main_image)TouchImageView mTouchImageView;
    @InjectView(R.id.twv_main_image)TouchWebView mTouchWebView;
    @InjectView(R.id.frag_iv_tv_album_caption)TextView mAlbumCaption;
    @InjectView(R.id.frag_iv_album_caption_container)ScrollView mScrollContainer;

    private String mLastAlbumId = "";
    private int mCurrentPosition;
    private Gallery mGallery;
    private boolean mIsCurrentlyAlbum;
    private int mAlbumImageIndex;
    private GalleryAlbum mCurrentAlbum;
    private GalleryImage mCurrentImage;
    private Image mTargetImage;
    private Album mTargetAlbum;
    private boolean mSingleItemMode;
    private boolean mHasComments;
    private GalleryItem mExternalLoadedItem;

    private static final String TAG = "EzImgur.ImageViewerFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        CharSequence itemTitle = item.getTitle();
        if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COPY)) {
            sendTextToClipboard(mImageApi.getHttpUrlForImage(mTargetImage, ImageSize.ACTUAL_SIZE));
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_SHARE)) {
            shareCurrentImage();
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_SAVE)) {
             saveImageToDisk();
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_FAV_IMAGE)) {
            saveImageToFavorites();
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_OPEN_IN_WEB)) {
            viewContent(mImageApi.getHttpUrlForImage(mTargetImage, ImageSize.ACTUAL_SIZE), true);
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COMMENT_ITEM)) {
            openItemDetailFragment();
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_IMGUR_PAGE)) {
            viewContent(mImageApi.getImgurPageUrlForImage(mGallery.galleryName, mTargetImage.id), false);
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_REDDIT_COMMENTS)) {
            viewContent("http://reddit.com/" +getRedditCommentsLinkForCurrentItem(), false);
            return true;
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COPY_ALBUM_URL)) {
            if (mCurrentAlbum != null)
                sendTextToClipboard(mAlbumApi.getHttpUrlForAlbum(mCurrentAlbum.id));
            else if (mTargetAlbum != null) {
                sendTextToClipboard(mTargetAlbum.link);
            }
        }
        else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COPY_PAGE_URL)) {
            if (mGallery != null) {
                GalleryItem targetItem = mGallery.imageList.get(mCurrentPosition);
                String url = "";
                if (mGallery.galleryName.contains("r/")){
                    url = String.format("http://imgur.com/%s/%s", mGallery.galleryName, targetItem.id);
                } else {
                    url = String.format("http://imgur.com/gallery/%s", targetItem.id);
                }
                sendTextToClipboard(url);
            } else if (mExternalLoadedItem != null) {
                sendTextToClipboard(String.format("http://imgur.com/gallery/%s", mExternalLoadedItem.id));
            }
        }

        return false;
    }

    public void setCurrentImage(Image image) {
        mExternalLoadedItem = null;
        mSingleItemMode = true;
        mIsCurrentlyAlbum = false;
        loadImageAndSetViewState(image);

    }

    public void setCurrentAlbum(Album album) {
        mExternalLoadedItem = null;
        mSingleItemMode = true;
        mTargetAlbum = album;
        mIsCurrentlyAlbum = true;
        mAlbumImageIndex = 0;
        loadImageAndSetViewState(album.images.get(mAlbumImageIndex));
    }

    public void setSingleItemHasComments(boolean hasComments) {
        mHasComments = hasComments;
    }

    private void viewContent(String contentUrl, boolean forceInWeb) {
        DialogContentViewer contentViewer = DialogContentViewer.newInstance(contentUrl, forceInWeb);
        contentViewer.show(getFragmentManager(), TAG);
    }

    private void openItemDetailFragment() {
        DialogGalleryItemDetail dialogFragment = DialogGalleryItemDetail.newInstance(getCurrentGalleryItem(), mHasComments);
        dialogFragment.show(getFragmentManager(), "galleryitem_detail");
    }

    private void saveImageToFavorites() {
        Image image = getCurrentImage();

        AlbumsManager manager = new AlbumsManager(getActivity());
        manager.addImageToFavoritesAlbum(image);


        if (EzApplication.hasToken())  {
        GalleryItem item = getCurrentGalleryItem();
            new FavoriteItemTask(getActivity(), item.id, mIsCurrentlyAlbum){

                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    Toast.makeText(getActivity(), "item added to your imgur account as favorite", Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }

        Toast.makeText(getActivity(), "image saved locally in favorites album", Toast.LENGTH_SHORT).show();
    }

    public void setImageGallery(Gallery gallery, int imageToLoadPosition) {
        mGallery = gallery;
        loadImage(imageToLoadPosition);
        mHasComments = GalleryManager.galleryHasComments(mGallery.galleryName);
    }

    public void setImageGallery(Gallery gallery) {
        mGallery = gallery;

        loadImage(0);
        mHasComments = GalleryManager.galleryHasComments(mGallery.galleryName);
    }

    public void setCurrentImageByPositiion(int position) {
        loadImage(position);
    }

    public Image getCurrentImage() {
        if (!mIsCurrentlyAlbum && mCurrentImage != null)
            return mCurrentImage.toImage();
        else if (!mIsCurrentlyAlbum && mExternalLoadedItem != null) {
            return ((GalleryImage)(mExternalLoadedItem)).toImage();
        }
        else if (mIsCurrentlyAlbum)  {
            return mTargetAlbum.images.get(mAlbumImageIndex);
        }
        else
            return mTargetImage;
    }

    public GalleryItem getCurrentGalleryItem() {
        if (mSingleItemMode) {
            if (mExternalLoadedItem != null) {
                return mExternalLoadedItem;
            }
            else if (mIsCurrentlyAlbum) {
                return Extensions.albumToItem(mTargetAlbum);
            } else {
                return Extensions.imageToItem(mTargetImage);
            }
        } else {
            return mGallery.imageList.get(mCurrentPosition);
        }
    }

    public void setCurrentGalleryItem(GalleryItem item) {
        mExternalLoadedItem = item;
        mHasComments = true;
    }

    private void loadImage(final int position) {
        loadImage(position, false);
    }

    private void loadImage(final int position, boolean isFling) {
        if (!mSingleItemMode && position >= mGallery.imageList.size() || position < 0){
            return;
        }

        Log.d(TAG, "loadImage = "+position);
        transformGalleryItemToTarget(position);
        mCurrentPosition = position;

        if (mIsCurrentlyAlbum) {
            Log.d(TAG, "current image is album");

            new LoadAlbumImagesTask(getActivity(), 1, 1000, mTargetAlbum.id) {
                @Override
                protected void onSuccess(List<Image> images) throws Exception {
                    super.onSuccess(images);
                    mTargetAlbum.images = new ArrayList<Image>();
                    mTargetAlbum.images.addAll(images);
                    if (!mLastAlbumId.equals(mTargetAlbum.id)){
                        mAlbumImageIndex = 0;
                        mLastAlbumId = mTargetAlbum.id;
                    }

                    mTargetImage = mTargetAlbum.images.get(mAlbumImageIndex);
                    loadImageAndSetViewState(mTargetImage);
                    mEventManager.fire(new OnSelectImageEvent(mAlbumImageIndex, true));
                }
            }.execute();
        } else  {
            loadImageAndSetViewState(mTargetImage);
        }

        mEventManager.fire(new OnSelectImageEvent(mCurrentPosition, false, isFling));
    }

    private void transformGalleryItemToTarget(int position){
        GalleryItem targetItem = mGallery.imageList.get(position);
        boolean isAlbum = targetItem instanceof GalleryAlbum;
        if (isAlbum) {
            mIsCurrentlyAlbum = true;
            mCurrentAlbum = (GalleryAlbum) targetItem;
            mTargetAlbum = mCurrentAlbum.toAlbum();
        } else {
            mIsCurrentlyAlbum = false;
            mCurrentImage = (GalleryImage) targetItem;
            mTargetImage = mCurrentImage.toImage();
        }
    }

    private void setupView(){
        mTouchImageView.setOnFlingListener(getImageViewListener());
        mTouchWebView.setOnFlingListener(getImageViewListener());

        setupContextMenuProvider();
    }

    private void loadImageAndSetViewState(Image image) {
        mTargetImage = image;

        String imageUrl = mImageApi.getHttpUrlForImage(image, ImageSize.ACTUAL_SIZE);

        String ext = imageUrl.substring(imageUrl.length()-3, imageUrl.length());
        if (ext.equalsIgnoreCase("gif"))
            mTargetImage.animated = true;

        if (mTargetImage.animated ) {
            mTouchWebView.setVisibility(View.VISIBLE);
            mTouchImageView.setVisibility(View.GONE);

            mTouchWebView.clearCache(true);
            mTouchWebView.bringToFront();
            mTouchWebView.loadDataWithBaseURL(null,
                    String.format(ViewUtils.htmlImageFormat, ViewUtils.javascript, imageUrl),
                    "text/html",
                    "utf-8",
                    null);
        } else {
            mTouchImageView.setVisibility(View.VISIBLE);
            mTouchWebView.setVisibility(View.GONE);

            ImageLoader imageLoader = EzImageLoader.getImageLoaderInstance(getActivity());
            imageLoader.displayImage(imageUrl, mTouchImageView, BaseActivity.getDefaultImageLoadingListener(mEventManager));
        }



        if (mIsCurrentlyAlbum && image.title != null && image.title.length() > 0) {
            String desc = image.description == null ? "":image.description;
            mAlbumCaption.setText(image.title + "\n" + desc);
            mAlbumCaption.setVisibility(View.VISIBLE);
            attachTextAttachmentListener();
        }
        else if (image.description != null && image.description.length() > 0){
            mAlbumCaption.setText(image.description);
            mAlbumCaption.setVisibility(View.VISIBLE);
            attachTextAttachmentListener();
        }
        else {
            mAlbumCaption.setVisibility(View.GONE);
        }

        mTouchWebView.setContextMenuTitle(mTargetImage.title);
        mTouchImageView.setContextMenuTitle(mTargetImage.title);
    }

    /*
        Used to adjust the scrolling container size based on the measured height of the text view. We can not get the
        actual height of tv until it is added to view tree.
     */
    private void attachTextAttachmentListener(){
        ViewTreeObserver vto = mAlbumCaption.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.LayoutParams lp = mScrollContainer.getLayoutParams();
                int targetHeight = mAlbumCaption.getHeight();

                if (targetHeight > 140)
                    lp.height = 140;
                else
                    lp.height = targetHeight;

                mScrollContainer.setLayoutParams(lp);
                mScrollContainer.invalidate();
                mAlbumCaption.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private void setupContextMenuProvider() {
        ContextMenuProvider provider = new ContextMenuProvider() {
            @Override
            public List<String> getMenuItems(boolean isWeb) {
                String[] globalMenuItems = new String[4];
                globalMenuItems[0] = BaseActivity.CONTEXT_MENU_COPY;
                globalMenuItems[1] = BaseActivity.CONTEXT_MENU_SHARE;
                globalMenuItems[2] = BaseActivity.CONTEXT_MENU_FAV_IMAGE;
                globalMenuItems[3] = BaseActivity.CONTEXT_MENU_OPEN_IN_WEB;

                final List<String> menuItems = new ArrayList<String>(8);
                final List<String> webMenuItems = new ArrayList<String>(7);

                Collections.addAll(menuItems, globalMenuItems);
                Collections.addAll(webMenuItems, globalMenuItems);

                menuItems.add(BaseActivity.CONTEXT_MENU_SAVE);

                if (isWeb)
                    return addDynamicItemsToContextMenuList(webMenuItems);
                else
                    return addDynamicItemsToContextMenuList(menuItems);
            }
        };

        mTouchImageView.setContextMenuProvider(provider);
        mTouchWebView.setmContextMenuProvider(provider);
    }

    private List<String> addDynamicItemsToContextMenuList(List<String> menuList) {
        if (mGallery != null || mExternalLoadedItem != null)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_COPY_PAGE_URL);
        if (mIsCurrentlyAlbum)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_COPY_ALBUM_URL);

        boolean hasRedditCommentsLink = getRedditCommentsLinkForCurrentItem() != null;
        if (mHasComments)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_COMMENT_ITEM);
        if (hasRedditCommentsLink)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_REDDIT_COMMENTS);

        return menuList;
    }

    private void addDynamicItemToContextMenuList(List<String> menuList, String menuItem) {
        if (!menuList.contains(menuItem)){
            menuList.add(menuItem);
        }
    }

    private String getRedditCommentsLinkForCurrentItem() {
        if (mIsCurrentlyAlbum && mCurrentAlbum != null) {
            return mCurrentAlbum.redditCommentsLink;
        }
        else if (mCurrentImage != null)
            return mCurrentImage.redditCommentsLink;
        return null;
    }


    private void saveImageToDisk() {
        if (mTargetImage == null) {
            noImageMessage();
        }

        if (mTargetImage.animated){
            Toast.makeText(getActivity(), "Can't save gifs currently, sorry", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap imageToSave = ((BitmapDrawable)mTouchImageView.getDrawable()).getBitmap();
            FileManager.saveImageToSd(getActivity(), imageToSave, mTargetImage.id, mImageApi.getExtensionForImage(mTargetImage), true);
        }
    }

    private void sendTextToClipboard(String textToSend){
        if (mTargetImage == null) {
            noImageMessage();
            return;
        }
        FileManager.sendTextToClipboard(getActivity(), textToSend);
    }

    public int getCurrentAlbumIndex() {
        return mAlbumImageIndex;
    }

    public int getCurrentAlbumCount() {
        if (mTargetAlbum != null && mTargetAlbum.images != null)
            return mTargetAlbum.images.size();
        return 1;
    }

    public Album getCurrentAlbum() {
        return mTargetAlbum;
    }

    public void shareCurrentImage() {
        if (mTargetImage == null) {
            noImageMessage();
            return;
        }

        if (mTargetImage.animated) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, mTargetImage.title);
            shareIntent.putExtra(Intent.EXTRA_TITLE, mTargetImage.title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, mImageApi.getHttpUrlForImage(mTargetImage, ImageSize.ACTUAL_SIZE));
            startActivity(shareIntent);
        } else {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType(mTargetImage.mimeType);

            Bitmap imageToSave = ((BitmapDrawable)mTouchImageView.getDrawable()).getBitmap();
            String path = FileManager.saveImageToSd(getActivity(), imageToSave, "ezimgur_last_shared", mImageApi.getExtensionForImage(mTargetImage), false);

            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
            startActivity(Intent.createChooser(share, "Share Image"));
        }
    }

    private void noImageMessage() {
        Toast.makeText(getActivity(), "Image is not ready bro", Toast.LENGTH_SHORT).show();
    }

    private void setCurrentShowingImage(Image image) {
        mTargetImage = image;
        loadImageAndSetViewState(mTargetImage);
    }

    private SwipeGestureDetector.GestureListener getImageViewListener() {
        return new SwipeGestureDetector.GestureListener() {

            @Override
            public boolean onFlingUp() {
                Log.d(TAG, "onFlingUp");

                if (mIsCurrentlyAlbum && mTouchImageView.saveScale < 1.1f && mAlbumImageIndex < mTargetAlbum.images.size() -1) {
                    mAlbumImageIndex++;

                    setCurrentShowingImage(mTargetAlbum.images.get(mAlbumImageIndex));

                    mEventManager.fire(new OnSelectImageEvent(mAlbumImageIndex, true));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onFlingDown() {
                Log.d(TAG,"onFlingDown");
                if (mTouchImageView.saveScale < 1.1f && mIsCurrentlyAlbum) {
                    if (mAlbumImageIndex >0) {
                        mAlbumImageIndex--;

                        setCurrentShowingImage(mTargetAlbum.images.get(mAlbumImageIndex));

                        mEventManager.fire(new OnSelectImageEvent(mAlbumImageIndex, true));
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean onFlingLeft() {
                if (mTouchImageView.saveScale < 1.1f) {
                    loadImage(mCurrentPosition + 1, true);
                    mEventManager.fire(new ImageViewerFlingEvent(ImageViewerFlingEvent.ImageViewerFling.LEFT));
                }
                return false;
            }

            @Override
            public boolean onFlingRight() {
                if (mTouchImageView.saveScale < 1.1f) {
                    loadImage(mCurrentPosition - 1, true);
                    mEventManager.fire(new ImageViewerFlingEvent(ImageViewerFlingEvent.ImageViewerFling.RIGHT));
                }
                return false;
            }

            @Override
            public void onLongPress() {
                if (mTargetImage == null)
                    return;
                if (!mTargetImage.animated)
                    getActivity().openContextMenu(mTouchImageView);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return mTouchImageView.onDoubleTap(e);
            }
        };
    }

    public interface ContextMenuProvider {
        List<String> getMenuItems(boolean isWeb);
    }
}
