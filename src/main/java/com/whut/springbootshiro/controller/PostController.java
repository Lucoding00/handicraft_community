package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.service.PostService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发帖管理
 *
 * @author Lei
 * @date 2024-04-28 23:48
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;


    /**
     * 发帖
     *
     * @param postForm 帖子
     * @return 返回值
     */
    @PostMapping("add")
    public Object add(PostForm postForm) {
        return postService.add(postForm);
    }

    /**
     * 修改帖子
     *
     * @param postForm 帖子
     * @return 返回值
     */
    @PostMapping("update")
    public Object update(PostForm postForm) {
        return postService.update(postForm);
    }


    /**
     * 获取发布的帖子列表
     *
     * @return 返回值
     */
    @PostMapping("reviewPage")
    public Object reviewPage(ReviewQuery reviewQuery) {
        return postService.reviewPage(reviewQuery);
    }

    /**
     * 管理员审核当前的帖子信息，并同意
     *
     * @return 返回值
     */
    @PostMapping("auditPostAgree")
    public Object auditPostAgree(int postId) {
        return postService.auditPost(postId, PostStatusEnum.AGREE);
    }

    /**
     * 管理员审核当前的帖子信息，并不同意
     *
     * @return 返回值
     */
    @PostMapping("auditPostDisagree")
    public Object auditPostDisagree(int postId) {
        return postService.auditPost(postId, PostStatusEnum.DISAGREE);
    }

    /**
     * 软删除帖子
     *
     * @return 返回值
     */
    @PostMapping("deletePost")
    public Object deletePost(int postId) {
        return postService.auditPost(postId, PostStatusEnum.REMOVE);
    }
}
