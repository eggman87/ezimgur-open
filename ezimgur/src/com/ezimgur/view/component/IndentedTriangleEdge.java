package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.ezimgur.R;

/**
 * Created by NCR Corporation.
 * User: matthewharris
 * Date: 4/28/13
 * Time: 1:42 PM
 */
public class IndentedTriangleEdge extends View {

    /**
     *
     */
    Paint trianglePaint;

    public IndentedTriangleEdge(Context context){
        super(context);
        commonConstructor(context);
    }

    public IndentedTriangleEdge(Context context, AttributeSet attrs) {
        super(context, attrs);
        commonConstructor(context);
    }

    /**
     * @param context
     */
    private void commonConstructor(Context context) {
        trianglePaint = new Paint();
        //trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setColor(getResources().getColor(R.color.ezimgur_red_slight_transparent));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();

        Point p1 = new Point(0, 0);
        Point p2 = new Point(width, 0);
        Point midpoint = new Point(0, height / 2);

        Path topPath = new Path();
        topPath.moveTo(p1.x, p1.y);
        topPath.lineTo(p2.x, p2.y);
        topPath.lineTo(midpoint.x, midpoint.y);

        Point p4 = new Point(0, height);
        Point p5 = new Point(width, height);
        Path bottomPath = new Path();
        bottomPath.moveTo(p4.x, p4.y);
        bottomPath.lineTo(p5.x, p5.y);
        bottomPath.lineTo(midpoint.x, midpoint.y);

        canvas.drawPath(topPath, trianglePaint);
        canvas.drawPath(bottomPath, trianglePaint);
    }
}
