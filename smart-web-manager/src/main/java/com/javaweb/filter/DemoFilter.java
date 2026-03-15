package com.javaweb.filter;

import jakarta.servlet.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
//@WebFilter(urlPatterns = "/*")
//@WebFilter(value = "/*")
//@WebFilter("/*")    //设置拦截路径/*，代表拦截所有请求
public class DemoFilter implements Filter {
    //初始化方法，在web服务器启动时触发一次
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("DemoFilter...init...");
    }

    //销毁方法，在web服务器正常关闭时触发一次
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("DemoFilter...doFilter...");
        //放行
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("DemoFilter...destroy...");
    }
}
