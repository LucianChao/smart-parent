package com.javaweb.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
//@Aspect//声明当前类为切面类
@Component
public class DemoAspect {

    /**
     * 记录方法执行时间
     *
     * @return
     */
    @Around("execution(* com.javaweb.service.impl.DeptServiceImpl.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //1.获取方法运行时的 开始 时间
        long begin = System.currentTimeMillis();

        //2.调用方法，获取返回值
        Object result = joinPoint.proceed();

        //3.获取方法运行时的 结束 时间
        long end = System.currentTimeMillis();
        log.info("执行耗时: {}", end - begin);

        //4.返回方法运行结果
        return result;
    }
}
