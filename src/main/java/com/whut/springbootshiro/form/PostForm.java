package com.whut.springbootshiro.form;

import lombok.Data;

import java.util.List;

/**
 * @author Lei
 * @date 2024-04-28 23:50
 */
@Data
public class PostForm {

    private String title;

    private String content;

    private String cover;

    private String videoUrl;

    private String tags;

    private List<String> attachmentUrls;
}
