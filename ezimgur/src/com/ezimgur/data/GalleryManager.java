package com.ezimgur.data;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/31/12
 * Time: 4:41 PM
 */
public class GalleryManager {

    public static boolean galleryHasComments(String gallery) {
        return (gallery.equals("gallery") ||
                gallery.equals("viral") ||
                gallery.equals("score") ||
                gallery.equals("time")) ||
                gallery.equals("top") ||
                gallery.equals("new") ||
                gallery.equalsIgnoreCase("random") ||
                gallery.equalsIgnoreCase("hot") ||
                gallery.contains("search - ") ||
                gallery.equals("user");
    }
}
