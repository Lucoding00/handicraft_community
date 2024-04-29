package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.UserAdminQuery;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByNameAndPwd(String username, String toString);

    int updatePassword(Integer id, String password);

    int updateHeaderImg(Integer id, String img);

    List<User> selectList(UserAdminQuery userAdminQuery);
}