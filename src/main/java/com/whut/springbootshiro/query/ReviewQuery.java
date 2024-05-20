package com.whut.springbootshiro.query;

import lombok.Data;

/**
 * @author Lei
 * @date 2024-04-29 0:49
 */
@Data
public class ReviewQuery extends Query {
    private String title;

    private String status;

    private String userId;

    private String category;
}
