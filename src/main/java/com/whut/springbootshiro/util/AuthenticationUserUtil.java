package com.whut.springbootshiro.util;

import com.whut.springbootshiro.shiro.ActiveUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 认证用户工具类
 *
 * @author Lei
 * @date 2024-05-07 22:19
 */
public class AuthenticationUserUtil {


    /**
     * 获取当前登录的用户
     *
     * @return 当前登录的用户信息
     */
    public static ActiveUser getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (ActiveUser) subject.getPrincipal();
    }


}
