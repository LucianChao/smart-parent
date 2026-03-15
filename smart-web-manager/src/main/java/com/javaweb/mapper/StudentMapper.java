package com.javaweb.mapper;

import com.javaweb.entity.Student;
import com.javaweb.entity.StudentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface StudentMapper {
    /**
     * 学生列表分页查询
     *
     * @param param
     * @return
     */
    List<Student> page(StudentQueryParam param);

    /**
     * 批量删除学生
     *
     * @param ids
     */
    void deleteBatch(List<Integer> ids);

    /**
     * 保存学生
     *
     * @param student
     */
    void save(Student student);

    /**
     * 根据ID查询学生
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
     * 违规处理
     *
     * @param id
     * @param score
     */
    void violation(Integer id, Integer score);

    @Select("SELECT * FROM student")
    List<Student> findAll();
}
