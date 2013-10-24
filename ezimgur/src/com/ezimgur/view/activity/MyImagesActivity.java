package com.ezimgur.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.data.AlbumsManager;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Image;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.task.LoadAccountImagesTask;
import com.ezimgur.task.LoadAlbumImagesTask;
import com.ezimgur.task.LoadAlbumsTask;
import com.ezimgur.task.LoadLocalAlbumTask;
import com.ezimgur.view.adapter.AlbumAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 8:43 PM
 */
public class MyImagesActivity extends BaseActivity {

    public static final String ALBUM_ACCOUNT_IMAGES = "account_images_album_id";
    public static final String BUNDLE_ALBUMS_JSON = "albumsjson";

    @InjectView(R.id.gv_albums) protected GridView mAlbumsGridView;

    private static final String TAG = "EzImgur.MyImagesActivity";
    private static final String OPTIONS_RELOAD_TITLE = "reload";
    private AlbumAdapter mAlbumAdapter;
    private List<Album> mAlbums = new ArrayList<Album>();

    @Inject
    ImageApi mImageApi;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        mAlbumAdapter = new AlbumAdapter(mImageApi, mAlbums);
        mAlbumsGridView.setAdapter(mAlbumAdapter);

        if (!EzApplication.isAuthenticated()) {
            Toast.makeText(this, "You are currently not logged in. Log in to see your account images", Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState == null) {
            loadLocalAlbums();
            if (EzApplication.isAuthenticated())
                loadAllAlbumsFromNetwork();
        }

        mAlbumsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Album targetAlbum = mAlbums.get(i);
                Intent intent = new Intent(MyImagesActivity.this, AlbumImagesActivity.class);
                intent.putExtra(AlbumImagesActivity.BUNDLE_ALBUM_TO_SHOW, targetAlbum.id);
                intent.putExtra(AlbumImagesActivity.BUNDLE_ALBUM_TITLE, targetAlbum.title);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentViewId() {
        return R.layout.screen_myimages;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);

        String albumsJson = new Gson().toJson(mAlbums, mAlbums.getClass());
        outState.putString(BUNDLE_ALBUMS_JSON, albumsJson);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);

        String albumJson = savedInstanceState.getString(BUNDLE_ALBUMS_JSON);

        //have to parse specual because it is list
        JsonElement json = new JsonParser().parse(albumJson);
        JsonArray array= json.getAsJsonArray();
        Iterator iterator = array.iterator();

        while(iterator.hasNext()){
            JsonElement object = (JsonElement)iterator.next();
            Gson gson = new Gson();
            Album album = gson.fromJson(object, Album.class);
            mAlbums.add(album);
        }

        mAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(OPTIONS_RELOAD_TITLE).setIcon(R.drawable.ic_reload).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle().equals(OPTIONS_RELOAD_TITLE)){
            mAlbums = new ArrayList<Album>();
            mAlbumAdapter.setAlbums(mAlbums);
            if (EzApplication.isAuthenticated())
                this.loadAllAlbumsFromNetwork();
            else
                Toast.makeText(this, "you are not logged in. log in to see account images", Toast.LENGTH_SHORT).show();
            loadLocalAlbums();
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadAllAlbums() {
        loadLocalAlbums();
        loadAllAlbumsFromNetwork();
    }

    private void loadLocalAlbums() {
        new LoadLocalAlbumTask(this, AlbumsManager.UPLOADS_ID) {
            @Override
            protected void onSuccess(Album album) throws Exception {
                super.onSuccess(album);
                addAlbum(album);
            }
        }.execute();
        new LoadLocalAlbumTask(this, AlbumsManager.FAVORITES_ID) {
            @Override
            protected void onSuccess(Album album) throws Exception {
                super.onSuccess(album);
                addAlbum(album);
            }
        }.execute();
    }

    private void addAlbum(Album album) {
        if (album != null) {
            mAlbums.add(album);
            mAlbumAdapter.notifyDataSetChanged();
        }
    }

    private void loadAllAlbumsFromNetwork() {
        Log.d(TAG, "loadAllAlbumsFromNetwork");

        loadAccountImagesForCover();
    }

    private void loadAccountImagesForCover() {
        //load account images album cover...
        new LoadAccountImagesTask(this, 1, 1) {
            @Override
            protected void onSuccess(List<Image> accountImageComposites) throws Exception {
                super.onSuccess(accountImageComposites);
                Log.d(TAG, "load account images success");
                Album album = new Album();
                album.title = "account images";
                album.id = ALBUM_ACCOUNT_IMAGES;
                album.images = new ArrayList<Image>();
                album.images.add(accountImageComposites.get(0));

                mAlbums.add(album);

                loadAlbumsAccountImagesAndCovers();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Toast.makeText(getContext(), "unable to load albums from imgur", Toast.LENGTH_SHORT).show();
            }
        } .execute();
    }

    private void loadAlbumsAccountImagesAndCovers() {
        //load account albums and covers...
        new LoadAlbumsTask(this, 0, 100) {
            @Override
            protected void onSuccess(List<Album> accountAlbums) throws Exception {
                super.onSuccess(accountAlbums);
                Log.d(TAG, "load albums success");

                mAlbums.addAll(accountAlbums);
                mAlbumAdapter.notifyDataSetChanged();

                loadCoverForEachAlbum();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Toast.makeText(getContext(), "unable to load all albums from imgur", Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

    private void loadCoverForEachAlbum () {
        final int total = mAlbums.size();

        for (final Album album : mAlbums) {
            //only load 1 image for the cover...
            if (!album.id.equals(ALBUM_ACCOUNT_IMAGES) && !AlbumsManager.isLocalAlbum(album.id)) {
                new LoadAlbumImagesTask(this, 1, 1, album.id){
                    @Override
                    protected void onSuccess(List<Image> accountImageComposites) throws Exception {
                        super.onSuccess(accountImageComposites);
                        if (accountImageComposites != null && accountImageComposites.size() > 0) {
                            if (album.images == null)
                                album.images = new ArrayList<Image>();
                            album.images.add(accountImageComposites.get(0));
                            mAlbumAdapter.notifyDataSetChanged();
                        }

                    }
                }.execute();
            }
        }
    }
}
