package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PersonalMessage;

import java.util.List;

public interface PersonalMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PersonalMessage record);

    int insertSelective(PersonalMessage record);

    PersonalMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonalMessage record);

    int updateByPrimaryKey(PersonalMessage record);

    List<PersonalMessage> selectRecipientId(Integer userId, Integer recipientId);

    int batchReceive(List<Integer> messageIds);

    List<PersonalMessage> getMessageUserList(Integer id);
}