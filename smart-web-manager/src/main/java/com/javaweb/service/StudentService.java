package com.javaweb.service;

import com.javaweb.entity.PageBean;
import com.javaweb.entity.Student;
import com.javaweb.entity.StudentQueryParam;

import java.util.List;

public interface StudentService {

    /**
     * 学生列表分页查询
     *
     * @param param
     * @return
     */
    PageBean page(StudentQueryParam param);

    /**
     * 批量删除学生
     *
     * @param ids
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 保存学生信息
     *
     * @param student
     */
    void save(Student student);

    /**
     * 根据ID查询学生信息
     *
     * @param id
     * @return
     */
    Student getById(Integer id);

    /**
     * 修改学生信息
     *
     * @param student
     */
    void update(Student student);

    /**
     * 违纪处理
     *
     * @param id
     * @param score
     */
    void violation(Integer id, Integer score);
}
