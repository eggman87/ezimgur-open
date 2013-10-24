package com.ezimgur.view.builder;

import android.support.v4.app.FragmentManager;
import android.text.style.URLSpan;
import com.ezimgur.view.component.UrlClickableSpan;
import com.ezimgur.view.utils.RichTextUtils;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/24/12
 * Time: 2:13 PM
 */
public class URLSpanConverter implements RichTextUtils.SpanConverter<URLSpan, UrlClickableSpan> {

    @Override
    public UrlClickableSpan convert(URLSpan span) {
        return new UrlClickableSpan(span.getURL());
    }
}
