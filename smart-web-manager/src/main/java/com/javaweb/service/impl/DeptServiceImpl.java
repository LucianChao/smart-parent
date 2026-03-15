package com.javaweb.service.impl;

import com.javaweb.entity.Dept;
import com.javaweb.mapper.DeptMapper;
import com.javaweb.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service//表示当前类是服务类，被Spring管理
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    public List<Dept> list() {
        //1.调用mapper的方法，获取列表数据并返回
        return deptMapper.list();
    }

    /**
     * 根据id删除部门
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //调用Mapper的删除方法
        deptMapper.delete(id);
    }

    /**
     * 保存部门
     * @param dept
     */
    @Override
    public void save(Dept dept) {
        //1.补充基础属性
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        //2.调用mapper方法
        deptMapper.save(dept);
    }

    /**
     * 数据回显
     * @param id
     * @return
     */
    @Override
    public Dept getById(Integer id) {
        //调用mapper方法
        return deptMapper.getById(id);
    }

    /**
     * 修改部门
     * @param dept
     */
    @Override
    public void update(Dept dept) {
        //1.补充基础属性
//        dept.setUpdateTime(LocalDateTime.now());
        //2.调用mapper方法
        deptMapper.update(dept);
    }

}
