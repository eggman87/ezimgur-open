package com.ezimgur.task;

import android.content.Context;
import android.widget.Toast;
import com.ezimgur.api.CommentApi;
import com.ezimgur.datacontract.VoteType;
import com.google.inject.Inject;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/22/12
 * Time: 11:15 AM
 */
public class VoteOnCommentTask extends LoadingTask<Boolean> {

    @Inject CommentApi mCommentApi;
    private String mCommentId;
    private VoteType mVoteType;

    protected VoteOnCommentTask(Context context, String commentId, VoteType voteType) {
        super(context);
        mCommentId = commentId;
        mVoteType = voteType;
    }

    @Override
    public Boolean call() throws Exception {
        return mCommentApi.voteForComment(mCommentId, mVoteType);
    }

    @Override
    protected void onException(Exception e) throws RuntimeException {
        super.onException(e);
        Toast.makeText(getContext(), "unable to vote for comment : "+e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
