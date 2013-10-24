package com.ezimgur.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Image;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.file.FileManager;
import com.ezimgur.task.LoadAccountImagesTask;
import com.ezimgur.task.LoadAlbumImagesTask;
import com.ezimgur.task.LoadLocalAlbumTask;
import com.ezimgur.view.adapter.AlbumImageAdapter;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/1/12
 * Time: 9:59 PM
 */
public class AlbumImagesActivity extends BaseActivity {

    public static final String BUNDLE_ALBUM_TO_SHOW = "album_to_show";
    public static final String BUNDLE_ALBUM_TITLE = "album_title";

    private int currentPage = 1;
    private int currentTake = 50;
    private AlbumImageAdapter mAlbumImageAdapter;

    @Inject private ImageApi mImageApi;
    @InjectView(R.id.lv_album_images) private ListView mListImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String targetAlbum = null;

        Bundle intentBundle = getIntent().getExtras();
        if (intentBundle != null) {
            targetAlbum = intentBundle.getString(BUNDLE_ALBUM_TO_SHOW);
            getSupportActionBar().setTitle(intentBundle.getString(BUNDLE_ALBUM_TITLE));
        }
        if (targetAlbum == null)
            finish();
        else {
            loadCurrentAlbumImages(targetAlbum);
        }

        attachViewListeners();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_album_images;
    }

    private void loadCurrentAlbumImages(String targetAlbum) {
        mAlbumImageAdapter = new AlbumImageAdapter(mImageApi, new ArrayList<Image>());
        mListImages.setAdapter(mAlbumImageAdapter);

        if (targetAlbum.equals(MyImagesActivity.ALBUM_ACCOUNT_IMAGES))
            loadAccountImagesAlbum();
        else if (AlbumsManager.isLocalAlbum(targetAlbum))
            loadLocalAlbum(targetAlbum);
        else
            loadNetworkAlbum(targetAlbum);
    }

    private void loadAccountImagesAlbum() {
        new LoadAccountImagesTask(this, currentPage, currentTake){
            @Override
            protected void onSuccess(List<Image> accountImageComposites) throws Exception {
                super.onSuccess(accountImageComposites);
                if (accountImageComposites != null && accountImageComposites.size() > 0)
                    mAlbumImageAdapter.setImages(accountImageComposites);
                else
                    getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " - no images");
            }
        }.execute();
    }

    private void loadLocalAlbum(String albumId) {
        new LoadLocalAlbumTask(this, albumId) {
            @Override
            protected void onSuccess(Album album) throws Exception {
                super.onSuccess(album);
                if (album != null)
                    mAlbumImageAdapter.setImages(album.images);
            }
        }.execute();
    }

    private void loadNetworkAlbum(String albumId) {
        new LoadAlbumImagesTask(this, currentPage, currentTake, albumId ){
            @Override
            protected void onSuccess(List<Image> accountImageComposites) throws Exception {
                super.onSuccess(accountImageComposites);
                if (accountImageComposites != null && accountImageComposites.size() > 0)
                    mAlbumImageAdapter.setImages(accountImageComposites);
                else
                    getSupportActionBar().setTitle(getSupportActionBar().getTitle() + " - no images");
            }
        }.execute();
    }

    private void attachViewListeners() {
        final Activity self = this;
        mListImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CharSequence[] items = { CONTEXT_MENU_COPY, CONTEXT_MENU_SHARE };

                final Image selectedImage = mAlbumImageAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(self);
                if (selectedImage.title == null || selectedImage.title.length() < 1)
                    selectedImage.title = "untitled ("+selectedImage.id+")";

                builder.setTitle(selectedImage.title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item] == CONTEXT_MENU_COPY) {
                            FileManager.sendTextToClipboard(self, mImageApi.getHttpUrlForImage(selectedImage, ImageSize.ACTUAL_SIZE));
                        }
                        else if (items[item] == CONTEXT_MENU_SHARE) {
                            shareImage(selectedImage);
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });


        mListImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(self, ViewItemActivity.class);
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_TYPE, ViewItemActivity.ItemType.IMAGE.ordinal());
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_ID,mAlbumImageAdapter.getItem(position).id);
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_HAS_COMMENTS, false);
                self.startActivity(intent);
            }
        });
    }

    public void shareImage(Image image) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, image.title);
        shareIntent.putExtra(Intent.EXTRA_TITLE, image.title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageApi.getHttpUrlForImage(image, ImageSize.ACTUAL_SIZE));
        startActivity(shareIntent);
    }
}
