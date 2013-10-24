package tests.api;

import com.ezimgur.api.GalleryApi;
import com.ezimgur.api.impl.gallery.GalleryApiImpl;
import com.ezimgur.datacontract.*;
import com.ezimgur.instrumentation.Log;
import junit.framework.TestCase;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/14/12
 * Time: 10:37 PM
 */
public class GalleryApiImplTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        Log.LOG_ENABLED = true;
        Log.type = Log.Type.LOG4J;
    }

    public void tearDown() throws Exception {

    }

    public void testGetAlbumFromGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        GalleryAlbum album = apiImpl.getGalleryAlbumById("Q8a1q");

        assertNotNull(album);
    }

    public void testGetImageFromGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        GalleryImage image = apiImpl.getGalleryImageById("KXqnw");

        assertNotNull(image);
    }

    public void testGetGalleryItemFromGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        GalleryItem item = apiImpl.getGalleryItemById("KXqnw");

        assertNotNull(item);
    }

    public void testGetGalleryItemWithCommentsFromGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        GalleryItemComposite item = apiImpl.getGalleryItemWithCommentsById("KXqnw");

        assertNotNull(item);
    }

    public void testGetGalleryItemComments() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        List<Comment> comments = apiImpl.getCommentsForGalleryItem("KXqnw");

        assertNotNull(comments);
        assertTrue(comments.size() > 0);
    }

    public void testAddImageToGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        boolean image = apiImpl.addImageToGallery("hTaxB", "API testing");

        assertNotNull(image);
    }

    public void testUpVoteImageInGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        boolean voteCast = apiImpl.submitVoteForGalleryItem("hTaxB", VoteType.UP);

        assertTrue(voteCast);
    }

    public void testSearchGallery() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        List<GalleryItem> items = apiImpl.searchGallery("do not want");
        assertNotNull(items);
        assertTrue(items.size() > 0);

    }

    public void testLoadGalleryItems() throws Exception {
        GalleryApi apiImpl = new GalleryApiImpl();

        List<GalleryItem> items = apiImpl.getGalleryItems("viral", GallerySort.TOP, 1);
        assertNotNull(items);
        assertTrue(items.size() > 0);
    }


}
