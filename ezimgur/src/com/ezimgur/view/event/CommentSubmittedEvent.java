package com.ezimgur.view.event;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/23/12
 * Time: 10:06 PM
 */
public class CommentSubmittedEvent {
    private String mCommentId;

    public CommentSubmittedEvent(String newCommentId) {
        mCommentId = newCommentId;
    }

    public String getmCommentId() {
        return mCommentId;
    }
}
