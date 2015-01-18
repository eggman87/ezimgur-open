package com.ezimgur.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;
import com.ezimgur.view.fragment.ImageViewerFragment;
import com.ezimgur.view.listener.SwipeGestureDetector;

/**
 * Created by mharris on 1/16/15.
 *
 */
public class TouchVideoView extends VideoView {

    private OnTouchListener extraTouchListener;
    private String mMenuTitle;
    private boolean mContextMenuEnabled = false;

    public TouchVideoView(Context context) {
        super(context);
    }

    public TouchVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public void setOnFlingListener(final SwipeGestureDetector.GestureListener listener) {
        setLongClickable(true);

        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), new SwipeGestureDetector(listener));

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    private ImageViewerFragment.ContextMenuProvider mContextMenuProvider;
    public void setContextMenuProvider(ImageViewerFragment.ContextMenuProvider contextMenuProvider){
        mContextMenuProvider = contextMenuProvider;
        mContextMenuEnabled = true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu) {
        if (mContextMenuEnabled) {
            menu.setHeaderTitle(mMenuTitle);

            for (String menuItem : mContextMenuProvider.getMenuItems(true)) {
                menu.add(menuItem);
            }
        }
        super.onCreateContextMenu(menu);
    }

}
