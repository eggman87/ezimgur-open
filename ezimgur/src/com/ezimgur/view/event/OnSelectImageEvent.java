package com.ezimgur.view.event;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/3/12
 * Time: 9:56 AM
 */
public class OnSelectImageEvent {
    private int mPosition;
    private boolean mIsAlbum;
    private boolean mIsFling;

    public OnSelectImageEvent(int position) {
        mPosition = position;
    }

    public OnSelectImageEvent(int position, boolean isAlbum) {
        mPosition = position;
        mIsAlbum = isAlbum;
    }

    public OnSelectImageEvent(int position, boolean isAlbum, boolean isFling) {
        mPosition = position;
        mIsAlbum = isAlbum;
        mIsFling = isFling;
    }

    public int getPosition() {
        return mPosition;
    }

    public boolean isAlbumChange() {
        return mIsAlbum;
    }

    public boolean isFling() {
        return mIsFling;
    }
}
