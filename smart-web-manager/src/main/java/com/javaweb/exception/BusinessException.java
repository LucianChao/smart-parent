package com.javaweb.exception;

public class BusinessException extends RuntimeException {
    public BusinessException() {
        //  无参构造方法
    }

    public BusinessException(String message) {
        super(message);//  调用父类构造方法
    }
}
