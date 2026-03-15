package com.javaweb.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect
@Component
public class MyAspect1 {
    @Pointcut("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    public void pt(){}
    //环绕通知
//    @Around("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("环绕前通知......");
        Object result = joinPoint.proceed();
        log.info("环绕后通知......");
        return result;
    }

    //目标方法前触发该通知
//    @Before("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    @Before("pt()")
    public void before() {
        log.info("前置通知......");
    }

    //无论目标方法是否有异常，都会触发该通知
//    @After("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    @After("pt()")
    public void after() {
        log.info("后置通知......");
    }

//    @AfterReturning("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    @AfterReturning("pt()")
    public void afterReturning() {
        log.info("返回后通知......");
    }

//    @AfterThrowing("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    @AfterThrowing("pt()")
    public void afterThrowing() {
        log.info("异常后通知......");
    }
}
