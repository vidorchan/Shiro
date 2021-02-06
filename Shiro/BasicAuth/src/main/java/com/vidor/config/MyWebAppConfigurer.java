package com.vidor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class MyWebAppConfigurer implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;
    //配置权限拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").order(20);
        registry.addInterceptor(logInterceptor).addPathPatterns("/**").order(10);
    }
    //简单配置启动页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry)
    {
        registry.addViewController("/").setViewName("redirect:/index.html");
    }
}