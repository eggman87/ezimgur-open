package com.ezimgur.view.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import com.ezimgur.R;
import com.ezimgur.view.fragment.ImageViewerFragment;
import com.ezimgur.view.listener.SwipeGestureDetector;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/11/12
 * Time: 6:46 PM
 */
public class TouchWebView extends WebView {

    private OnTouchListener extraTouchListener;
    private AttributeSet mAttrs;
    //private List<String> mMenuItems;
    private String mMenuTitle;
    private boolean mContextMenuEnabled = false;

    public TouchWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(R.color.abs__background_holo_dark);
    }

    public void setOnFlingListener(SwipeGestureDetector.GestureListener listener) {
        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), new SwipeGestureDetector(listener));
        extraTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
        setOnTouchListener(extraTouchListener);
    }

//    public void setContextMenuItems(List<String> menuItems) {
//        mMenuItems = menuItems;
//        mContextMenuEnabled = true;
//    }

    private ImageViewerFragment.ContextMenuProvider mContextMenuProvider;
    public void setmContextMenuProvider(ImageViewerFragment.ContextMenuProvider contextMenuProvider){
        mContextMenuProvider = contextMenuProvider;
        mContextMenuEnabled = true;
    }

    public void setContextMenuTitle(String title) {
        mMenuTitle = title;
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
