package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.CommentForm;
import com.whut.springbootshiro.mapper.PostCommentMapper;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.PostComment;
import com.whut.springbootshiro.query.PostCommentsQuery;
import com.whut.springbootshiro.service.CommentService;
import com.whut.springbootshiro.vo.PostCommentVo;
import com.whut.springbootshiro.vo.PostVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author Lei
 * @date 2024-04-29 23:34
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private PostCommentMapper postCommentMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result addComment(CommentForm commentForm) {
        PostComment postComment = new PostComment();
        BeanUtil.copyProperties(commentForm, postComment);
        postComment.setCreateTime(new Date());
        Date date = new Date();
        postComment.setUpdateTime(date);
        postComment.setCreateTime(date);
        int res = postCommentMapper.insertSelective(postComment);
        if (res > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result deleteComment(int commentId) {
        int res = postCommentMapper.deleteByPrimaryKey(commentId);
        if (res > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result postComments(PostCommentsQuery postCommentsQuery) {
        Page<PostCommentVo> postCommentVos = PageHelper.startPage(postCommentsQuery.getPage(), postCommentsQuery.getLimit());
        postCommentMapper.selectList(postCommentsQuery);
        PageInfo<PostCommentVo> postCommentVoPageInfo = postCommentVos.toPageInfo();
        List<PostCommentVo> list = postCommentVoPageInfo.getList();
        for (PostCommentVo postCommentVo : list) {
            postCommentVo.setCommentUser(userMapper.selectByPrimaryKey(postCommentVo.getUserId()));
            postCommentVo.setToUser(userMapper.selectByPrimaryKey(postCommentVo.getToUserId()));
        }
        return new Result(postCommentVos.toPageInfo());
    }
}
