package com.ezimgur.datacontract;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 9:26 PM
 */
public enum GallerySort {

    TOP,
    TIME;

    /**
     * Used to turn the sort into the actual URL sort string - the sort is different for subreddits in comparison to
     * imgur gallery.
     * @param sort sort by hot or new
     * @param isImgur is the gallery name a imgur gallery?
     * @return a string that can be used in a request url
     */
    public static String getSortStringForTargetType(GallerySort sort, boolean isImgur) {
        if (isImgur){
            if (sort == TOP)
                return "viral";
            else
                return "time";
        }
        else return sort.toString().toLowerCase();
    }
}

