package com.ezimgur.view.component;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Gallery;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.view.fragment.DialogCommentDetail;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/21/12
 * Time: 10:27 PM
 */
public class CaptionGallery extends Gallery {

    private String mGalleryItemId;
    private List<Comment> mCaptions;
    private FragmentManager mFragmentManager;

    public CaptionGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCaptions(String galleryItemId, List<Comment> captions, FragmentManager fragmentManager) {
        mGalleryItemId = galleryItemId;
        mCaptions = captions;
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);

        View selView = getSelectedView();
        int selPos = getSelectedItemPosition();
        int selIndex = 0;
        int pressedPos = 0;
        int captionPos = 0;

        //find which index is the selected index and which index was pressed.
        for (int i=0; i<getChildCount();i++) {
            View viewInQuestion = getChildAt(i);
            if (viewInQuestion == selView) {
                selIndex = i;
            }
            if (wasPressed(viewInQuestion, e.getX())) {
                pressedPos = i;
            }
        }

        if (pressedPos > selIndex)
            captionPos = selPos + (pressedPos - selIndex);
        else if (pressedPos < selIndex)
            captionPos = selPos - (selIndex - pressedPos);
        else
            captionPos = selPos;

        showCaptionDetails(captionPos);
    }

    private boolean wasPressed(View view, float pressedX) {
        if (view != null && (inBetween(view.getLeft(), view.getWidth() +view.getLeft(), pressedX)))
        {
            return true;
        }
        return false;
    }

    private boolean inBetween(float start, float end, float value) {
        return (value >= start  && value <= end );
    }

    private void showCaptionDetails(int position) {
        DialogCommentDetail dialogFragment = DialogCommentDetail.newInstance(mGalleryItemId, mCaptions.get(position));
        dialogFragment.show(mFragmentManager, "comment_detail");

//        Dialog captionDialog = new Dialog(this.getContext(), R.style.Theme_ezimgur_Dialog);
//        captionDialog.setContentView(R.layout.dialog_caption);
//        captionDialog.setCanceledOnTouchOutside(true);
//
//        Comment caption = mCaptions.get(position);
//
//        TextView txtCaption = (TextView) captionDialog.findViewById(R.id.caption);
//        TextView txtPoints = (TextView) captionDialog.findViewById(R.id.caption_points);
//        TextView txtTimeAgo = (TextView) captionDialog.findViewById(R.id.caption_posted);
//
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date dateTimeAgoActual = new Date();
//        CharSequence timeAgo = "";
//
//        timeAgo = DateUtils.getRelativeTimeSpanString(caption.dateCreated.getTime());
//
//
//        captionDialog.setTitle("from " + caption.author);
//        txtCaption.setText(caption.comment);
//        txtPoints.setText(caption.points + " points");
//        txtTimeAgo.setText(timeAgo);
//
//        captionDialog.show();
    }


}
