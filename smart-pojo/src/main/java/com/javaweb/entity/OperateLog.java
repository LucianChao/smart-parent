package com.javaweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperateLog {
    private Integer id; //ID
    private Integer operateEmpId;   //操作人id
    private LocalDateTime operateTime;  //操作时间
    private String className;   //类名
    private String methodName;  //方法名
    private String methodParams;//操作方法参数
    private String returnValue; //返回值
    private long costTime;      //执行耗时,单位ms
    private  String operateEmpName;//操作人名称
}
