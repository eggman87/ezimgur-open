package com.ezimgur.api.impl.album;

import com.ezimgur.api.AlbumApi;
import com.ezimgur.api.ImgurApiConstants;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.album.request.FavoriteAlbumRequest;
import com.ezimgur.api.impl.album.request.GetAlbumRequest;
import com.ezimgur.api.impl.album.request.GetImagesForAlbumRequest;
import com.ezimgur.datacontract.Album;
import com.ezimgur.datacontract.Image;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 7:35 PM
 */
public class AlbumApiImpl extends ApiBase implements AlbumApi {

    @Override
    public Album getAlbumById(String albumId) throws ApiException{

        GetAlbumRequest request = new GetAlbumRequest(albumId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<Image> getImagesForAlbum(String albumId) throws ApiException{

        GetImagesForAlbumRequest request = new GetImagesForAlbumRequest(albumId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Image getImageFromAlbum(String imageId, String albumId) {
        return null;
    }

    @Override
    public boolean createNewAlbum(String title, String description, String cover, List<String> imageIds) {
        return false;
    }

    @Override
    public boolean updateAlbum(String title, String description, String cover, List<String> imageIds) {
        return false;
    }

    @Override
    public boolean deleteAlbum(String albumId) {
        return false;
    }

    @Override
    public boolean setAlbumImages(List<String> imageIds) {
        return false;
    }

    @Override
    public boolean addAlbumImages(List<String> imageIds) {
        return false;
    }

    @Override
    public boolean removeImagesFromAlbum(List<String> imagesId) {
        return false;
    }

    @Override
    public String getHttpUrlForAlbum(String albumId) {
        return String.format(ImgurApiConstants.URL_ALBUM_FORMAT, albumId);
    }

    @Override
    public void favoriteAlbum(String albumId) throws ApiException{
        FavoriteAlbumRequest request = new FavoriteAlbumRequest(albumId);
        submitApiRequest(request);
    }
}
