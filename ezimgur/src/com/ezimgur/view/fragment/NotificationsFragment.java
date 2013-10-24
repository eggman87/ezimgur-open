package com.ezimgur.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.ezimgur.R;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.Message;
import com.ezimgur.datacontract.Notification;
import com.ezimgur.serializer.GsonUtils;
import com.ezimgur.task.LoadNotificationsTask;
import com.ezimgur.view.activity.CommunityActivity;
import com.ezimgur.view.activity.ViewItemActivity;
import com.ezimgur.view.adapter.NotificationAdapter;
import com.github.rtyley.android.sherlock.roboguice.fragment.RoboSherlockFragment;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/28/12
 * Time: 7:51 PM
 */
public class NotificationsFragment extends RoboSherlockFragment {

    private NotificationState mState;
    private ListView mListNotifications;
    private NotificationAdapter mAdapter;

    private static final String TAG = "EzImgur.Notifications";


    public static NotificationsFragment newInstance() {
        NotificationsFragment fragment = new NotificationsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifications, null);

        mListNotifications = (ListView) v.findViewById(R.id.frg_not_lv_notifications);

        //only load if we need to
        if (savedInstanceState == null)
            loadNotifications();
        else {
            mState = GsonUtils.getGsonInstance().fromJson(savedInstanceState.getString("state"), NotificationState.class);
            setViewState();
            setViewListeners();
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String json = GsonUtils.getGsonInstance().toJson(mState, NotificationState.class);
        outState.putString("state", json);
    }


    private void loadNotifications() {
        new LoadNotificationsTask(getActivity()) {
            @Override
            protected void onSuccess(List<Notification> notifications) throws Exception {
                super.onSuccess(notifications);
                if (mState == null)
                    mState = new NotificationState();
                mState.notifications = notifications;
                setViewState();
                setViewListeners();
            }
        }.execute();
    }

    private void setViewState() {
        if (mAdapter == null) {
            mAdapter = new NotificationAdapter(mState.notifications);
            mListNotifications.setAdapter(mAdapter);
        } {
            mAdapter.setNotifications(mState.notifications);
        }
    }

    public void setViewListeners() {
        mListNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification target = mAdapter.getItem(position);
                if (target.content instanceof Comment){
                    Comment comment = (Comment) target.content;
                    ViewItemActivity.ItemType type = comment.onAlbum ? ViewItemActivity.ItemType.ALBUM: ViewItemActivity.ItemType.IMAGE;

                    Intent intent = new Intent(getActivity(), ViewItemActivity.class);
                    intent.putExtra(ViewItemActivity.BUNDLE_ITEM_TYPE, type.ordinal());
                    intent.putExtra(ViewItemActivity.BUNDLE_ITEM_ID, comment.imageId);
                    intent.putExtra(ViewItemActivity.BUNDLE_ITEM_HAS_COMMENTS, true);
                    getActivity().startActivity(intent);

                } else if (target.content instanceof Message) {
                    getActivity().startActivity(new Intent(getActivity(), CommunityActivity.class));
                }
            }
        });

    }

    public class NotificationState {
        public List<Notification> notifications;
    }
}
