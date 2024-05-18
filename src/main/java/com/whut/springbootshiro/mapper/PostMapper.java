package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.Post;
import com.whut.springbootshiro.query.ReviewQuery;
import com.whut.springbootshiro.vo.PostVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Post record);

    int insertSelective(Post record);

    Post selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Post record);

    int updateByPrimaryKeyWithBLOBs(Post record);

    int updateByPrimaryKey(Post record);

    List<PostVo> selectList(ReviewQuery reviewQuery);

    List<PostVo> selectNewList(int newNum);

    List<PostVo> selectHotList(int hotNum);

    List<PostVo> selectListByIds(@Param("list") List<Integer> predictPostIds);
}