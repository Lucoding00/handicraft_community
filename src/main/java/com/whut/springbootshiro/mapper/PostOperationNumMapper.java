package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PostOperationNum;

public interface PostOperationNumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostOperationNum record);

    int insertSelective(PostOperationNum record);

    PostOperationNum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostOperationNum record);

    int updateByPrimaryKey(PostOperationNum record);
}