package com.tlc.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

// 拦截带有 @Controller 注解
@ControllerAdvice
public class ControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 自定义 拦截 Exception 及以下异常
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler (HttpServletRequest request, Exception e) throws Exception {
        logger.error("Requst URL : {}, Exception : {}", request.getRequestURL(), e);

        // 寻找该异常是否存在关于 ResponseStatus 注解
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}
