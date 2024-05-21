package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PostOperationNum;

import java.util.List;

public interface PostOperationNumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostOperationNum record);

    int insertSelective(PostOperationNum record);

    PostOperationNum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostOperationNum record);

    int updateByPrimaryKey(PostOperationNum record);

    List<PostOperationNum> selectByUserIdAndPostId(Integer userId, Integer postId);

    PostOperationNum selectByUserIdAndPostIdAndStatus(Integer userId, Integer postId, String status);

    PostOperationNum selectLastLook(Integer userId, Integer postId);

    List<PostOperationNum> selectList();
}