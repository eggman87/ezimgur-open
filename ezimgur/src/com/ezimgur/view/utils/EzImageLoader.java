package com.ezimgur.view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import com.ezimgur.R;
import com.ezimgur.instrumentation.Log;
import com.nostra13.universalimageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 7:41 PM
 */
public class EzImageLoader {

    private static final String TAG = "EzImgur.EzImageLoader";
    private static boolean initialized = false;

    public static ImageLoader getImageLoaderInstance(Context context){
        if (!initialized) {
            return initImageLoader(context);
        }
        return ImageLoader.getInstance();
    }

    public static ImageLoader initImageLoader(Context context) {
        Log.d(TAG, "initImageLoaderStart");
        ImageLoader imageLoader = ImageLoader.getInstance();

        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true)
                //.bitmapConfig(Bitmap.Config.RGB_565)
                //.cacheInMemory(false)
                //.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();

        File cacheDir;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory(), "data/ezimgur/cache");
        } else {
            cacheDir = context.getCacheDir();
        }
        cacheDir.mkdirs();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .discCache(new TotalSizeLimitedDiscCache(cacheDir, (6 * 1024 * 1024)))
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
        imageLoader.init(config);

        Log.d(TAG, "initImageLoaderEnd");
        initialized = true;
        return imageLoader;
    }
}
