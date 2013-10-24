package com.ezimgur.view.builder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 9/30/12
 * Time: 10:12 PM
 */
public class UiBuilder {
    public static View inflate( Context context, int resourceId, ViewGroup root ) {
        LayoutInflater li = ( LayoutInflater )context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        return li.inflate( resourceId, root);
    }
}
