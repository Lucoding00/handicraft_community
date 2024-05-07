package com.whut.springbootshiro.service;

import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.form.UserAdminForm;
import com.whut.springbootshiro.form.UserInfoForm;
import com.whut.springbootshiro.query.UserAdminQuery;

/**
 * 用户的接口
 * 
 * @author Lei
 * @date 2024-04-20 23:00
 */
public interface UserService {
    Result login(String username, String password, String code, String key);
    Object getCaptcha();
    Result logout();
    Result updatePassword(String password, String newPassword);

    Result updateHeaderImg(String img);

    Result resetPassword();

    Result normalUserRegister(String username, String password);

    Result updateUserInfo(UserInfoForm userInfoForm);

    Result freezeUser(int userId);

    Result unfreezeUser(int userId);

    Result addAdminUser(UserAdminForm userAdminForm);

    Result addNormalUser(UserAdminForm userAdminForm);

    Result deleteUser(int userId);

    Result page(UserAdminQuery userAdminQuery);

    Result recharge(int userId,int coinNum);
}
