package com.javaweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Emp {
    private Integer id;//ID,主键
    private String username;//用户名
    private String password;//密码
    private String name;//姓名
    private Integer gender;//性别:1-男,2-女
    private String phone;//手机
    private Integer job;//职位:1-员工,2-班主任,3-学工主管,4-教研主管,5-咨询师
    private Integer salary;//薪资
    private String image;//头像
    private LocalDate entryDate;//入职日期
    private Integer deptId;//关联的部门ID
    private LocalDateTime createTime;//创建时间
    private LocalDateTime updateTime;//修改时间
    private String role; // ADMIN / TEACHER

    //封装部门名称
    private String deptName;//部门名称

    //封装工作经历列表数据
    private List<EmpExpr> exprList;

    // 旧密码字段，用于修改密码时验证
    private String oldPassword;

    // 新密码字段，用于修改密码
    private String newPassword;
}
