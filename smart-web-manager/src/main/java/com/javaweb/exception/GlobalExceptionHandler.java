package com.javaweb.exception;

import com.javaweb.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice//作用：用来捕获控制器controller中抛出的的所有异常
//@ControllerAdvice
//@RequestBody
public class GlobalExceptionHandler {
    @ExceptionHandler   //指定处理何种异常，默认为Exception，即所有异常
    public Result doException(Exception e){
        log.error(e.getMessage());
        return Result.error("出错了，请联系管理员！！！");
    }
}
