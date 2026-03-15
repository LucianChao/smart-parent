package com.javaweb.controller;

import com.javaweb.entity.*;
import com.javaweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取员工性别数据
     * @return
     */
    @GetMapping("/empGenderData")
    public Result genderCount() {
        List<EmpGenderData> empGenderDataList= reportService.genderCount();
        return Result.success(empGenderDataList);
    }

    /**
     * 获取员工职位数据
     * @return
     */
    @GetMapping("/empJobData")
    public Result jobCount() {
        EmpJobData empJobData = reportService.jobCount();
        return Result.success(empJobData);
    }

    /**
     * 获取学生学历数据
     * @return
     */
    @GetMapping("/studentDegreeData")
    public  Result studentDegreeData() {
        List<StudentDegreeData> stuDegreeData = reportService.stuDegreeData();
        return Result.success(stuDegreeData);
    }
    /**
     * 班级人数统计
     */
    @GetMapping("/studentCountData")
    public Result studentCountData() {
        StudentCountList studentCountList= reportService.studentCountData();
        return Result.success(studentCountList);
    }
}
