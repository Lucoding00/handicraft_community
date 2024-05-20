package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.PostForm;
import com.whut.springbootshiro.model.PersonalMessage;
import com.whut.springbootshiro.service.PersonMessageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lei
 * @date 2024-05-20 23:44
 */
@RestController
@RequestMapping("/message")
public class PersonMessageController {

    @Resource
    private PersonMessageService personMessageService;

    /**
     * 私信发送
     *
     * @param personalMessage 发送的响应体
     * @return 响应体
     */
    @PostMapping("/send")
    public Object send(PersonalMessage personalMessage) {
        return personMessageService.send(personalMessage);
    }

    /**
     * 获取和某个用户的私信记录
     *
     * @param recipientId 另外一个用户的id
     * @return 响应体
     */
    @PostMapping("/getUserMessageList")
    public Object getUserMessageList(Integer recipientId) {
        return personMessageService.getUserMessageList(recipientId);
    }

    /**
     * 将聊天记录更改为已读
     *
     * @param messageIds 私信消息的id列表
     * @return 响应体
     */
    @PostMapping("/batchReceive")
    public Object batchReceive(List<Integer> messageIds) {
        return personMessageService.batchReceive(messageIds);
    }


    /**
     * 获取到当前用户的聊天用户以及最后发送消息
     *
     * @return 响应体
     */
    @PostMapping("/getMessageUserList")
    public Object getMessageUserList() {
        return personMessageService.getMessageUserList();
    }


}
