package com.ezimgur.view.event;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 7:07 PM
 */
public class AlbumTotalCountEvent {

    public int totalCount;
    public int currentIndex;
    public String albumId;

    public AlbumTotalCountEvent(String albumId, int currentIndex, int count) {
        this.albumId = albumId;
        this.currentIndex = currentIndex;
        this.totalCount = count;
    }
}
