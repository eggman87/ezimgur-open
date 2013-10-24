package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 9/30/12
 * Time: 5:40 PM
 */
public class VerticalTextView extends TextView {
    final boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        final int gravity = getGravity();
        if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
            setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
            topDown = false;
        }else
            topDown = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b){
        return super.setFrame(l, t, l+(b-t), t+(r-l));
    }

    @Override
    public void draw(Canvas canvas){
        if(topDown){
            canvas.translate(getHeight(), 0);
            canvas.rotate(90);
        }else {
            canvas.translate(0, getWidth());
            canvas.rotate(-90);
        }

        canvas.clipRect(0, 0, getWidth(), getHeight(), Region.Op.REPLACE);
        canvas.save();
        super.draw(canvas);
    }
}
