package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.CommentOperationNum;

public interface CommentOperationNumMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CommentOperationNum record);

    int insertSelective(CommentOperationNum record);

    CommentOperationNum selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CommentOperationNum record);

    int updateByPrimaryKey(CommentOperationNum record);
}