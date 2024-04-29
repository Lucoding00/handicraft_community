package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.query.ReviewQuery;

/**
 * @author Lei
 * @date 2024-04-29 0:04
 */
public interface PostService {
    Result add(PostForm postForm);

    Result reviewPage(ReviewQuery reviewQuery);

    Result auditPost(int postId, PostStatusEnum postStatusEnum);

    Result update(PostForm postForm);
}
