package com.ezimgur.view.event;

import com.ezimgur.datacontract.GalleryItemComposite;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 2:18 PM
 */
public class GalleryCaptionLoadEvent {

    public GalleryItemComposite composite;
    public boolean success;
    public int targetPostition;

    public GalleryCaptionLoadEvent(GalleryItemComposite composite, boolean success, int targetPostition) {
        this.composite = composite;
        this.success = success;
        this.targetPostition = targetPostition;
    }
}
