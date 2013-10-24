package com.ezimgur.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.GalleryItem;
import com.ezimgur.datacontract.VoteType;
import com.ezimgur.serializer.GsonUtils;
import com.ezimgur.task.SubmitCommentTask;
import com.ezimgur.task.VoteOnImageTask;
import com.ezimgur.view.activity.CommunityActivity;
import com.ezimgur.view.event.CommentSubmittedEvent;
import com.ezimgur.view.event.UpVoteItemEvent;
import com.google.inject.Inject;
import roboguice.event.EventManager;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/23/12
 * Time: 7:39 PM
 */
public class DialogGalleryItemDetail extends EzDialogFragment{

    public static final String TAG = "EzImgur.DialogGalleryItemDetail";
    @Inject protected EventManager mEventManager;
    private GalleryItem mGalleryItem;
    private boolean mGallerySupportsComments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_ezimgur_Dialog);

        if (getArguments() != null) {
            mGalleryItem = GsonUtils.getGsonInstance().fromJson(getArguments().getString("galleryItemJson"), GalleryItem.class);
            mGallerySupportsComments = getArguments().getBoolean("gallerySupportsComments");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.view_galleryitem_detail, container, false);

        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);

        TextView txtCaption = (TextView) v.findViewById(R.id.v_gi_tv_caption);
        TextView txtAuthor = (TextView) v.findViewById(R.id.v_gi_tv_author);
        final EditText editComment = (EditText) v.findViewById(R.id.v_gi_et_comment);
        Button btnSubmit = (Button) v.findViewById(R.id.v_gi_btn_submit);
        final CheckBox checkUpvote = (CheckBox) v.findViewById(R.id.v_gi_check_upvote);

        txtCaption.setText(mGalleryItem.title);
        String authorName = mGalleryItem.accountUrl == null  ? "anon/reddit user":mGalleryItem.accountUrl;
        txtAuthor.setText("posted by " + authorName);

        if (!EzApplication.isAuthenticated()){
            editComment.setEnabled(false);
            editComment.setHint("login to comment");
        }

        if (!mGallerySupportsComments)
            disableComment("comments not allowed", editComment);
        else if (!EzApplication.isAuthenticated())
            disableComment("login to comment", editComment);

        attachListenerForTextEntry(editComment, checkUpvote, btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCommentToItem(editComment.getText(), checkUpvote.isChecked());
            }
        });


        if (mGalleryItem.accountUrl != null) {
            txtAuthor.setOnClickListener(new View.OnClickListener() {
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
                                    intent.putExtra(CommunityActivity.EXTRA_COMPOSE_TO, mGalleryItem.accountUrl);
                                    getActivity().startActivity(intent);
                                }
                            }).create();
                    dialog.show();
                }
            });
        }

        return v;
    }

    private void disableComment(String disableText, EditText editText) {
        editText.setEnabled(false);
        editText.setHint(disableText);
    }

    private void submitCommentToItem(CharSequence commentText, boolean upvote) {
        final DialogGalleryItemDetail self = this;

        final SubmitCommentTask submitCommentTask = new SubmitCommentTask(getActivity(), mGalleryItem.id, commentText.toString()){
            @Override
            protected void onSuccess(String newCommentId) throws Exception {
                super.onSuccess(newCommentId);
                Toast.makeText(getContext(), "comment submitted successfully with id " + newCommentId, Toast.LENGTH_SHORT).show();
                mEventManager.fire(new CommentSubmittedEvent(newCommentId));
                self.dismiss();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Toast.makeText(getContext(), "unable to comment, please try again.", Toast.LENGTH_SHORT).show();
            }
        };

        if (upvote){
            new VoteOnImageTask(getActivity(), mGalleryItem.id, VoteType.UP) {
                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    submitCommentTask.execute();
                    mEventManager.fire(new UpVoteItemEvent(mGalleryItem.id));
                }

                @Override
                protected void onException(Exception e) throws RuntimeException {
                    super.onException(e);
                    Toast.makeText(getContext(), "unable to upvote/comment image, please try again", Toast.LENGTH_SHORT).show();
                }
            }.execute();

        } else {
            submitCommentTask.execute();
        }
    }

    private void attachListenerForTextEntry(EditText editCaption, final CheckBox checkUpvote, final Button btnSubmit) {
        editCaption.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    btnSubmit.setVisibility(View.VISIBLE);
                    checkUpvote.setVisibility(View.VISIBLE);

                } else {
                    btnSubmit.setVisibility(View.GONE);
                    checkUpvote.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static DialogGalleryItemDetail newInstance(GalleryItem item, boolean gallerySupportsComments) {

        Bundle bundle = new Bundle();
        bundle.putString("galleryItemJson", GsonUtils.getGsonInstance().toJson(item, GalleryItem.class));
        bundle.putBoolean("gallerySupportsComments", gallerySupportsComments);

        DialogGalleryItemDetail dialogFragment = new DialogGalleryItemDetail();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }


}
