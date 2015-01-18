package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import com.ezimgur.R;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.view.fragment.ImageViewerFragment;
import com.ezimgur.view.listener.SwipeGestureDetector;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/2/12
 * Time: 7:38 PM
 */
public class TouchImageView extends ImageView{

    Matrix matrix = new Matrix();
    static final float	 MIN_ZOOM	= 0.9f;
    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    float minScale = 1f;
    float maxScale = 8f;
    float[] m;

    float redundantXSpace, redundantYSpace;

    float width, height;
    static final int CLICK = 3;
    public float saveScale = 1f;
    float right, bottom, origWidth, origHeight, bmWidth, bmHeight;

    ScaleGestureDetector mScaleDetector;

    private OnTouchListener extraTouchListener;
    private OnTouchListener activityTouchListener;

    //private List<String> mMenuItems;
    private String mMenuTitle;
    private boolean mContextMenuEnabled = false;
    private boolean mCenterOnMeasure = false;



    public TouchImageView(Context context) {
        super(context);
        initView(context);
    }

    public TouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TouchImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public void setActivityTouchListener(OnTouchListener listener) {
        activityTouchListener = listener;
    }

    @Override
    public void createContextMenu(ContextMenu menu) {
        super.createContextMenu(menu);
    }

    private void initView(Context context){
        super.setClickable(true);

        new ContextThemeWrapper(getContext(), R.style.Theme_ezimgur_Dialog);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix.setTranslate(1f, 1f);
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);

        setLongClickable(true);

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);

                //forward events to extra listeners.
                if (extraTouchListener != null) {
                    extraTouchListener.onTouch(v, event);
                }
                if (activityTouchListener != null) {
                    activityTouchListener.onTouch(v, event);
                }

                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        last.set(event.getX(), event.getY());
                        start.set(last);
                        mode = DRAG;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            cancelLongPress();
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float scaleWidth = Math.round(origWidth * saveScale);
                            float scaleHeight = Math.round(origHeight * saveScale);
                            if (scaleWidth < width) {
                                deltaX = 0;
                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            } else if (scaleHeight < height) {
                                deltaY = 0;
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);
                            } else {
                                if (x + deltaX > 0)
                                    deltaX = -x;
                                else if (x + deltaX < -right)
                                    deltaX = -(x + right);

                                if (y + deltaY > 0)
                                    deltaY = -y;
                                else if (y + deltaY < -bottom)
                                    deltaY = -(y + bottom);
                            }
                            matrix.postTranslate(deltaX, deltaY);
                            last.set(curr.x, curr.y);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK)
                            performClick();
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                }
                setImageMatrix(matrix);
                invalidate();

                return true; // indicate event was handled

            }

        });
    }
    public void setExtraTouchListener(OnTouchListener listener) {
        extraTouchListener = listener;
    }


    public void setOnFlingListener(SwipeGestureDetector.GestureListener listener) {
        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), new SwipeGestureDetector(listener));
        extraTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

    }

    public void setContextMenuTitle(String title) {
        mMenuTitle = title;
    }

    ImageViewerFragment.ContextMenuProvider mContextMenuProvider;
    public void setContextMenuProvider(ImageViewerFragment.ContextMenuProvider provider){
        mContextMenuProvider = provider;
        mContextMenuEnabled = true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu) {
        if (mContextMenuEnabled) {
            menu.setHeaderTitle(mMenuTitle);

            for (String menuItem : mContextMenuProvider.getMenuItems(false)) {
                menu.add(menuItem);
            }
        }
        super.onCreateContextMenu(menu);
    }

    public boolean onDoubleTap(MotionEvent e) {

        if (saveScale < 1.1f ) {
            mode = ZOOM;
            calculateImageScale(saveScale * 1.5f, e.getX(), e.getY());
            return true;
        }
        else {
            centerImage();
            return true;
        }

    }

    private void centerImage() {
        mode = NONE;
        float scale;
        float scaleX =  (float)width / (float)bmWidth;
        float scaleY = (float)height / (float)bmHeight;
        scale = Math.min(scaleX, scaleY);
        matrix.setScale(scale, scale);
        setImageMatrix(matrix);
        saveScale = 1f;

        // Center the image
        redundantYSpace = (float)height - (scale * (float)bmHeight) ;
        redundantXSpace = (float)width - (scale * (float)bmWidth);
        redundantYSpace /= (float)2;
        redundantXSpace /= (float)2;

        matrix.postTranslate(redundantXSpace, redundantYSpace);

        origWidth = width - 2 * redundantXSpace;
        origHeight = height - 2 * redundantYSpace;
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        setImageMatrix(matrix);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (bm == null)
            return;
        bmWidth = bm.getWidth();
        bmHeight = bm.getHeight();
        centerImage();
    }

    @Override
    public ScaleType getScaleType() {
        return ScaleType.CENTER_INSIDE;
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        bmHeight = drawable.getMinimumHeight();
        bmWidth = drawable.getMinimumWidth();
    }

    private boolean calculateImageScale(float mScaleFactor, float targetX, float targetY) {
        float origScale = saveScale;
        saveScale *= mScaleFactor;
        if (saveScale > maxScale) {
            saveScale = maxScale;
            mScaleFactor = maxScale / origScale;
        } else if (saveScale < minScale) {
            saveScale = minScale;
            mScaleFactor = minScale / origScale;
        }
        right = width * saveScale - width - (2 * redundantXSpace * saveScale);
        bottom = height * saveScale - height - (2 * redundantYSpace * saveScale);
        if (origWidth * saveScale <= width || origHeight * saveScale <= height) {
            matrix.postScale(mScaleFactor, mScaleFactor, width / 2, height / 2);
            if (mScaleFactor < 1) {
                matrix.getValues(m);
                float x = m[Matrix.MTRANS_X];
                float y = m[Matrix.MTRANS_Y];
                if (mScaleFactor < 1) {
                    if (Math.round(origWidth * saveScale) < width) {
                        if (y < -bottom)
                            matrix.postTranslate(0, -(y + bottom));
                        else if (y > 0)
                            matrix.postTranslate(0, -y);
                    } else {
                        if (x < -right)
                            matrix.postTranslate(-(x + right), 0);
                        else if (x > 0)
                            matrix.postTranslate(-x, 0);
                    }
                }
            }
        } else {
            matrix.postScale(mScaleFactor, mScaleFactor, targetX, targetY);
            matrix.getValues(m);
            float x = m[Matrix.MTRANS_X];
            float y = m[Matrix.MTRANS_Y];
            if (mScaleFactor < 1) {
                if (x < -right)
                    matrix.postTranslate(-(x + right), 0);
                else if (x > 0)
                    matrix.postTranslate(-x, 0);
                if (y < -bottom)
                    matrix.postTranslate(0, -(y + bottom));
                else if (y > 0)
                    matrix.postTranslate(0, -y);
            }
        }
        return true;
    }

    public float getMaxZoom()
    {
        return maxScale;
    }

    public void setMaxZoom(float x)
    {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = (float)Math.min(Math.max(.95f, detector.getScaleFactor()), 1.05);
            calculateImageScale(mScaleFactor, detector.getFocusX(), detector.getFocusY());
            return true;
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (mCenterOnMeasure){
            centerImage();
        }
    }

    /**
     * use if you set the image after it is already hidden.
     */
    public void centerImageOnMeasure(boolean centerImageOnMeasure){
        mCenterOnMeasure = centerImageOnMeasure;
    }

    public boolean isZoomedIn() {
        return saveScale > 1f;
    }
}
