package com.ezimgur.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ezimgur.R;
import com.ezimgur.api.AlbumApi;
import com.ezimgur.api.ImageApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.datacontract.*;
import com.ezimgur.file.FileManager;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.FavoriteItemTask;
import com.ezimgur.task.LoadAlbumImagesTask;
import com.ezimgur.view.activity.BaseActivity;
import com.ezimgur.view.component.TouchImageView;
import com.ezimgur.view.component.TouchWebView;
import com.ezimgur.view.event.AlbumTotalCountEvent;
import com.ezimgur.view.event.ImageViewerFlingEvent;
import com.ezimgur.view.event.OnSelectImageEvent;
import com.ezimgur.view.listener.SwipeGestureDetector;
import com.ezimgur.view.utils.EzImageLoader;
import com.ezimgur.view.utils.ViewUtils;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.RoboGuice;
import roboguice.event.EventManager;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:49 PM
 */
public class GalleryItemFragment extends RoboSherlockFragment {

    @Inject EventManager eventManager;
    @Inject ImageApi imageApi;
    @Inject AlbumApi albumApi;

    @InjectView(R.id.fragment_gallery_item_container) RelativeLayout container;
    @InjectView(R.id.fragment_gallery_item_tiv) TouchImageView touchImageView;
    @InjectView(R.id.fragment_gallery_item_twv) TouchWebView touchWebView;
    @InjectView(R.id.frag_iv_tv_album_caption) TextView albumCaption;

    private GalleryItemComposite target;
    private boolean isAlbum;
    private GalleryImage image;
    private GalleryAlbum album;
    private Image targetImage;
    private int albumIndex;

    private static final String TAG = "EzImgur.GalleryItemFragment";

    public static GalleryItemFragment newInstance(GalleryItemComposite item) {
        Bundle args = new Bundle();

        args.putParcelable("item", item);

        GalleryItemFragment fragment = new GalleryItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gallery_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        target = args.getParcelable("item");

        transformGalleryItemToTarget();
        loadImage();
        attachListeners();
    }

    private void transformGalleryItemToTarget(){

        isAlbum = target.galleryItem instanceof GalleryAlbum;
        if (isAlbum) {
            isAlbum = true;
            album = (GalleryAlbum) target.galleryItem;
            eventManager.fire(new AlbumTotalCountEvent(album.imageCount));

        } else {
            isAlbum = false;
            image  = (GalleryImage) target.galleryItem;
            targetImage = image.toImage();
        }
    }

    private void loadImage() {
        if (isAlbum) {
            new LoadAlbumImagesTask(getActivity(), 1, 1000, album.id) {
                @Override
                protected void onSuccess(List<Image> images) throws Exception {
                    super.onSuccess(images);
                    album.images = new ArrayList<Image>();
                    album.images.addAll(images);
//                    if (!mLastAlbumId.equals(mTargetAlbum.id)){
//                        mAlbumImageIndex = 0;
//                        mLastAlbumId = mTargetAlbum.id;
//                    }

                    eventManager.fire(new AlbumTotalCountEvent(album.images.size()));
                    targetImage = album.images.get(albumIndex);
                    loadImageAndSetViewState(targetImage);
                }
            }.execute();
        } else  {
            loadImageAndSetViewState(image.toImage());
        }
    }

    private void loadImageAndSetViewState(Image image) {
        String imageUrl = imageApi.getHttpUrlForImage(image, ImageSize.ACTUAL_SIZE);

        String ext = imageUrl.substring(imageUrl.length()-3, imageUrl.length());
        if (ext.equalsIgnoreCase("gif"))
            image.animated = true;

        if (image.animated ) {
            touchWebView.setVisibility(View.VISIBLE);
            touchImageView.setVisibility(View.GONE);

            touchWebView.clearCache(true);
            touchWebView.bringToFront();
            touchWebView.loadDataWithBaseURL(null,
                    String.format(ViewUtils.htmlImageFormat, ViewUtils.javascript, imageUrl),
                    "text/html",
                    "utf-8",
                    null);
        } else {
            touchImageView.setVisibility(View.VISIBLE);
            touchWebView.setVisibility(View.GONE);

            ImageLoader imageLoader = EzImageLoader.getImageLoaderInstance(getActivity());
            imageLoader.displayImage(imageUrl, touchImageView, BaseActivity.getDefaultImageLoadingListener(eventManager));
        }



        if (isAlbum && image.title != null && image.title.length() > 0) {
            String desc = image.description == null ? "":image.description;
            albumCaption.setText(image.title + "\n" + desc);
            albumCaption.setVisibility(View.VISIBLE);

        } else if (image.description != null && image.description.length() > 0){
            albumCaption.setText(image.description);
            albumCaption.setVisibility(View.VISIBLE);

        } else {
            albumCaption.setVisibility(View.GONE);
        }

        touchWebView.setContextMenuTitle(image.title);
        touchImageView.setContextMenuTitle(image.title);

        setupContextMenuProvider();
    }

    private void attachListeners() {
        touchImageView.setOnFlingListener(getImageViewListener());
        touchWebView.setOnFlingListener(getImageViewListener());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        if (getUserVisibleHint()) {
                // Handle menu events and return true

            CharSequence itemTitle = item.getTitle();
            if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COPY)) {
                sendTextToClipboard(imageApi.getHttpUrlForImage(targetImage, ImageSize.ACTUAL_SIZE));
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
                viewContent(imageApi.getHttpUrlForImage(targetImage, ImageSize.ACTUAL_SIZE), true);
                return true;
            }
            else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COMMENT_ITEM)) {
                openItemDetailFragment();
                return true;
            } else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_REDDIT_COMMENTS)) {
                viewContent("http://reddit.com/" +getRedditCommentsLinkForCurrentItem(), false);
                return true;
            } else if (itemTitle.equals(BaseActivity.CONTEXT_MENU_COPY_ALBUM_URL)) {
                if (album != null)
                    sendTextToClipboard(albumApi.getHttpUrlForAlbum(album.id));
             }
             return true;
        }
        return false;
    }


    private void viewContent(String contentUrl, boolean forceInWeb) {
        DialogContentViewer contentViewer = DialogContentViewer.newInstance(contentUrl, forceInWeb);
        contentViewer.show(getFragmentManager(), TAG);
    }

    private void openItemDetailFragment() {
        DialogGalleryItemDetail dialogFragment = DialogGalleryItemDetail.newInstance(getCurrentGalleryItem(), false);
        dialogFragment.show(getFragmentManager(), "galleryitem_detail");
    }

    public GalleryItem getCurrentGalleryItem() {
        if (isAlbum)
            return album;
        else
            return image;
    }

    private void saveImageToDisk() {
        if (targetImage == null) {
            noImageMessage();
            return;
        }

        if (targetImage.animated){
            Toast.makeText(getActivity(), "Can't save gifs currently, sorry", Toast.LENGTH_SHORT).show();
        } else {
            Bitmap imageToSave = ((BitmapDrawable)touchImageView.getDrawable()).getBitmap();
            FileManager.saveImageToSd(getActivity(), imageToSave, targetImage.id, imageApi.getExtensionForImage(targetImage), true);
        }
    }

    private void sendTextToClipboard(String textToSend){
        if (targetImage == null) {
            noImageMessage();
            return;
        }
        FileManager.sendTextToClipboard(getActivity(), textToSend);
    }

    private void noImageMessage() {
        Toast.makeText(getActivity(), "Image is not ready bro", Toast.LENGTH_SHORT).show();
    }

    public void shareCurrentImage() {
        if (targetImage == null) {
            noImageMessage();
            return;
        }

        if (targetImage.animated) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, image.title);
            shareIntent.putExtra(Intent.EXTRA_TITLE, image.title);
            shareIntent.putExtra(Intent.EXTRA_TEXT, imageApi.getHttpUrlForImage(targetImage, ImageSize.ACTUAL_SIZE));
            startActivity(shareIntent);
        } else {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType(targetImage.mimeType);

            Bitmap imageToSave = ((BitmapDrawable)touchImageView.getDrawable()).getBitmap();
            String path = FileManager.saveImageToSd(getActivity(), imageToSave, "ezimgur_last_shared", imageApi.getExtensionForImage(targetImage), false);

            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
            startActivity(Intent.createChooser(share, "Share Image"));
        }
    }


    private void saveImageToFavorites() {
        Image image = targetImage;

        AlbumsManager manager = new AlbumsManager(getActivity());
        manager.addImageToFavoritesAlbum(image);


        if (EzApplication.hasToken())  {
            GalleryItem item = getCurrentGalleryItem();
            new FavoriteItemTask(getActivity(), item.id, isAlbum){

                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    Toast.makeText(getActivity(), "item added to your imgur account as favorite", Toast.LENGTH_SHORT).show();
                }
            }.execute();
        }

        Toast.makeText(getActivity(), "image saved locally in favorites album", Toast.LENGTH_SHORT).show();
    }

    private String getRedditCommentsLinkForCurrentItem() {
        if (isAlbum && album != null) {
            return album.redditCommentsLink;
        }
        else if (image != null)
            return image.redditCommentsLink;
        return null;
    }

    private void setupContextMenuProvider() {
        ImageViewerFragment.ContextMenuProvider provider = new ImageViewerFragment.ContextMenuProvider() {
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

        touchImageView.setContextMenuProvider(provider);
        touchWebView.setmContextMenuProvider(provider);
    }

    private List<String> addDynamicItemsToContextMenuList(List<String> menuList) {
        if (isAlbum)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_COPY_ALBUM_URL);

        boolean hasRedditCommentsLink = getRedditCommentsLinkForCurrentItem() != null;
        if (hasRedditCommentsLink)
            addDynamicItemToContextMenuList(menuList, BaseActivity.CONTEXT_MENU_REDDIT_COMMENTS);

        return menuList;
    }

    private void addDynamicItemToContextMenuList(List<String> menuList, String menuItem) {
        if (!menuList.contains(menuItem)){
            menuList.add(menuItem);
        }
    }


    private SwipeGestureDetector.GestureListener getImageViewListener() {
        return new SwipeGestureDetector.GestureListener() {

            @Override
            public boolean onFlingUp() {

                if (isAlbum && touchImageView.saveScale < 1.1f && albumIndex < album.images.size() -1) {
                    albumIndex++;

                    loadImage();

                    eventManager.fire(new OnSelectImageEvent(albumIndex, true));
                    return true;
                }
                return false;
            }

            @Override
            public boolean onFlingDown() {

                if (touchImageView.saveScale < 1.1f && isAlbum) {
                    if (albumIndex >0) {
                        albumIndex--;

                        loadImage();

                        eventManager.fire(new OnSelectImageEvent(albumIndex, true));
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean onFlingLeft() {
                return false;
            }

            @Override
            public boolean onFlingRight() {
                return false;
            }

            @Override
            public void onLongPress() {
                if (targetImage == null)
                    return;
                if (!targetImage.animated)
                    getActivity().openContextMenu(touchImageView);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return touchImageView.onDoubleTap(e);
            }
        };
    }
}
