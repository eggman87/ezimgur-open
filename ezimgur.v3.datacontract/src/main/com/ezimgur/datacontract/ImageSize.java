package com.ezimgur.datacontract;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/17/12
 * Time: 10:47 PM
 *
There are 6 total thumbnails that an image can be resized to. Each one is accessable by appending a single character suffix to the end of the image id, and before the file extension. The thumbnails are:

Thumbnail Suffix	Thumbnail Name	Thumbnail Size	Keeps Image Proportions
    s	    Small Square	    90x90	    No
    b	    Big Square	        160x160	    No
    t	    Small Thumbnail	    160x160	    Yes
    m	    Medium Thumbnail	320x320	    Yes
    l	    Large Thumbnail	    640x640	    Yes
    h	    Huge Thumbnail	    1024x1024	Yes
 *
 */
public enum ImageSize {

    ACTUAL_SIZE(""),
    SMALL_SQUARE("s"),
    BIG_SQUARE("b"),
    SMALL_THUMB("t"),
    MEDIUM_THUMB("m"),
    LARGE_THUMB("l"),
    HUGE_THUMB("h");

    private final String suffix;

    ImageSize(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }
}
