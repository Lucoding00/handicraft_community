package com.whut.springbootshiro.controller;

import com.whut.springbootshiro.form.UserAdminForm;
import com.whut.springbootshiro.form.UserInfoForm;
import com.whut.springbootshiro.query.UserAdminQuery;
import com.whut.springbootshiro.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 会员管理
 *
 * @author Lei
 * @since 2024-04-28 22:50
 */
@RestController
@RequestMapping("/user/admin")
public class UserAdminController {

    @Resource
    private UserService userService;


    /**
     * 添加管理员信息
     *
     * @param userAdminForm 用户信息
     * @return 返回值
     */
    @PostMapping("addAdminUser")
    public Object addAdminUser(UserAdminForm userAdminForm) {
        return userService.addAdminUser(userAdminForm);
    }

    /**
     * 添加用户信息
     *
     * @param userAdminForm 用户信息
     * @return 返回值
     */
    @PostMapping("addNormalUser")
    public Object addNormalUser(UserAdminForm userAdminForm) {
        return userService.addNormalUser(userAdminForm);
    }

    /**
     * 添加用户信息
     *
     * @param userId 用户id
     * @return 返回值
     */
    @PostMapping("deleteUser")
    public Object deleteUser(int userId) {
        return userService.deleteUser(userId);
    }


    /**
     * 更新用户信息
     *
     * @param userInfoForm 用户信息form
     * @return 返回值
     */
    @PostMapping("updateUserInfo")
    public Object updateUserInfo(UserInfoForm userInfoForm) {
        return userService.updateUserInfo(userInfoForm);
    }

    /**
     * 用户信息分页
     *
     * @param userAdminQuery 用户信息分页查询
     * @return 返回当前用户信息列表
     */
    @PostMapping("page")
    public Object page(UserAdminQuery userAdminQuery) {
        return userService.page(userAdminQuery);
    }
}
