package com.whut.springbootshiro;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@MapperScan(value = "com.whut.springbootshiro.mapper")
@SpringBootApplication
public class SpringBootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootShiroApplication.class, args);
    }

}
