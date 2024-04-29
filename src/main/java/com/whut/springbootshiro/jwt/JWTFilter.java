package com.whut.springbootshiro.jwt;


import cn.hutool.json.JSONUtil;
import com.whut.springbootshiro.common.CodeMsg;
import com.whut.springbootshiro.common.Result;
import com.whut.springbootshiro.config.ExcludePath;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 利用JWT实现自定义shiro的过滤器 
 * @author melo、lh
 * @createTime 2019-10-21 13:49:05
 */
@Slf4j
@Configuration
public class JWTFilter extends BasicHttpAuthenticationFilter implements ApplicationContextAware {

    /**请求头授权标示*/
    private final static String AUTHORIZATION = "Authorization";

    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(JWTFilter.applicationContext == null){
            JWTFilter.applicationContext  = applicationContext;
        }
    }
    /**
     *  判断用户是否想要登录 （请求头是否携带token）
     * @param request
     * @param response
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 13:55:57
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(AUTHORIZATION);
        if (authorization==null || "".equals(authorization)){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 是否允许访问 false 请求被拦截  (如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入)
     * @param request
     * @param response
     * @param mappedValue
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 14:05:20
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        // 得到请求的urI
        String requestURI = httpServletRequest.getRequestURI();

        //如果访问的是当前的static目录下面，那么我让他直接进行访问，而不是访问的时候出现400的权限错误
//        if (requestURI.endsWith("html")
//                ||requestURI.endsWith("css")
//                ||requestURI.endsWith("js")
//                ||requestURI.endsWith("png")
//                ||requestURI.endsWith("svg")
//                ||requestURI.endsWith("jpeg")
//                ||requestURI.endsWith("jpg")
//                ||requestURI.endsWith("woff")
//                ||requestURI.endsWith("ttf")
//                ||requestURI.endsWith("ico")){
//            return true;
//        }

        // 配置免认证的url
        ExcludePath bean = JWTFilter.applicationContext.getBean(ExcludePath.class);
        List<String> exclude = bean.getExclude();
        String[] strings = exclude.toArray(new String[0]);
        // 如果请求的是无需认证的URL 直接放行  类似 /login,/logout
        for (String url:strings) {
            if(pathMatcher.match(url,requestURI)){
                return true;
            }
        }
        // 如果不是无需认证的 请求 则判断是否想要访问
        if(isLoginAttempt(request,response)){
            // 进行登陆认证
            try {
                return executeLogin(request,response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 进行登陆认证处理
     * @param request
     * @param response
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 14:24:43
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //得到授权的token
        String token = httpServletRequest.getHeader(AUTHORIZATION);

        JWTToken jwtToken = new JWTToken(token);
        // 提交给realm进行 认证操作，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(jwtToken);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 重写该方法避免循环调用doGetAuthenticationInfo方法
     * @param request
     * @param response
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 14:26:32
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 对于跨域提供支持
     * @param request
     * @param response
     * @return boolean
     * @author melo、lh
     * @createTime 2019-10-21 14:28:44
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     *  失败响应(return false 进入)
     * @param request
     * @param response
     * @return boolean
     * @createTime 2019-10-21 14:33:45
     */
    @Override
    protected boolean sendChallenge(ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(200);
        httpResponse.setCharacterEncoding("utf-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpResponse.getWriter()) {
            Result result = new Result(CodeMsg.AUTH_ERROR);
            String s = JSONUtil.toJsonStr(result);
            out.print(s);
        } catch (IOException e) {

        }
        return false;
    }
}
