package com.javaweb.mapper;

import com.javaweb.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReportMapper {
    /**
     * 查询员工性别数据
     *
     * @return
     */
    List<EmpGenderData> genderCount();

    /**
     * 查询员工职位数据
     *
     * @return
     */
    List<EmpJobCount> jobCount();

    /**
     * 查询学生学历数据
     *
     * @return
     */
    List<StudentDegreeData> stuDegreeData();

    /**
     * 查询班级人数统计
     *
     * @return
     */
    List<StudentCount> studentCountData();
}
