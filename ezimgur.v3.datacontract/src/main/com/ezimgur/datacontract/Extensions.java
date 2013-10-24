package com.ezimgur.datacontract;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 1/19/13
 * Time: 6:20 PM
 */
public class Extensions {

    public static GalleryItem albumToItem(Album album) {
        GalleryItem item = new GalleryItem();
        item.isAlbum = true;
        item.title = album.title;
        item.dateCreated = album.dateCreated;
        item.id = album.id;
        item.description = album.description;

        return item;
    }

    public static GalleryItem imageToItem(Image image) {
        GalleryItem item = new GalleryItem();
        item.id = image.id;
        item.title = image.title;
        item.description = image.description;
        item.dateCreated = image.dateCreated;

        return item;
    }
}
