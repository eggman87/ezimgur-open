package com.ezimgur.view.listener;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/2/12
 * Time: 7:52 PM
 */
public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 150;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 350;
    private static final String TAG = "EzImgur.SwipeGestureDetector";

    private GestureListener mListener;

    public SwipeGestureDetector(GestureListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (mListener == null) {
            return false;
        }

        try {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
            {
                if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    return mListener.onFlingDown();
                } else if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    return mListener.onFlingUp();
                }
                return false;
            }

            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return mListener.onFlingLeft();
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                return mListener.onFlingRight();
            }


        } catch (Exception e) {
            Log.d("ImgurGestureDetector", "Error onFling", e);
        }

        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (mListener != null && e.getPointerCount() ==1){
            mListener.onLongPress();
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if (mListener != null)
            return mListener.onDoubleTap(e);
        return false;
    }

    public interface GestureListener {
        boolean onFlingUp();
        boolean onFlingDown();
        boolean onFlingLeft();
        boolean onFlingRight();
        void onLongPress();
        boolean onDoubleTap(MotionEvent e);
    }
}
