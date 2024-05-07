package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.CommentOperationEnum;
import com.whut.springbootshiro.form.CommentForm;
import com.whut.springbootshiro.mapper.CommentOperationNumMapper;
import com.whut.springbootshiro.mapper.PostCommentMapper;
import com.whut.springbootshiro.mapper.PostMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.CommentOperationNum;
import com.whut.springbootshiro.model.PostComment;
import com.whut.springbootshiro.query.PostCommentsQuery;
import com.whut.springbootshiro.service.CommentService;
import com.whut.springbootshiro.shiro.ActiveUser;
import com.whut.springbootshiro.util.AuthenticationUserUtil;
import com.whut.springbootshiro.vo.PostCommentVo;
import com.whut.springbootshiro.vo.PostVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Lei
 * @date 2024-04-29 23:34
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private PostCommentMapper postCommentMapper;


    @Resource
    private CommentOperationNumMapper commentOperationNumMapper;


    @Override
    public Result addComment(CommentForm commentForm) {
        PostComment postComment = new PostComment();
        BeanUtil.copyProperties(commentForm, postComment);
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
        return new Result(postCommentVos.toPageInfo());
    }

    /**
     * 点赞的状态：
     * 无点赞
     * 1：如果没有点赞，那么就进行点赞
     * 1：需要添加记录到CommentOperationNum表里面的数据，并且增加数值到PostComment
     * 点赞
     * 1: 如果有点赞，那么就取消点赞
     * 1：删除CommentOperationNum表的数据，并减少数值到PostComment
     * 点踩
     * 1：如果当前的状态是点踩，那么就将CommentOperationNum表的数据进行转化，然后更换PostComment表里面的数据
     *
     * @param commentId 评论id
     * @return 返回信息值
     */
    @Override
    public Result upComment(Integer commentId) {
        PostComment postComment = postCommentMapper.selectByPrimaryKey(commentId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();

        CommentOperationNum commentOperationNum =
                commentOperationNumMapper.selectByUserIdAndCommentIdAndStatus(userId, commentId);
        // 无点赞
        if (Objects.isNull(commentOperationNum)) {
            CommentOperationNum insertNum = new CommentOperationNum();
            insertNum.setOperationType(CommentOperationEnum.UP.getValue());
            insertNum.setUserId(userId);
            insertNum.setCommentId(commentId);
            commentOperationNumMapper.insertSelective(insertNum);
            Integer upNum = postComment.getUpNum();
            postComment.setUpNum((Objects.isNull(upNum) ? 0 : upNum) + 1);
            postCommentMapper.updateByPrimaryKeySelective(postComment);
            return new Result(CodeMsg.SUCCESS);
        }else if (CommentOperationEnum.UP.getValue().equals(commentOperationNum.getOperationType())){ // 点赞了
            Integer upNum = postComment.getUpNum();
            postComment.setUpNum((Objects.isNull(upNum) ? 0 : upNum) - 1);
            postCommentMapper.updateByPrimaryKeySelective(postComment);
            commentOperationNumMapper.deleteByPrimaryKey(commentOperationNum.getId());
            return new Result(CodeMsg.SUCCESS);
        }else{
            commentOperationNum.setOperationType(CommentOperationEnum.DOWN.getValue());
            commentOperationNumMapper.updateByPrimaryKeySelective(commentOperationNum);
            Integer upNum = postComment.getUpNum();
            Integer downNum = postComment.getDownNum();
            postComment.setUpNum((Objects.isNull(upNum) ? 0 : upNum) + 1);
            postComment.setDownNum((Objects.isNull(downNum) ? 0 : downNum) - 1);
            return new Result(CodeMsg.SUCCESS);
        }
    }

    @Override
    public Result downComment(Integer commentId) {
        PostComment postComment = postCommentMapper.selectByPrimaryKey(commentId);
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer userId = currentUser.getId();

        CommentOperationNum commentOperationNum =
                commentOperationNumMapper.selectByUserIdAndCommentIdAndStatus(userId, commentId);
        // 无点踩
        if (Objects.isNull(commentOperationNum)) {
            CommentOperationNum insertNum = new CommentOperationNum();
            insertNum.setOperationType(CommentOperationEnum.DOWN.getValue());
            insertNum.setUserId(userId);
            insertNum.setCommentId(commentId);
            commentOperationNumMapper.insertSelective(insertNum);
            Integer upNum = postComment.getUpNum();
            postComment.setDownNum((Objects.isNull(upNum) ? 0 : upNum) + 1);
            postCommentMapper.updateByPrimaryKeySelective(postComment);
            return new Result(CodeMsg.SUCCESS);
        }else if (CommentOperationEnum.DOWN.getValue().equals(commentOperationNum.getOperationType())){ // 点踩
            Integer upNum = postComment.getUpNum();
            postComment.setDownNum((Objects.isNull(upNum) ? 0 : upNum) - 1);
            postCommentMapper.updateByPrimaryKeySelective(postComment);
            commentOperationNumMapper.deleteByPrimaryKey(commentOperationNum.getId());
            return new Result(CodeMsg.SUCCESS);
        }else{
            commentOperationNum.setOperationType(CommentOperationEnum.UP.getValue());
            commentOperationNumMapper.updateByPrimaryKeySelective(commentOperationNum);
            Integer upNum = postComment.getUpNum();
            Integer downNum = postComment.getDownNum();
            postComment.setUpNum((Objects.isNull(upNum) ? 0 : upNum) - 1);
            postComment.setDownNum((Objects.isNull(downNum) ? 0 : downNum) + 1);
            return new Result(CodeMsg.SUCCESS);
        }
    }
}
