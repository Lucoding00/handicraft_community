package com.whut.springbootshiro.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.whut.springbootshiro.model.PostAttachment;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Lei
 * @date 2024-04-29 0:45
 */
@Data
public class PostVo {

    private int id;

    private String title;

    private String content;

    private String cover;

    private String videoUrl;

    private String tags;

    private String userId;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<PostAttachment> postAttachments;
}
