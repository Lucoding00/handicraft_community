package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.UserPostRel;

public interface UserPostRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserPostRel record);

    int insertSelective(UserPostRel record);

    UserPostRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserPostRel record);

    int updateByPrimaryKey(UserPostRel record);

    UserPostRel selectByPostId(Integer postId);
}