package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.CommentForm;
import com.whut.springbootshiro.query.PostCommentsQuery;

/**
 * @author Lei
 * @date 2024-04-29 23:34
 */
public interface CommentService {
    Result addComment(CommentForm commentForm);

    Result deleteComment(int commentId);

    Result postComments(PostCommentsQuery postCommentsQuery);
}
