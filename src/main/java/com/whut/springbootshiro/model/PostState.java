package com.whut.springbootshiro.model;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-05-16 22:46
 */
@Data
public class PostState extends Post {
    private boolean isLike;

    private boolean isDisLike;

    private boolean isCoin;

    private boolean isCollect;

    private boolean isShare;

    private  User createUser;

    private boolean isFollow;
}
