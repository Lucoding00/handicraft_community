package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-04-28 23:11
 */
@Data
public class UserAdminForm extends UserInfoForm {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
