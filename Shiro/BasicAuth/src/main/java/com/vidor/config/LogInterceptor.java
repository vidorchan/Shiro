package com.vidor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    private Logger logger = LoggerFactory.getLogger(LogInterceptor.class);
    private ThreadLocal<Long> timeHolder = new ThreadLocal<>();

    /**
     * preHandle在业务处理器处理请求之前被调用。预处理，可以进行编码、安全控制等处理；
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        timeHolder.set(System.currentTimeMillis());
        return super.preHandle(request, response, handler);
    }

    /**
     * postHandle在业务处理器处理请求执行完成后，生成视图之前执行。
     * 后处理（调用了Service并返回ModelAndView，但未进行页面渲染），有机会修改ModelAndView；
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Long spendTime = System.currentTimeMillis() - timeHolder.get();
        logger.info("request method "+request.getRequestURI() + " spended "+spendTime+" ms");
        super.postHandle(request, response, handler, modelAndView);
    }
}
