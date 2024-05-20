package com.whut.springbootshiro.vo;

import com.whut.springbootshiro.model.User;
import lombok.Data;

/**
 * @author Lei
 * @date 2024-05-21 1:01
 */
@Data
public class MessageUserListVo {
    private User user;

    private String lastMessage;
}
