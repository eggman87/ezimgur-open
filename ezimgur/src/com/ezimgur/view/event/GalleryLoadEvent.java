package com.ezimgur.view.event;

import com.ezimgur.datacontract.GalleryItem;

import java.util.List;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 1:05 PM
 */
public class GalleryLoadEvent {

    public String galleryName;
    public List<GalleryItem> galleryItems;

    public GalleryLoadEvent(String galleryName, List<GalleryItem> items) {
        this.galleryName = galleryName;
        this.galleryItems = items;
    }
}
