package com.javaweb.service;

import com.javaweb.entity.Emp;
import com.javaweb.entity.EmpLoginInfo;
import com.javaweb.entity.EmpQueryParam;
import com.javaweb.entity.PageBean;

import java.util.List;

public interface EmpService {
    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    PageBean page(EmpQueryParam param);

    /**
     * 新增员工
     *
     * @param emp
     */
    void save(Emp emp);

    /**
     * 删除员工
     *
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 员工回显
     *
     * @param id
     * @return
     */
    Emp getById(Integer id);

    /**
     * 修改员工
     *
     * @param emp
     */
    void update(Emp emp);

    /**
     * 获取员工列表
     *
     * @return
     */
    List<Emp> list();


    /**
     * 员工登录
     *
     * @param emp
     * @return
     */
    EmpLoginInfo login(Emp emp);
    
    /**
     * 修改密码
     * @param id 员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean updatePassword(Integer id, String oldPassword, String newPassword);
}