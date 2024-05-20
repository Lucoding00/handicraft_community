package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.model.PersonalMessage;

import java.util.List;

/**
 * @author Lei
 * @date 2024-05-20 23:45
 */
public interface PersonMessageService {
    Result send(PersonalMessage personalMessage);

    Result getUserMessageList(Integer recipientId);

    Result batchReceive(List<Integer> messageIds);

    Result getMessageUserList();
}
