package com.whut.springbootshiro.query;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-04-30 0:00
 */
@Data
public class PostCommentsQuery extends Query {
    private int postId;

    private int toUserId;
}
