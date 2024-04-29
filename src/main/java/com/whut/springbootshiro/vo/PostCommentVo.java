package com.whut.springbootshiro.vo;

import com.whut.springbootshiro.model.PostComment;
import com.whut.springbootshiro.model.User;
import lombok.Data;

/**
 *
 * @author Lei
 * @since  2024-04-30 0:04
 */
@Data
public class PostCommentVo extends PostComment {
    private User commentUser;

    private User toUser;
}
