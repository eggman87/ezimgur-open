package com.ezimgur.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ezimgur.R;
import com.ezimgur.app.EzApplication;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.VoteType;
import com.ezimgur.task.SubmitCommentTask;
import com.ezimgur.task.VoteOnCommentTask;
import com.ezimgur.view.activity.CommunityActivity;
import com.ezimgur.view.event.CommentSubmittedEvent;
import com.google.inject.Inject;
import roboguice.event.EventManager;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/23/12
 * Time: 12:51 AM
 */
public class DialogCommentDetail extends EzDialogFragment {

    public static final String TAG = "EzImgur.DialogCommentDetail";
    @Inject protected EventManager mEventManager;
    private Comment mComment;
    private String mGalleryItemId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_TITLE, R.style.Theme_ezimgur_Dialog);

        if (getArguments() != null) {
            Bundle args = getArguments();

            mComment = new Comment();
            mGalleryItemId = args.getString("galleryItemId");
            mComment.id = args.getString("id");
            mComment.vote = args.getString("vote");
            mComment.author = args.getString("author");
            mComment.comment = args.getString("comment");
            mComment.dateCreated = new Date(args.getLong("dateCreated"));
            mComment.downs = args.getInt("downs");
            mComment.ups = args.getInt("ups");
            mComment.points = args.getInt("points");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.view_comment_detail, container, false);

        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(true);


        TextView txtCaption = (TextView) v.findViewById(R.id.v_cd_tv_caption);
        TextView txtPosted = (TextView) v.findViewById(R.id.v_cd_tv_posted);
        TextView txtAuthor = (TextView) v.findViewById(R.id.v_cd_tv_author);
        TextView txtPoints = (TextView) v.findViewById(R.id.v_cd_tv_points);
        final EditText editCaption = (EditText) v.findViewById(R.id.v_cd_et_comment);
        Button btnSubmit = (Button) v.findViewById(R.id.v_cd_btn_submit);
        final CheckBox checkUpvote = (CheckBox) v.findViewById(R.id.v_cd_check_upvote);

        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(mComment.dateCreated.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);

        if (!EzApplication.isAuthenticated()){
            editCaption.setEnabled(false);
            editCaption.setHint("login to reply");
        }

        txtCaption.setText(mComment.comment);
        txtPosted.setText(timeAgo);
        txtAuthor.setText(mComment.author);
        txtPoints.setText(mComment.points + " points");

        attachListenerForTextEntry(editCaption, checkUpvote, btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCommentReply(editCaption.getText(), checkUpvote.isChecked());
            }
        });

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
                                intent.putExtra(CommunityActivity.EXTRA_COMPOSE_TO, mComment.author);
                                getActivity().startActivity(intent);
                            }
                        }).create();
                dialog.show();
            }
        });



        return v;
    }

    private void submitCommentReply(CharSequence commentText, boolean upVote) {
        final DialogCommentDetail self = this;
        final SubmitCommentTask submitCommentTask = new SubmitCommentTask(getActivity(), mGalleryItemId, commentText.toString(),  mComment.id){

            @Override
            protected void onSuccess(String newCommentId) throws Exception {
                super.onSuccess(newCommentId);
                Toast.makeText(getContext(), "reply created with id " + newCommentId, Toast.LENGTH_SHORT).show();
                mEventManager.fire(new CommentSubmittedEvent(newCommentId));
                self.dismiss();
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                super.onException(e);
                Toast.makeText(getContext(), "submitting reply failed, please try again", Toast.LENGTH_SHORT).show();
            }
        };

        if (upVote){
            new VoteOnCommentTask(getActivity(), mComment.id, VoteType.UP) {
                @Override
                protected void onSuccess(Boolean aBoolean) throws Exception {
                    super.onSuccess(aBoolean);
                    submitCommentTask.execute();
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

    public static DialogCommentDetail newInstance(String galleryItemId, Comment comment) {

        Bundle bundle = new Bundle();
        bundle.putString("galleryItemId", galleryItemId);
        bundle.putString("id", comment.id);
        bundle.putString("vote", comment.vote);
        bundle.putString("author", comment.author);
        bundle.putString("comment", comment.comment);
        bundle.putLong("dateCreated", comment.dateCreated.getTime());
        bundle.putInt("downs", comment.downs);
        bundle.putInt("ups", comment.ups);
        bundle.putInt("points", comment.points);

        DialogCommentDetail dialogFragment = new DialogCommentDetail();
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }
}
