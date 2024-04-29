package com.whut.springbootshiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author Lei
 * @create 2022-05-27 23:53
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "router") // 配置文件的前缀
public class ExcludePath {
    private List<String> exclude;
}
