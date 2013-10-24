package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/2/12
 * Time: 10:56 PM
 */
public class OutlineTextView extends TextView {

    private static final float OUTLINE_PROPORTION = 0.1f;

    private final Paint mStrokePaint = new Paint();
    private int mOutlineColor = Color.TRANSPARENT;

    public OutlineTextView(Context context) {
        super(context);
    }

    public OutlineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void draw(Canvas canvas) {
        super.onDraw(canvas);
        // Get the text to print
        final float textSize = super.getTextSize();
        final String text = super.getText().toString();

        Paint strokePaint = new Paint();
        strokePaint.setARGB(255, 0, 0, 0);
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setTextSize(16);
        strokePaint.setTypeface(Typeface.DEFAULT_BOLD);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);

        Paint textPaint = new Paint();
        textPaint.setARGB(255, 255, 255, 255);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(16);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        canvas.drawText("BAHABAHABAJ", 100, 100, strokePaint);
        canvas.drawText(text, 100, 100, textPaint);

        super.draw(canvas);
    }



}
