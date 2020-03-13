package com.tlc.blog.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 拦截未登录用户强转到 admin 下的页面
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("/admin");
            return false;
        }
        return true;
    }
}
