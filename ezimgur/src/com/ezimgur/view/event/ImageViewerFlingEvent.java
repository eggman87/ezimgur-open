package com.ezimgur.view.event;

/**
 * EggmanProjects
 * Author: Matthew Harris
 * Date: 10/3/12
 * Time: 9:57 AM
 */
public class ImageViewerFlingEvent {
    private ImageViewerFling mFlingType;

    public ImageViewerFlingEvent(ImageViewerFling type) {
        mFlingType = type;
    }

    public ImageViewerFling getmFlingType() {
        return mFlingType;
    }

    public enum ImageViewerFling {
        RIGHT,
        LEFT
    }
}
