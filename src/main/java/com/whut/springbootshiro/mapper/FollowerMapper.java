package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.Follower;

public interface FollowerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Follower record);

    int insertSelective(Follower record);

    Follower selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Follower record);

    int updateByPrimaryKey(Follower record);
}