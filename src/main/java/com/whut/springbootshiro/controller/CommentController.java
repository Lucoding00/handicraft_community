package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.CommentForm;
import com.whut.springbootshiro.form.InterestForm;
import com.whut.springbootshiro.query.PostCommentsQuery;
import com.whut.springbootshiro.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lei
 * @date 2024-04-29 23:34
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;


    /**
     * 添加评论
     *
     * @param commentForm 评论实体
     * @return 返回值
     */
    @PostMapping("addComment")
    public Object addComment(CommentForm commentForm) {
        return commentService.addComment(commentForm);
    }


    /**
     * 管理员删除评论
     *
     * @param commentId 评论id
     * @return 返回值
     */
    @PostMapping("deleteComment")
    public Object deleteComment(int commentId) {
        return commentService.deleteComment(commentId);
    }



    /**
     * 获取到评论的分页
     *
     * @param postCommentsQuery 帖子信息的查询
     * @return 返回值
     */
    @PostMapping("postComments")
    public Object postComments(PostCommentsQuery postCommentsQuery) {
        return commentService.postComments(postCommentsQuery);
    }





}
