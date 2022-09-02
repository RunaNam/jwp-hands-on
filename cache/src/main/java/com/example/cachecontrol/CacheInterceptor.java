package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class CacheInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        Exception {
        response.addHeader("Cache-Control", "no-cache, private");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
