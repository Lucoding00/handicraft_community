package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PersonalMessage;

public interface PersonalMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalMessage record);

    int insertSelective(PersonalMessage record);

    PersonalMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalMessage record);

    int updateByPrimaryKey(PersonalMessage record);
}