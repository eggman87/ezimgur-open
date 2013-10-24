package com.ezimgur.view.event;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 9:07 PM
 */
public class ChangeDaysAgoEvent {
    private int mDaysAgo;

    public ChangeDaysAgoEvent(int daysAgo) {
        mDaysAgo = daysAgo;
    }

    public int getDaysAgo(){
        return mDaysAgo;
    }
}
