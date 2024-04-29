package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-04-29 23:36
 */
@Data
public class CommentForm {
    private int id;

    private Integer userId;

    private Integer postId;

    private String comment;

    private Integer parentId;
}
