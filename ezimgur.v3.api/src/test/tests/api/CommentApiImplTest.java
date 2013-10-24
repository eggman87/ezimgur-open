package tests.api;

import com.ezimgur.api.CommentApi;
import com.ezimgur.api.impl.comment.CommentApiImpl;
import com.ezimgur.datacontract.Comment;
import com.ezimgur.datacontract.VoteType;
import com.ezimgur.instrumentation.Log;
import junit.framework.TestCase;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:16 PM
 */
public class CommentApiImplTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

        Log.LOG_ENABLED = true;
        Log.type = Log.Type.LOG4J;
    }

    public void tearDown() throws Exception {

    }

    public void testGetCommentById() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        Comment comment = commentApi.getCommentById("11833407");

        assertNotNull(comment);
    }

    public void testGetCommentByIdWithReplies() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        Comment comment = commentApi.getCommentWithAllRepliesById("11833407");

        assertNotNull(comment);
        assertNotNull(comment.children);
    }

    public void testAddCommentToImage() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        String commentId = commentApi.addCommentToGalleryItem("KXqnw", "lolwut");

        assertNotNull(commentId);
    }

    public void testAddReplyCommentToImage() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        String replyCommentId = commentApi.addCommentToGalleryItem("KXqnw", "lolwutreply", "11833407");

        assertNotNull(replyCommentId);
    }

    public void testDeleteCommentOnImage() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        boolean deleted = commentApi.deleteCommentById("11833407");

        assertTrue(deleted);
    }

    public void testVoteForComment() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        boolean voteSuccess = commentApi.voteForComment("11833407", VoteType.UP);

        assertTrue(voteSuccess);
    }

    public void testReportComment() throws Exception {
        CommentApi commentApi = new CommentApiImpl();

        boolean reported = commentApi.reportComment("11833407");

        assertTrue(reported);
    }
}
