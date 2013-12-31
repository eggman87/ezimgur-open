package com.ezimgur.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ezimgur.view.utils.GifDecoder;
import sun.net.www.content.image.gif;


/*
    Majority of this source code was created by Felipe Lima. More information on Felipe can be found here:
        http://felipecsl.com/blog/2013/08/20/animated-gifs-with-android/
 */
public class GifDecoderView extends TouchImageView implements Runnable {

    private static final String TAG = "GifDecoderView";
    private GifDecoder gifDecoder;
    private Bitmap mTmpBitmap;
    private final Handler handler = new Handler();
    private boolean animating = false;
    private Thread animationThread;
    private final Runnable updateResults = new Runnable() {
        @Override
        public void run() {
            if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
                setImageBitmap(mTmpBitmap);
            }
        }
    };

    public GifDecoderView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public GifDecoderView(final Context context) {
        super(context);
    }

    public void setBytes(final byte[] bytes) {

        gifDecoder = new GifDecoder();
        gifDecoder.read(bytes);

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    /**
     * use this is if you want to create the gif decoder and read all bytes off the UI thread.
     * @param gifDecoder gif decoder with bytes already read.
     */
    public void setGifDecoder(final GifDecoder gifDecoder) {
        this.gifDecoder = gifDecoder;
        gifDecoder.advance();
        setImageBitmap(gifDecoder.getNextFrame());
    }

    public void startAnimation() {
        animating = true;

        if (canStart()) {
            animationThread = new Thread(this);
            animationThread.start();
        }
    }

    public void stopAnimation() {
        animating = false;
        gifDecoder = null;

        if (animationThread != null) {
            animationThread.interrupt();
            setImageBitmap(null);
            mTmpBitmap = null;
            animationThread = null;

        }
    }

    private boolean canStart() {
        return animating && gifDecoder != null && animationThread == null;
    }

    @Override
    public void run() {
        final int n = gifDecoder.getFrameCount();
        do {
            for (int i = 0; i < n; i++) {
                try {
                    if (gifDecoder != null) {
                        mTmpBitmap = gifDecoder.getNextFrame();
                        handler.post(updateResults);
                    }
                } catch (final ArrayIndexOutOfBoundsException e) {
                    // suppress
                }
                try {
                    if (gifDecoder != null)
                        gifDecoder.advance();
                } catch (ArithmeticException e) {
                    // suppress
                }
                try {
                    if (gifDecoder != null)
                        Thread.sleep(gifDecoder.getNextDelay());
                } catch (final InterruptedException e) {
                    // suppress
                } catch (IllegalArgumentException e) {
                    // suppress
                } catch (IndexOutOfBoundsException e) {
                    // suppress
                }
            }
        } while (animating);
    }
}