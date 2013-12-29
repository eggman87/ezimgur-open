package com.ezimgur.view.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.ezimgur.instrumentation.Log;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/13/12
 * Time: 9:05 PM
 */
public class ViewUtils {

    public static final String javascript = "<script type='text/javascript'> function resizeImage(){ " +
            " var window_height = document.body.clientHeight" +
            " var window_width  = document.body.clientWidth" +
            " var image_width   = document.images[0].width" +
            " var image_height  = document.images[0].height" +
            " var height_ratio  = image_height / window_height" +
            " var width_ratio   = image_width / window_width" +
            "if (height_ratio > width_ratio)" +
            "{" +
            " document.images[0].style.width  = 'auto'" +
            " document.images[0].style.height = '100%'" +
            "}" +
            "else" +
            "{" +
            " document.images[0].style.width  = '100%'" +
            " document.images[0].style.height ='auto'" +
            "}" +
            "}" +
            "</script>";
    public static final String htmlImageFormat = "<html><head>%s</head><body><center><img onLoad='resizeImage()' src='%s' style='margin-top:10%%' /></center></body></html>";

    public static int getScale(Context context, int picWidth, int picHeight, int availableWidth, int availableHeight){

        // account for the thumbnail slider /image info
        //int availableHeight = screenHeight - (screenHeight/2);
        //int availableWidth = screenWidth;
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        picHeight = (int) (picHeight * (metrics.ydpi/160f));
        picWidth = (int) (picWidth * (metrics.xdpi/160f));

        Double val = 0d;
        double neededWidthScale = 0d;
        double neededHeightScale = 0d;
        if (picWidth > availableWidth) {
            neededWidthScale = new Double(availableWidth)/new Double(picWidth);
        }
        double alreadyScaledValue = neededWidthScale > 0 ? neededWidthScale:1;
        if ((picHeight*alreadyScaledValue) > availableHeight){
            neededHeightScale = new Double(availableHeight)/new Double(picHeight);
        }

        val = (neededWidthScale + neededHeightScale) *100d;

        return val.intValue();
    }

    public static int getLayoutWidth(Context context){

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics ();
        display.getMetrics(outMetrics);

        float density  = context.getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        float dpWidth  = outMetrics.widthPixels / density;

        return (int)dpWidth;

    }

    public static String commify(String stringToCommify) {
        String regex = "(\\d)(?=(\\d{3})+$)";
        return stringToCommify.replaceAll(regex, "$1,");
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static boolean isTabletInLandscapeMode(Context context) {
        if (isTablet(context))
        {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(metrics);
            Log.d("EzImgur.ViewUtils", "WIDTH = "+metrics.widthPixels + " HEIGHT="+metrics.heightPixels);
            if (metrics.widthPixels> metrics.heightPixels)
                return true;
        }
        return false;
    }

    public static float getScaleToFitScreenWidth(Context context, int contentWidth){
        int width = getLayoutWidth(context);
        Double val = new Double(width)/new Double(contentWidth);
        return new Float(val );
        //val = val * 100d;
        //return val.intValue();
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float)height / (float)reqHeight);
            } else {
                inSampleSize = Math.round((float)width / (float)reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromFile(String fileUrl, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileUrl, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileUrl, options);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
