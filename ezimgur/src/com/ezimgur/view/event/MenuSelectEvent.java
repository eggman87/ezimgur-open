package com.ezimgur.view.event;

import android.app.Activity;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/30/12
 * Time: 8:19 PM
 */
public class MenuSelectEvent <T extends Activity>{

    private Class<T> activityToLaunch;

    public  MenuSelectEvent(Class<T> activityToLaunch) {
        this.activityToLaunch = activityToLaunch;
    }

    public Class<T> getActivityToLaunch() {
        return activityToLaunch;
    }
}
