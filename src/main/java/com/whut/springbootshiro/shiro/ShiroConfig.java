package com.whut.springbootshiro.shiro;

import com.whut.springbootshiro.jwt.JWTFilter;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.servlet.Filter;
import java.util.LinkedHashMap;


@Configuration
public class ShiroConfig {
    //创建shiroFilter 拦截所有请求，这个就是shiro的拦截器
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 securityManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        // 配置自定义过滤器链JWTFilter
        LinkedHashMap<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt", new JWTFilter());
        shiroFilterFactoryBean.setFilters(filters);
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 所有请求都要经过 jwt过滤器
        filterChainDefinitionMap.put("/**", "jwt");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    //创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //添加realm，因为配置了bean，会找到下面的realm。spring加载安全管理器时发现需要realm会自动去找bean realm，找到下面的realm
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    //创建自定义realm
    @Bean("realm")
    public Realm getRealm(){
        return new MyAuthentication();
    }
}
