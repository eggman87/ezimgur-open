package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:38 PM
 */
public class FixedSlidingDrawer extends SlidingDrawer {

    private ViewGroup mHandleLayout;
    private final Rect mHitRect = new Rect();
    private static final String TAG_CLICK_INTERCEPTED = "click_intercepted";

    public FixedSlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedSlidingDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthMeasureSpec == MeasureSpec.UNSPECIFIED)
            widthMeasureSpec = MeasureSpec.AT_MOST;
        if (heightMeasureSpec == MeasureSpec.UNSPECIFIED)
            heightMeasureSpec = MeasureSpec.AT_MOST;

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View handle = getHandle();

        if (handle instanceof ViewGroup) {
            mHandleLayout = (ViewGroup) handle;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        if (mHandleLayout != null)
        {
            int clickX = (int) (event.getX() - mHandleLayout.getLeft());
            int clickY = (int) (event.getY() - mHandleLayout.getTop());

            if (isAnyClickableChildHit(mHandleLayout, clickX, clickY))
            {
                return false;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    private boolean isAnyClickableChildHit(ViewGroup viewGroup, int clickX, int clickY)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View childView = viewGroup.getChildAt(i);

            if (TAG_CLICK_INTERCEPTED.equals(childView.getTag()))
            {
                childView.getHitRect(mHitRect);

                if (mHitRect.contains(clickX, clickY))
                {
                    return true;
                }
            }

            if (childView instanceof ViewGroup && isAnyClickableChildHit((ViewGroup) childView, clickX, clickY))
            {
                return true;
            }
        }
        return false;
    }
}
