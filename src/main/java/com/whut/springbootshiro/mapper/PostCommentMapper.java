package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PostComment;
import com.whut.springbootshiro.query.PostCommentsQuery;
import com.whut.springbootshiro.vo.PostCommentVo;

import java.util.List;

public interface PostCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostComment record);

    int insertSelective(PostComment record);

    PostComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostComment record);

    int updateByPrimaryKey(PostComment record);

    List<PostCommentVo> selectList(PostCommentsQuery postCommentsQuery);
}