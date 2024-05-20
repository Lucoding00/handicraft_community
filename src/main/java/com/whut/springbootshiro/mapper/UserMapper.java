package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.User;
import com.whut.springbootshiro.query.UserAdminQuery;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByNameAndPwd(@Param("username") String username,@Param("password") String password);

    int updatePassword(Integer id, String password);

    int updateHeaderImg(Integer id, String img);

    List<User> selectList(UserAdminQuery userAdminQuery);

    int updateCoinNum(Integer userId, int coinNum);

    User selectByCondition(String condition, String value);

    List<User> selectListByRole(String roleName);

    List<User> selectUserListByIds(List<Integer> userSet);
}