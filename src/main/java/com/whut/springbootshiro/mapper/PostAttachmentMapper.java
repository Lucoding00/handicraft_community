package com.whut.springbootshiro.mapper;

import com.whut.springbootshiro.model.PostAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PostAttachmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PostAttachment record);

    int insertSelective(PostAttachment record);

    PostAttachment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PostAttachment record);

    int updateByPrimaryKey(PostAttachment record);

    int insertBatch(@Param("postId") Integer id,@Param("list") List<String> attachmentUrls);

    int deleteBatchByPostId(Integer id);
}