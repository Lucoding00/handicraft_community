package com.whut.springbootshiro.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Lei
 * @date 2024-04-29 0:45
 */
@Data
public class PostVo {

    private int postId;

    private String title;

    private String content;

    private String cover;

    private String videoUrl;

    private String tags;

    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
