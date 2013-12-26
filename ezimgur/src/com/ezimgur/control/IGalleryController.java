package com.ezimgur.control;

import android.content.Context;
import com.ezimgur.datacontract.GalleryItemComposite;
import com.ezimgur.datacontract.GallerySort;
import roboguice.event.EventManager;

/**
 * Copyright NCR Inc,
 * User: matthewharris
 * Date: 12/25/13
 * Time: 12:55 PM
 */
public interface IGalleryController {

    void onCreate(Context context, EventManager manager);

    void onResume(Context context);

    void onPause(Context context);

    void loadGallery(final String galleryName,final int pageNumber, GallerySort sort);

    void loadGallery(final String galleryName,final int pageNumber, GallerySort sort, boolean overrideSort, final boolean makeDefault);

    void loadCaptions(final GalleryItemComposite item, int targetPosition);
}
