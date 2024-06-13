package com.whut.springbootshiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.wf.captcha.SpecCaptcha;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Constant;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.eunm.UserRoleEnum;
import com.whut.springbootshiro.eunm.UserStatusEnum;
import com.whut.springbootshiro.form.UserAdminForm;
import com.whut.springbootshiro.form.UserInfoForm;
import com.whut.springbootshiro.jwt.JWTToken;
import com.whut.springbootshiro.jwt.JWTUtil;
import com.whut.springbootshiro.mapper.FollowerMapper;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.Follower;
import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.UserAdminQuery;
import com.whut.springbootshiro.service.UserService;
import com.whut.springbootshiro.shiro.ActiveUser;
import com.whut.springbootshiro.util.AuthenticationUserUtil;
import com.whut.springbootshiro.util.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * 用户接口实现
 *
 * @author Lei
 * @date 2024-04-20 23:01
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Resource(name = "verCode")
    private Map<String, String> verCodeMap;


    @Override
    public Result login(String username, String password, String code, String key) {
        String s = verCodeMap.get(key);
        if (Objects.isNull(s)) {
            return new Result(CodeMsg.CODE_INVALID);
        }
        if (!code.toLowerCase().equals(s.toLowerCase())) {
            return new Result(CodeMsg.CODE_ERROR);
        }
        /*对传输过来的密码进行加密！！！*/
        Md5Hash md5Hash = new Md5Hash(password, Constant.MD5_SALT, 2);

        User user = userMapper.selectUserByNameAndPwd(username, md5Hash.toString());
        if (user == null) {
            return new Result(CodeMsg.USER_USER_PASSWORD_ERROR);
        }
        if (!user.getPassword().equals(md5Hash.toString())) {
            return new Result(CodeMsg.USER_USER_PASSWORD_ERROR);
        }
        user.setPassword(null);
        // 加密签名
        String token = JWTUtil.sign(user.getId(), username, md5Hash.toString());
        JWTToken jwtToken = new JWTToken(token);
        String jwtTokenToken = jwtToken.getToken();
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        ActiveUser activeUser = new ActiveUser();
        BeanUtil.copyProperties(user, activeUser);
        Date expTime = JWTUtil.getExpTime(token);
        String dateFormat = DateUtil.getDateFormat(expTime, DateUtil.FULL_TIME_PATTERN);
        stringObjectHashMap.put("token", jwtTokenToken);
        stringObjectHashMap.put("user", activeUser);
        stringObjectHashMap.put("exp", dateFormat);
        return new Result(stringObjectHashMap);
    }


    public Object getCaptcha() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String verCode = specCaptcha.text().toLowerCase();
        String key = UUID.randomUUID().toString();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("key", key);
        stringStringHashMap.put("img", specCaptcha.toBase64());
        //存放到内存当中
        verCodeMap.put(key, verCode);
        System.out.println(key);
        System.out.println(verCode);
        return stringStringHashMap;
    }

    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        /*返回默认的登录的数据模块，然后在这个地方*/
        return new Result();
    }


    public Result updatePassword(String password, String newPassword) {
        //校验当前密码还有实际的密码
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        String loginPassword = user.getPassword();
        Md5Hash md5Hash = new Md5Hash(password, Constant.MD5_SALT, 2);
        if (!StrUtil.equals(loginPassword, md5Hash.toString())) {
            return new Result(CodeMsg.USER_USER_PASSWORD_ERROR);
        }
        Md5Hash md5Hash1 = new Md5Hash(newPassword, Constant.MD5_SALT, 2);
        int result = userMapper.updatePassword(user.getId(), md5Hash1.toString());
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result resetPassword() {
        //校验当前密码还有实际的密码
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        Md5Hash md5Hash1 = new Md5Hash(Constant.DEFAULT_PASSWORD, Constant.MD5_SALT, 2);
        int result = userMapper.updatePassword(user.getId(), md5Hash1.toString());
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result updateHeaderImg(String img) {
        //校验当前密码还有实际的密码
        Subject subject = SecurityUtils.getSubject();
        ActiveUser user = (ActiveUser) subject.getPrincipal();
        int result = userMapper.updateHeaderImg(user.getId(), img);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result normalUserRegister(UserAdminForm userAdminForm) {
        Result x = registerCheckUserInfo(userAdminForm);
        if (x != null) return x;
        User user = new User();
        BeanUtil.copyProperties(userAdminForm, user);
        Md5Hash md5Hash1 = new Md5Hash(userAdminForm.getPassword(), Constant.MD5_SALT, 2);
        user.setPassword(md5Hash1.toString());
        user.setRoleName(UserRoleEnum.USER.getValue());
        user.setStatus(UserStatusEnum.AVAILABLE.getValue());
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        // 默认100个币
        user.setCoinNum(100);
        int result = userMapper.insertSelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    private Result registerCheckUserInfo(UserAdminForm userAdminForm) {
        User checkUserName = userMapper.selectByCondition("username", userAdminForm.getUsername());
        if (!Objects.isNull(checkUserName)) {
            return new Result(CodeMsg.USERNAME_ERROR);
        }
        User checkUserEmail = userMapper.selectByCondition("email", userAdminForm.getEmail());
        if (!Objects.isNull(checkUserEmail)) {
            return new Result(CodeMsg.EMAIL_ERROR);
        }
        return null;
    }

    @Override
    public Result updateUserInfo(UserInfoForm userInfoForm) {
        User checkUserEmail = userMapper.selectByCondition("email", userInfoForm.getEmail());
        if (!Objects.isNull(checkUserEmail)) {
            return new Result(CodeMsg.EMAIL_ERROR);
        }
        User user = userMapper.selectByPrimaryKey(userInfoForm.getId());
        user.setUpdateTime(new Date());
        user.setEmail(userInfoForm.getEmail());
        user.setCountry(userInfoForm.getCountry());
        user.setPic(userInfoForm.getPic());
        user.setSex(userInfoForm.getSex());
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result freezeUser(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (!UserStatusEnum.AVAILABLE.getValue().equals(user.getStatus())) {
            return new Result(CodeMsg.STATUS_IS_NOT_OK);
        }
        user.setStatus(UserStatusEnum.UNUSABLE.getValue());
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result unfreezeUser(int userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (!UserStatusEnum.UNUSABLE.getValue().equals(user.getStatus())) {
            return new Result(CodeMsg.STATUS_IS_OK);
        }
        user.setStatus(UserStatusEnum.AVAILABLE.getValue());
        int result = userMapper.updateByPrimaryKeySelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result addAdminUser(UserAdminForm userAdminForm) {
        Result x = registerCheckUserInfo(userAdminForm);
        if (x != null) return x;
        User user = new User();
        user.setStatus(UserStatusEnum.AVAILABLE.getValue());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setCoinNum(100);
        user.setRoleName(UserRoleEnum.ADMIN.getValue());
        BeanUtil.copyProperties(userAdminForm, user);
        Md5Hash md5Hash1 = new Md5Hash(userAdminForm.getUsername(), Constant.MD5_SALT, 2);
        user.setPassword(md5Hash1.toString());
        int result = userMapper.insertSelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result addNormalUser(UserAdminForm userAdminForm) {
        Result x = registerCheckUserInfo(userAdminForm);
        if (x != null) return x;
        User user = new User();
        user.setStatus(UserStatusEnum.AVAILABLE.getValue());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setCoinNum(100);
        user.setRoleName(UserRoleEnum.USER.getValue());
        BeanUtil.copyProperties(userAdminForm, user);
        Md5Hash md5Hash1 = new Md5Hash(userAdminForm.getUsername(), Constant.MD5_SALT, 2);
        user.setPassword(md5Hash1.toString());
        int result = userMapper.insertSelective(user);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result deleteUser(int userId) {
        int result = userMapper.deleteByPrimaryKey(userId);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Override
    public Result page(UserAdminQuery userAdminQuery) {
        Page<User> userPage = PageHelper.startPage(userAdminQuery.getPage(), userAdminQuery.getLimit());
        userMapper.selectList(userAdminQuery);
        return new Result(userPage.toPageInfo());
    }

    @Override
    public Result recharge(int userId, int coinNum) {
        User user = userMapper.selectByPrimaryKey(userId);
        Integer preCoinNum = user.getCoinNum();
        int result = userMapper.updateCoinNum(userId, coinNum + preCoinNum);
        if (result > 0) {
            return new Result(CodeMsg.SUCCESS);
        } else {
            return new Result(CodeMsg.ERROR);
        }
    }

    @Resource
    private FollowerMapper followerMapper;

    @Override
    public Result concernList() {
        ActiveUser currentUser = AuthenticationUserUtil.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        List<Integer> fanIds = followerMapper.selectFansByPostId(currentUserId);
        if (CollectionUtil.isEmpty(fanIds)) {
            return new Result(new ArrayList<>());
        } else {
            List<User> userList = userMapper.selectUserListByIds(fanIds);
            return new Result(userList);
        }
    }

    @Override
    public Result cancelConcern(int postUserId, int fansId) {
        Follower follower = followerMapper.selectByPostIdAndFollower(fansId, postUserId);
        followerMapper.deleteByPrimaryKey(follower.getId());
        return new Result(CodeMsg.SUCCESS);
    }
}
