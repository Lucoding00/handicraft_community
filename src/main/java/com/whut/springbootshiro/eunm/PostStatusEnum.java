package com.whut.springbootshiro.eunm;

/**
 * @author Lei
 * @date 2024-04-29 0:08
 */
public enum PostStatusEnum {

    PUBLISH("publish"),

    AGREE("agree"),

    DISAGREE("disagree"),

    REMOVE("remove");

    private String value;

    PostStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
