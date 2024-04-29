package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * 用户信息的表单
 *
 * @author Lei
 * @date 2024-04-21 10:56
 */
@Data
public class UserInfoForm {

    private int id;

    /**
     * 邮件
     */
    private String email;

    /**
     * 头像
     */
    private String pic;

    /**
     * 性别
     */
    private String sex;

    /**
     * 国家
     */
    private String country;

}
