package com.whut.springbootshiro.eunm;

/**
 * 评论状态
 *
 * @author Lei
 * @since 2024-05-07 22:27
 */
public enum CommentOperationEnum {
    UP("up"),

    DOWN("down");

    private String value;

    CommentOperationEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
