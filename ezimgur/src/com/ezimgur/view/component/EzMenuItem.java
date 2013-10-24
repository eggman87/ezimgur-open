package com.ezimgur.view.component;

import android.content.Context;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 7:11 PM
 */
public class EzMenuItem {

    public int id;
    public String title;
    public boolean selected;
    public Class activityToLaunch;

    public EzMenuItem (int id, String title) {
        this.id  = id;
        this.title = title;
        this.selected = false;
    }

    public EzMenuItem(int id, String title, boolean selected, Class activityToLaunch) {
        this.id  = id;
        this.title = title;
        this.selected = selected;
        this.activityToLaunch = activityToLaunch;
    }

}
