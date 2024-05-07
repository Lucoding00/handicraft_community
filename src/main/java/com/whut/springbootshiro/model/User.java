package com.whut.springbootshiro.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色名称
     */
    private String roleName;

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
     * 状态
     */
    private String status;

    /**
     * 国家
     */
    private String country;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 硬币数量
     */
    private Integer coinNum;
}