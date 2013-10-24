package com.ezimgur.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ezimgur.R;
import com.ezimgur.api.ImageApi;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.GalleryImage;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.ImageSize;
import com.ezimgur.file.FileManager;
import com.ezimgur.serializer.GsonUtils;
import com.ezimgur.task.LoadLikedItemsTask;
import com.ezimgur.view.activity.BaseActivity;
import com.ezimgur.view.activity.ViewItemActivity;
import com.ezimgur.view.adapter.LikesAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;
import com.google.inject.Inject;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 8:12 PM
 */
public class LikesFragment extends RoboSherlockFragment {

    @Inject ImageApi mImageApi;
    private LikesState mState;
    private LikesAdapter mAdapter;
    private ListView mListLikes;

    public static LikesFragment newInstance() {
        LikesFragment likesFragment = new LikesFragment();
        return likesFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_likes, null);

        mListLikes = (ListView) v.findViewById(R.id.frg_likes_lv_likes);

        if (savedInstanceState == null)
            loadLikedItems();
        else {
            mState = GsonUtils.getGsonInstance().fromJson(savedInstanceState.getString("state"), LikesState.class);
            setViewState();
        }

        attachViewListeners();

        return v;
    }

    private void loadLikedItems() {
        String userName = EzApplication.getAccountUserName();
        new LoadLikedItemsTask(getActivity(), userName) {

            @Override
            protected void onSuccess(List<GalleryItem> items) throws Exception {
                super.onSuccess(items);
                if (mState == null)
                    mState = new LikesState();
                mState.likes = items;
                setViewState();
            }
        }.execute();
    }

    private void setViewState() {
        if (mAdapter == null) {
            mAdapter = new LikesAdapter(mImageApi, mState.likes);
            mListLikes.setAdapter(mAdapter);
        } {
            mAdapter.setLikedItems(mState.likes);
        }
    }

    private void attachViewListeners(){
        mListLikes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final CharSequence[] items = {BaseActivity.CONTEXT_MENU_COPY, BaseActivity.CONTEXT_MENU_SHARE };

                final GalleryItem selectedItem = mAdapter.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                if (selectedItem.title == null || selectedItem.title.length() < 1)
                    selectedItem.title = "untitled ("+selectedItem.id+")";

                builder.setTitle(selectedItem.title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item] == BaseActivity.CONTEXT_MENU_COPY) {

                            String url;
                            if (!selectedItem.isAlbum){
                                GalleryImage image = (GalleryImage) selectedItem;
                                url  = mImageApi.getHttpUrlForImage(image.toImage(), ImageSize.ACTUAL_SIZE);
                            } else  {
                                url = String.format("http://imgur.com/a/%s", selectedItem.id);
                            }

                            FileManager.sendTextToClipboard(getActivity(), url);
                        }
                        else if (items[item] == BaseActivity.CONTEXT_MENU_SHARE) {
                            if (selectedItem instanceof GalleryImage){
                                GalleryImage image = (GalleryImage) selectedItem;
                                shareImage(image.title, mImageApi.getHttpUrlForImage(image.toImage(), ImageSize.ACTUAL_SIZE));
                            } else {
                                shareImage(selectedItem.title, String.format("http://imgur.com/a/%s",selectedItem.id));
                            }

                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            }
        });

        final Activity self = getActivity();
        mListLikes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                boolean isAlbum = mAdapter.getItem(position).isAlbum;
                ViewItemActivity.ItemType type = isAlbum ? ViewItemActivity.ItemType.ALBUM:ViewItemActivity.ItemType.IMAGE;

                Intent intent = new Intent(self, ViewItemActivity.class);
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_TYPE, type.ordinal());
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_ID, mAdapter.getItem(position).id);
                intent.putExtra(ViewItemActivity.BUNDLE_ITEM_HAS_COMMENTS, true);
                self.startActivity(intent);

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String json = GsonUtils.getGsonInstance().toJson(mState, LikesState.class);
        outState.putString("state", json);
    }

    public void shareImage(String title, String url) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TITLE, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        startActivity(shareIntent);
    }

    static class LikesState {
        public List<GalleryItem> likes;
    }
}
