package com.ezimgur.api.impl.gallery;

import com.ezimgur.api.GalleryApi;
import com.ezimgur.api.exception.ApiException;
import com.ezimgur.api.impl.ApiBase;
import com.ezimgur.api.impl.gallery.request.*;
import com.ezimgur.datacontract.*;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 9:28 PM
 */
public class GalleryApiImpl extends ApiBase implements GalleryApi {

    @Override
    public GalleryImage getGalleryImageById(String imageId) throws ApiException {

        LoadGalleryImageRequest request = new LoadGalleryImageRequest(imageId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public GalleryAlbum getGalleryAlbumById(String albumId) throws ApiException {

        LoadGalleryAlbumRequest request = new LoadGalleryAlbumRequest(albumId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public GalleryItem getGalleryItemById(String galleryItemId) throws ApiException {

        LoadGalleryItemRequest request = new LoadGalleryItemRequest(galleryItemId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public GalleryItemComposite getGalleryItemWithCommentsById(String galleryItemId) throws ApiException {

        LoadGalleryItemRequest request = new LoadGalleryItemRequest(galleryItemId);
        submitApiRequest(request);

        GalleryItemComposite item = new GalleryItemComposite();
        item.galleryItem = request.getItemToReceive().data;
        item.comments = this.getCommentsForGalleryItem(item.galleryItem.id);

        return item;
    }

    @Override
    public boolean addImageToGallery(String imageId, String title) throws ApiException {

        AddGalleryItemRequest request = new AddGalleryItemRequest(imageId, title);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public boolean addAlbumToGallery(String albumId, String title) throws ApiException {

        AddGalleryItemRequest request = new AddGalleryItemRequest(albumId, title);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<GalleryItem> searchGallery(String searchTerms) throws ApiException {

        SearchGalleryRequest request = new SearchGalleryRequest(searchTerms);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<GalleryItem> getGalleryItems(String galleryName, GallerySort sort, int page) throws ApiException {

        LoadGalleryRequest request = new LoadGalleryRequest(galleryName, sort, page);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Vote reportGalleryItem(String galleryItemId) throws ApiException{

        ReportGalleryItemRequest request = new ReportGalleryItemRequest(galleryItemId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Vote getVotesForGalleryItem(String galleryItemId) throws ApiException{

        LoadGalleryItemVotesRequest request = new LoadGalleryItemVotesRequest(galleryItemId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public boolean submitVoteForGalleryItem(String galleryItemId, VoteType type) throws ApiException{

        VoteGalleryItemRequest request = new VoteGalleryItemRequest(galleryItemId, type);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public List<Comment> getCommentsForGalleryItem(String galleryItemId) throws ApiException {

        LoadGalleryItemCommentsRequest request = new LoadGalleryItemCommentsRequest(galleryItemId);
        submitApiRequest(request);

        return request.getItemToReceive().data;
    }

    @Override
    public Comment submitCommentForGalleryItem(String galleryItemId, String commentText) throws ApiException {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public Comment submitReplyCommentForGalleryItem(String galleryItemId, String parentCommentId, String commentText) throws ApiException {
        throw new UnsupportedOperationException("not implemented");
    }
}
