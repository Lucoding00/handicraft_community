package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PostComment;

public interface PostCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostComment record);

    int insertSelective(PostComment record);

    PostComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostComment record);

    int updateByPrimaryKey(PostComment record);
}