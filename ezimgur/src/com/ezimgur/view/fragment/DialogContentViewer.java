package com.ezimgur.view.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ezimgur.R;
import com.ezimgur.view.activity.BaseActivity;
import com.ezimgur.view.component.TouchImageView;
import com.ezimgur.view.component.TouchWebView;
import com.ezimgur.view.event.OnTaskLoadEvent;
import com.ezimgur.view.utils.EzImageLoader;
import com.google.inject.Inject;
import com.nostra13.universalimageloader.core.ImageLoader;
import roboguice.event.EventManager;
import roboguice.fragment.RoboDialogFragment;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/24/12
 * Time: 3:07 PM
 */
public class DialogContentViewer extends RoboDialogFragment {

    @Inject protected EventManager mEventManager;
    private String mImageUrl;
    private boolean mForceWeb;

    private static final String TAG = "EzImgur.DialogContentViewer";

    public static DialogContentViewer newInstance(String imageUrl, boolean forceInWeb) {
        DialogContentViewer dialogFragment = new DialogContentViewer();

        Bundle bundle = new Bundle();
        bundle.putString("image_url", imageUrl);
        bundle.putBoolean("force_web", forceInWeb);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    public static DialogContentViewer newInstance(String imageUrl) {
        DialogContentViewer dialogFragment = new DialogContentViewer();

        Bundle bundle = new Bundle();
        bundle.putString("image_url", imageUrl);
        bundle.putBoolean("force_web", false);
        dialogFragment.setArguments(bundle);

        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_ezimgur_TransparentDialog);

        if (getArguments() != null) {
            mImageUrl = getArguments().getString("image_url");
            mForceWeb = getArguments().getBoolean("force_web");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.dialog_imageview, container, false);

        final TouchWebView webView = (TouchWebView) v.findViewById(R.id.twv_dlg_webview);
        final TouchImageView imageView = (TouchImageView) v.findViewById(R.id.tiv_dlg_imageview);

        imageView.centerImageOnMeasure(true);

        String lastCharacters = mImageUrl.substring(mImageUrl.length() - 4, mImageUrl.length());
        boolean isImage = false;
        if ((lastCharacters.equals(".jpg") || lastCharacters.equals(".png")) && !mForceWeb)
            isImage = true;


        if (isImage) {
            webView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

            ImageLoader loader = EzImageLoader.getImageLoaderInstance(getActivity());
            loader.displayImage(mImageUrl, imageView, BaseActivity.getDefaultImageLoadingListener(mEventManager));

        } else {
            mEventManager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_STARTED));
            imageView.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl(mImageUrl);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    mEventManager.fire(new OnTaskLoadEvent(OnTaskLoadEvent.TaskLoading.LOAD_FINISHED));
                }
            });
        }

        getDialog().setCanceledOnTouchOutside(true);

        return v;
    }
}
