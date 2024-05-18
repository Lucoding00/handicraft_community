package com.whut.springbootshiro.config;

import net.jodah.expiringmap.ExpiringMap;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lei
 * @create 2021-08-12 11:27
 */
@Configuration
public class ImgHandlerConfig implements WebMvcConfigurer {

    @Value("${file-upload-path}")
    private String filePath;

    @Bean("verCode")
    public Map<String, String> getVerCodeMap() {
        return ExpiringMap.builder().expiration(30, TimeUnit.SECONDS).build();
    }

    @Bean("staticData")
    public Map<String, Object> getStaticDataMap() {
        return new HashMap<>();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //文件磁盘图片url 映射
        //配置server虚拟路径，handler为前台访问的目录，
        // locations为files相对应的本地路径 file代表磁盘，classpath代表的是项目的路径
        registry.addResourceHandler("/imgs/**").addResourceLocations("file:" + filePath);

    }
}
