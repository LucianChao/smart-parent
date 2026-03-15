package com.javaweb.service;

import com.javaweb.entity.EmpGenderData;
import com.javaweb.entity.EmpJobData;
import com.javaweb.entity.StudentCountList;
import com.javaweb.entity.StudentDegreeData;

import java.util.List;

public interface ReportService {
    /**
     * 获取员工性别数据
     *
     * @return
     */
    List<EmpGenderData> genderCount();

    /**
     * 获取员工职位数据
     *
     * @return
     */
    EmpJobData jobCount();
    /**
     * 获取学生学历数据
     *
     * @return
     */
    List<StudentDegreeData> stuDegreeData();

    StudentCountList studentCountData();
    
    /**
     * 生成AI分析报告
     * @param summary 数据摘要
     * @return AI生成的分析报告
     * @throws Exception
     */
    String generateReport(String summary) throws Exception;
}