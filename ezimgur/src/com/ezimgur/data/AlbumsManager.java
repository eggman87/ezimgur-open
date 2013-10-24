package com.ezimgur.data;

import android.content.Context;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Image;
import com.ezimgur.persistance.datasource.AlbumDataSource;
import com.ezimgur.persistance.datasource.AlbumImagesDataSource;
import com.ezimgur.persistance.datasource.ImageDataSource;

import java.util.Date;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/30/12
 * Time: 3:53 PM
 */
public class AlbumsManager {

    private Context mContext;
    //system managed albums..
    public static final String UPLOADS_ID = "ezimgur-uploads";
    public static final String FAVORITES_ID = "ezimgur-favorites";

    public AlbumsManager(Context context) {
        this.mContext = context;
    }

    public void addImageToUploadsAlbum(Image image) {
        AlbumDataSource albumDataSource = AlbumDataSource.newInstance(mContext);
        boolean albumExits = albumDataSource.albumExists(UPLOADS_ID);
        if (albumExits) {
            addImageToAlbum(UPLOADS_ID, image);
        } else {
            Album uploadAlbum = createNewSystemAlbum(UPLOADS_ID, "uploads", image.id);
            albumDataSource.createAlbum(uploadAlbum);

            addImageToAlbum(UPLOADS_ID, image);
        }
    }

    public void addImageToFavoritesAlbum(Image image) {
        AlbumDataSource albumDataSource = AlbumDataSource.newInstance(mContext);
        boolean albumExists = albumDataSource.albumExists(FAVORITES_ID);
        if (albumExists) {
            addImageToAlbum(FAVORITES_ID, image);
        } else {
            Album favoritesAlbum = createNewSystemAlbum(FAVORITES_ID, "favorites", image.id);
            albumDataSource.createAlbum(favoritesAlbum);

            addImageToAlbum(FAVORITES_ID, image);
        }
    }

    public Album getAlbumWithImagesById(String albumId) {
        AlbumDataSource albumDataSource = AlbumDataSource.newInstance(mContext);
        ImageDataSource imageDataSource = ImageDataSource.newInstance(mContext);

        Album album = albumDataSource.getAlbumById(albumId);
        if (album != null)
            album.images = imageDataSource.getImagesByAlbumId(albumId);

        return album;
    }

    public static boolean isLocalAlbum(String albumId) {
        return albumId.equals(UPLOADS_ID) || albumId.equals(FAVORITES_ID);
    }

    /**
     * Asserts that the album already exists.
     * @param albumId
     * @param image
     */
    private void addImageToAlbum(String albumId, Image image) {
        ImageDataSource imageDataSource = ImageDataSource.newInstance(mContext);
        AlbumImagesDataSource albumImageDataSource = AlbumImagesDataSource.newInstance(mContext);

        //creates only create new if does not already exist...
        imageDataSource.createImage(image);
        albumImageDataSource.createAlbumImage(albumId, image.id);
    }

    private Album createNewSystemAlbum(String id, String title, String cover){
        Album album = new Album();
        album.id = id;
        album.cover = cover;
        album.title = title;
        album.dateCreated = new Date(System.currentTimeMillis());
        album.description = "EzImgur system album";
        return album;
    }
}
