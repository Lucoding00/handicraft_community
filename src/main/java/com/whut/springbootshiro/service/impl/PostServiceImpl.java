package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.PostStatusEnum;
import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.mapper.PostAttachmentMapper;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.UserPostRelMapper;
import com.whut.springbootshiro.model.Post;
import com.whut.springbootshiro.model.PostAttachment;
import com.whut.springbootshiro.model.UserPostRel;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.service.PostService;
import com.whut.springbootshiro.shiro.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 发帖管理
 *
 * @author Lei
 * @since  2024-04-29 0:04
 */
@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;

    @Resource
    private PostAttachmentMapper postAttachmentMapper;

    @Resource
    private UserPostRelMapper userPostRelMapper;

    @Override
    public Result add(PostForm postForm) {
        Post post = new Post();
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        post.setStatus(PostStatusEnum.PUBLISH.getValue());
        BeanUtil.copyProperties(postForm, post);
        int nextInt = postMapper.insertSelective(post);
        if (nextInt <= 0) {
            return new Result(CodeMsg.INSERT_POST_ERROR);
        }
        Integer id = post.getId();
        List<String> attachmentUrls = postForm.getAttachmentUrls();
        int res = postAttachmentMapper.insertBatch(id, attachmentUrls);
        if (res <= 0) {
            return new Result(CodeMsg.ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        UserPostRel userPostRel = new UserPostRel();
        userPostRel.setUserId(user.getId());
        userPostRel.setPostId(id);
        int result = userPostRelMapper.insertSelective(userPostRel);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result reviewPage(ReviewQuery reviewQuery) {
        return null;
    }
}
