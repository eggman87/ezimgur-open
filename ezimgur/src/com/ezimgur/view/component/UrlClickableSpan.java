package com.ezimgur.view.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.style.ClickableSpan;
import android.view.ContextThemeWrapper;
import android.view.View;
import com.ezimgur.instrumentation.Log;
import com.ezimgur.view.fragment.DialogContentViewer;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/24/12
 * Time: 2:05 PM
 */
public class UrlClickableSpan extends ClickableSpan {

    private static final String TAG = "EzImgur.UrlClickableSpan";

    public String mUrl;
    public UrlClickableSpan(String url) {
        mUrl = url;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "url clickable span clicked.");

        FragmentManager manager = null;
        Context initialContext = view.getContext();
        FragmentActivity act = null;
        if (initialContext instanceof  FragmentActivity) {
            act = (FragmentActivity) initialContext;
            manager = act.getSupportFragmentManager();
        }
        else if (initialContext instanceof ContextThemeWrapper) {
            Context contextToUse = getFragmentActivity(initialContext);
            act = (FragmentActivity) contextToUse;
            manager = act.getSupportFragmentManager();
        }

        //http://youtu.be/NOSKkWSiDFk?t=2m8s
        if (act != null && (mUrl.contains("youtube") || mUrl.contains("youtu.be"))){
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
            act.startActivity(youtubeIntent);
        } else {
            DialogContentViewer dialog = DialogContentViewer.newInstance(mUrl);
            dialog.show(manager, TAG);
        }
    }

    private Context getFragmentActivity (Context context) {
        if (context instanceof  FragmentActivity) {
            return  context;

        } else {
            Context contextThemeBase = ((ContextThemeWrapper) context).getBaseContext();
            return getFragmentActivity(contextThemeBase);
        }
    }
}
