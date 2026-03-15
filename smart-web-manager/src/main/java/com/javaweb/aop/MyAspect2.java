package com.javaweb.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 连接点对象
 */
@Slf4j
//@Aspect
@Component
public class MyAspect2 {
    @Pointcut("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    public void pt() {
    }

    @Around("pt()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //1.获取目标类名
        String name = joinPoint.getTarget().getClass().getName();
        System.out.println("name = " + name);
        //2.目标方法签名
        Signature signature = joinPoint.getSignature();
        System.out.println("signature = " + signature);
        //3.获取目标方法名
        String methodName = signature.getName();
        System.out.println("methodName = " + methodName);
        //4.获取方法参数
        Object[] args = joinPoint.getArgs();
        System.out.println("args = " + Arrays.toString(args));

        //5.获取方法返回值
        Object result = joinPoint.proceed();
        System.out.println("result = " + result);
        System.out.println("--------------------------------");
        return result;
    }

    //目标方法前触发该通知
    @Before("pt()")
    public void before(JoinPoint joinPoint) {
        //1.获取目标类名
        String name = joinPoint.getTarget().getClass().getName();
        System.out.println("name = " + name);
        //2.目标方法签名
        Signature signature = joinPoint.getSignature();
        System.out.println("signature = " + signature);
        //3.获取目标方法名
        String methodName = signature.getName();
        System.out.println("methodName = " + methodName);
        //4.获取方法参数
        Object[] args = joinPoint.getArgs();
        System.out.println("args = " + Arrays.toString(args));
        log.info("前置通知......");
    }

}
