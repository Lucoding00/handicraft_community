package com.whut.springbootshiro.form;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-04-29 23:36
 */
@Data
public class CommentForm {
    private Integer postId;

    private String comment;

    private Integer toUserId;
}
