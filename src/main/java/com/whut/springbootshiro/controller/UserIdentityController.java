package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用户模块
 */
@RestController
@RequestMapping("/user/identity")
public class UserIdentityController {

    @Resource
    private UserService userService;

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码的文字
     * @param key      验证码的关联key
     * @return 登录返回值
     */
    @PostMapping("login")
    public Object login(String username,
                        String password,
                        String code,
                        String key) {
        return userService.login(username, password, code, key);
    }

    /**
     * 普通用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @return 注册状态返回消息
     */
    @PostMapping("normalUserRegister")
    public Object normalUserRegister(String username, String password) {
        return userService.normalUserRegister(username, password);
    }


    /**
     * 获取验证码
     *
     * @return 验证码响应图像
     */
    @ResponseBody
    @GetMapping("captcha")
    public Object captcha() {
        return userService.getCaptcha();
    }

    /**
     * 退出
     *
     * @return 退出消息
     */
    @GetMapping("logout")
    public Object logout() {
        return userService.logout();
    }


    /**
     * 修改密码
     *
     * @param password    原密码
     * @param newPassword 新密码
     * @return 返回状态信息
     */
    @PostMapping("updatePassword")
    public Object updatePassword(String password, String newPassword) {
        return userService.updatePassword(password, newPassword);
    }

    /**
     * 重置密码
     *
     * @return 返回状态消息
     */
    @PostMapping("resetPassword")
    public Object resetPassword() {
        return userService.resetPassword();
    }

    /**
     * 重新修改头像
     *
     * @param img 头像url
     * @return 头像返回值
     */
    @RequestMapping("updateHeaderImg")
    public Object updateHeaderImg(String img) {
        return userService.updateHeaderImg(img);
    }


    /**
     * 冻结当前用户
     *
     * @param userId 用户id
     * @return 返回值
     */
    @PostMapping("freezeUser")
    public Object freezeUser(int userId) {
        return userService.freezeUser(userId);
    }


    /**
     * 解冻当前的用户
     *
     * @param userId 用户id
     * @return 返回值
     */
    @PostMapping("unfreezeUser")
    public Object unfreezeUser(int userId) {
        return userService.unfreezeUser(userId);
    }

}
