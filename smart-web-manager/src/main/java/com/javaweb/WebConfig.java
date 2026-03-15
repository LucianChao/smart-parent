package com.javaweb;

import com.javaweb.interceptor.DemoInterceptor;
import com.javaweb.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
//@Component//将类注入到Spring容器中
@Slf4j
@Configuration//声明当前类是一个配置类 等价于 @Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private DemoInterceptor demoInterceptor;

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册拦截器");
//        registry.addInterceptor(demoInterceptor).addPathPatterns("/**");

        // 登录检查拦截器应用于所有路径
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**");
        log.info("登录检查拦截器已注册，应用于所有路径");
    }
}