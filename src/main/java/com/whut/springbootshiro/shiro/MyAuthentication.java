package com.whut.springbootshiro.shiro;



import cn.hutool.core.bean.BeanUtil;
import com.whut.springbootshiro.jwt.JWTToken;
import com.whut.springbootshiro.jwt.JWTUtil;
import com.whut.springbootshiro.mapper.UserMapper;
import com.whut.springbootshiro.model.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * 认证鉴权器
 */
public class MyAuthentication extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 判断token是否事我们的这个jwttoekn
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }
    /**
     * 用户认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的
        String tokenStr = (String) token.getCredentials();
        Integer userId = JWTUtil.getUserId(tokenStr);
        String username = JWTUtil.getUsername(tokenStr);
        User userInstance = userMapper.selectByPrimaryKey(userId);
        // 根据用户名和密码没有查询到数据  则直接返回null
        if (userInstance == null) {
            throw new AuthenticationException("用户不存在！");
        }
        if(!JWTUtil.verify(tokenStr,username,userInstance.getPassword())){
            throw new AuthenticationException("token校验不通过！");
        }
        ActiveUser activeUser = new ActiveUser();
        BeanUtil.copyProperties(userInstance,activeUser);
        return new SimpleAuthenticationInfo(activeUser, tokenStr, userInstance.getUsername());
    }

    /**
     * 用户鉴权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId = JWTUtil.getUserId(principals.toString());
        User userInstance = userMapper.selectByPrimaryKey(userId);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(Collections.singletonList(userInstance.getRoleName()));
        return simpleAuthorizationInfo;
    }
}
