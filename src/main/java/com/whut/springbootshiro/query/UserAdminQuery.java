package com.whut.springbootshiro.query;

import lombok.Data;

/**
 * 用户信息查询query
 *
 * @author Lei
 * @since  2024-04-28 23:19
 */
@Data
public class UserAdminQuery extends Query {
    private String username;
}
