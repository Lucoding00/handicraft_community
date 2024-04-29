package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.InterestPostRel;

public interface InterestPostRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InterestPostRel record);

    int insertSelective(InterestPostRel record);

    InterestPostRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InterestPostRel record);

    int updateByPrimaryKey(InterestPostRel record);
}