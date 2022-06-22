package com.cn.linka.common.config;

import com.cn.linka.common.utils.RandomStringUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志拦截器
 */
@Aspect
@Component
@Slf4j
public class WebLogAspect {
    public final static String REQUEST_ID = "requestId";
    /** 以 controller 包下定义的所有请求为切入点 */
    @Pointcut("execution( * com.cn.linka.business.controller.*.*(..))")
    public void webLog() {}

    /**
     * 在切点之前织入
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 开始打印请求日志
        String uuid = RandomStringUtil.createRandomNumber(10);
        MDC.put(REQUEST_ID, uuid);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        log.info("========================================== Start ==========================================");
        String token = request.getHeader("Authorization");
        log.info("URL:{},Authorization: {},Class Method : {}.{},IP : {},Request Args: {}", request.getRequestURL().toString(),token,joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),request.getRemoteAddr(), new Gson().toJson(joinPoint.getArgs()));
    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        log.info("Response Args  : {}", new Gson().toJson(result));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        MDC.clear();
        return result;
    }

}
