package com.javaweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaweb.entity.PageBean;
import com.javaweb.entity.Student;
import com.javaweb.entity.StudentQueryParam;
import com.javaweb.mapper.StudentMapper;
import com.javaweb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 学生列表分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageBean page(StudentQueryParam param) {
        //1.分页查询
        PageHelper.startPage(param.getPage(), param.getPageSize());
        //2.查询数据
        List<Student> studentList = studentMapper.page(param);
        Page p = (Page) studentList;
        //3.封装数据
        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 批量删除学生
     *
     * @param ids
     */
    @Override
    public void deleteBatch(List<Integer> ids) {
        //1.调用mapper方法
        studentMapper.deleteBatch(ids);
    }

    /**
     * 保存学生
     *
     * @param student
     */
    @Override
    public void save(Student student) {
        //1.1补齐缺失字段
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        //1.2调用mapper方法
        studentMapper.save(student);
    }

    /**
     * 根据ID查询学生
     *
     * @param id
     * @return
     */
    @Override
    public Student getById(Integer id) {
        Student student = studentMapper.getById(id);
        return student;
    }

    /**
     * 修改学生
     *
     * @param student
     */
    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    /**
     * 扣分
     *
     * @param id
     * @param score
     */
    @Override
    public void violation(Integer id, Integer score) {
        studentMapper.violation(id, score);
    }

}
