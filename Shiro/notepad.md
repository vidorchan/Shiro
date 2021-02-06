# BasicAuth
1.1 定义拦截器：

HandlerInterceptorAdapter

    preHandle
    
1.2注册拦截器：

WebMvcConfigurer

    addInterceptors：
        InterceptorRegistry addInterceptor(HandlerInterceptor).addPathPatterns("/**").order(int order);
    addViewControllers: 可配置默认页面
        ViewControllerRegistry addViewController(urlPath).setViewName("redirect:/页面.html");








# Shiro