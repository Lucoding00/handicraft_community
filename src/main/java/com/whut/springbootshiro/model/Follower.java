package com.whut.springbootshiro.model;

public class Follower {
    private Integer id;

    private Integer poster;

    private Integer fans;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoster() {
        return poster;
    }

    public void setPoster(Integer poster) {
        this.poster = poster;
    }

    public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }
}