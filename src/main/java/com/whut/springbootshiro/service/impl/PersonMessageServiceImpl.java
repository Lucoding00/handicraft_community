package com.whut.springbootshiro.service.impl;

import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.mapper.PersonalMessageMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.PersonalMessage;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.service.PersonMessageService;
import com.whut.springbootshiro.shiro.ActiveUser;
import com.whut.springbootshiro.util.AuthenticationUserUtil;
import com.whut.springbootshiro.vo.MessageUserListVo;
import org.hibernate.validator.constraints.EAN;
import org.springframework.stereotype.Service;
import sun.plugin2.message.Message;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Lei
 * @date 2024-05-20 23:45
 */
@Service
public class PersonMessageServiceImpl implements PersonMessageService {

    @Resource
    private PersonalMessageMapper personalMessageMapper;

    @Resource
    private UserMapper userMapper;


    @Override
    public Result send(PersonalMessage personalMessage) {
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        personalMessage.setSendUserId(currentUser.getId());
        personalMessage.setSendTime(new Date());
        personalMessage.setIsRead(false);
        personalMessageMapper.insertSelective(personalMessage);
        return new Result(CodeMsg.SUCCESS);
    }


    @Override
    public Result getUserMessageList(Integer recipientId) {
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        List<PersonalMessage> list = personalMessageMapper.selectRecipientId(currentUser.getId(), recipientId);
        User curUser = userMapper.selectByPrimaryKey(currentUser.getId());
        User receiveUser = userMapper.selectByPrimaryKey(recipientId);
        Map<Integer, User> userMap = new HashMap<>();
        userMap.put(currentUser.getId(), curUser);
        userMap.put(recipientId, receiveUser);
        for (PersonalMessage personalMessage : list) {
            personalMessage.setSendUser(userMap.get(personalMessage.getSendUserId()));
            personalMessage.setReceiveUser(userMap.get(personalMessage.getRecipientId()));
        }
        return new Result(list);
    }

    @Override
    public Result batchReceive(List<Integer> messageIds) {
        personalMessageMapper.batchReceive(messageIds);
        return new Result(CodeMsg.SUCCESS);
    }

    @Override
    public Result getMessageUserList() {
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        List<PersonalMessage> list = personalMessageMapper.getMessageUserList(currentUser.getId());
        List<MessageUserListVo> messageUserListVos = new ArrayList<>();
        Integer currentUserId = currentUser.getId();
        List<Integer> userSet = new ArrayList<>();
        Map<Integer, String> lastMessage = new HashMap<>();
        for (PersonalMessage personalMessage : list) {
            if (personalMessage.getSendUserId().equals(currentUserId)) {
                userSet.add(personalMessage.getRecipientId());
                lastMessage.put(personalMessage.getRecipientId(), personalMessage.getSendMessage());
            } else {
                userSet.add(personalMessage.getSendUserId());
                lastMessage.put(personalMessage.getSendUserId(), personalMessage.getSendMessage());
            }
        }
        List<User> userList = userMapper.selectUserListByIds(userSet);
        HashMap<Integer, User> integerUserHashMap = new HashMap<>();
        for (User user : userList) {
            integerUserHashMap.put(user.getId(), user);
        }
        for (Integer userId : userSet) {
            MessageUserListVo messageUserListVo = new MessageUserListVo();
            messageUserListVo.setUser(integerUserHashMap.get(userId));
            messageUserListVo.setLastMessage(lastMessage.get(userId));
            messageUserListVos.add(messageUserListVo);
        }
        return new Result(messageUserListVos);
    }
}
