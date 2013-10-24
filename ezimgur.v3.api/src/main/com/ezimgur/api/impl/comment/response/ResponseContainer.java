package com.ezimgur.api.impl.comment.response;

import com.ezimgur.datacontract.Basic;
import com.ezimgur.datacontract.Comment;

import java.util.List;

/**
 * Created by EggmanProjects.
 * User: matthewharris
 * Date: 12/16/12
 * Time: 3:10 PM
 */
public class ResponseContainer {
    public class CommentContainer extends Basic<Comment> {

    }

    public class CommentWithRepliesContainer extends Basic<List<Comment>> {

    }

    public class AddCommentResponseContainer extends Basic<BasicIdResponse> {

    }

    public class DeleteCommentResponseContainer extends Basic<Boolean> {

    }

    public class VoteCommentResponseContainer extends Basic<Boolean> {

    }

    public class ReportCommentResponseContainer extends Basic<Boolean> {

    }

    //1393 [main] DEBUG ezimgur  - EzImgur.HttpConnection | Response JSON: {"data":{"id":"12191905"},"success":true,"status":200}
    public class BasicIdResponse {
        public String id;
    }
}
