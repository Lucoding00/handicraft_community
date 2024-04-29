package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.Interest;
import com.whut.springbootshiro.query.InterestQuery;

import java.util.List;

public interface InterestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Interest record);

    int insertSelective(Interest record);

    Interest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Interest record);

    int updateByPrimaryKey(Interest record);

    List<Interest> selectList(InterestQuery interestQuery);
}