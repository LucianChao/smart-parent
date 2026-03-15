package com.javaweb.service.impl;

import com.javaweb.entity.Clazz;
import com.javaweb.entity.Emp;
import com.javaweb.entity.Student;
import com.javaweb.mapper.ClazzMapper;
import com.javaweb.mapper.EmpMapper;
import com.javaweb.mapper.LogMapper;
import com.javaweb.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AutoAnalysisService {

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClazzMapper clazzMapper;
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private LogMapper logMapper;

    public Map<String, Object> analyzeData() {
        log.info("开始进行数据分析");
        Map<String, Object> result = new HashMap<>();
        
        // 1. 获取学员数据
        log.info("获取学员数据");
        List<Student> stuList = studentMapper.findAll();
        log.info("获取到{}名学员数据", stuList.size());

        // 2. 高风险学员
        log.info("计算高风险学员");
        double avgViolation = stuList.stream().mapToInt(Student::getViolationCount).average().orElse(0);
        log.info("平均违规次数: {}", avgViolation);
        double stdViolation = Math.sqrt(stuList.stream().mapToDouble(s -> Math.pow(s.getViolationCount() - avgViolation, 2)).average().orElse(0));
        log.info("违规次数标准差: {}", stdViolation);
        List<Student> highRiskStu = stuList.stream().filter(s -> s.getViolationCount() > avgViolation + stdViolation).collect(Collectors.toList());
        log.info("识别出{}名高风险学员", highRiskStu.size());
        result.put("highRiskStu", highRiskStu);

        // 3. 异常班级
        log.info("分析异常班级");
        List<Clazz> clazzList = clazzMapper.list();
        log.info("获取到{}个班级", clazzList.size());
        Map<Integer, Long> clazzViolation = stuList.stream()
                .collect(Collectors.groupingBy(Student::getClazzId, Collectors.summingLong(Student::getViolationCount)));
        log.info("统计各班级违规次数完成");
        List<Map<String,Object>> highRiskClazz = clazzList.stream()
                .filter(c -> clazzViolation.getOrDefault(c.getId(),0L)
                        > clazzViolation.values().stream().mapToLong(l->l).average().orElse(0))
                .map(c -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("clazzName", c.getName());
                    map.put("violationCount", clazzViolation.getOrDefault(c.getId(),0L));
                    return map;
                })
                .collect(Collectors.toList());
        log.info("识别出{}个异常班级", highRiskClazz.size());

        result.put("highRiskClazz", highRiskClazz);

        // 4. 员工低绩效
        log.info("分析员工低绩效");
        List<Emp> empList = empMapper.getAll();
        log.info("获取到{}名员工数据", empList.size());
        List<Emp> lowPerfEmp = empList.stream()
                .filter(e -> e.getSalary() != null && e.getSalary() > 0) // 可以根据工资或任务判断绩效
                .collect(Collectors.toList());
        log.info("识别出{}名低绩效员工", lowPerfEmp.size());
        result.put("lowPerfEmp", lowPerfEmp);

        // 分析摘要文本
        StringBuilder summary = new StringBuilder();
        summary.append("高风险学员数量: ").append(highRiskStu.size()).append("\n")
                .append("异常班级数量: ").append(highRiskClazz.size()).append("\n")
                .append("低绩效员工数量: ").append(lowPerfEmp.size());
        result.put("summary", summary.toString());
        log.info("数据分析完成");
        return result;
    }
    
    // 添加getter方法，以便在控制器中访问mapper
    public StudentMapper getStudentMapper() {
        return studentMapper;
    }
    
    public ClazzMapper getClazzMapper() {
        return clazzMapper;
    }
    
    public EmpMapper getEmpMapper() {
        return empMapper;
    }
}