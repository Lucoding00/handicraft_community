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


    /**
     * 给帖子点赞
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("upPost")
    public Object upPost(Integer postId) {
        return postService.upPost(postId);
    }

    /**
     * 给帖子点踩
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("downPost")
    public Object downPost(Integer postId) {
        return postService.downPost(postId);
    }


    /**
     * 给帖子投币
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("coinPost")
    public Object coinPost(Integer postId) {
        return postService.coinPost(postId);
    }

    /**
     * 给帖子收藏
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("collectPost")
    public Object collectPost(Integer postId) {
        return postService.collectPost(postId);
    }

    /**
     * 给帖子分享帖子
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("sharePost")
    public Object sharePost(Integer postId) {
        return postService.sharePost(postId);
    }

    /**
     * 浏览当前帖子
     *
     * @param postId 帖子id
     * @return 返回值
     */
    @PostMapping("lookPost")
    public Object lookPost(Integer postId) {
        return postService.lookPost(postId);
    }

}
