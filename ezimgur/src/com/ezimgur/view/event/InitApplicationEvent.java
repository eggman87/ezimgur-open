package com.ezimgur.view.event;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 11/18/12
 * Time: 11:06 AM
 */
public class InitApplicationEvent {
    private boolean mSuccess;

    public InitApplicationEvent(boolean wasSuccessful){
        mSuccess = wasSuccessful;
    }

    public boolean wasInitSuccessful() {
        return mSuccess;
    }
}
