package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.InterestForm;
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
     * 获取发布的帖子列表
     *
     * @return 返回值
     */
    @PostMapping("reviewPage")
    public Object reviewPage(ReviewQuery reviewQuery) {
        return postService.reviewPage(reviewQuery);
    }












}
