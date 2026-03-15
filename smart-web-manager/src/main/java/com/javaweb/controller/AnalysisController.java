package com.javaweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.entity.Clazz;
import com.javaweb.entity.Emp;
import com.javaweb.entity.Result;
import com.javaweb.entity.Student;
import com.javaweb.service.impl.AutoAnalysisService;
import com.javaweb.service.impl.LLMReportService;
import com.javaweb.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class AnalysisController {
    @Autowired
    private AutoAnalysisService autoAnalysisService;
    @Autowired
    private LLMReportService llmReportService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/autoAnalysis")
    public Result autoAnalysis() {
        try {
            // 获取用户信息
            Integer userId = JWTUtils.getCurrentUserId();
            String userRole = JWTUtils.getCurrentUserRole();
            log.info("用户ID:{} Role:{} 请求自动分析", userId, userRole);

            log.info("开始执行自动分析");
            Map<String, Object> analysis = autoAnalysisService.analyzeData();
            log.info("自动分析数据获取完成，准备生成报告");
            log.info("摘要信息: {}", analysis.get("summary"));
            String report = llmReportService.generateReport((String) analysis.get("summary"));
            log.info("AI报告生成完成");
            analysis.put("report", report);
            log.info("自动分析完成，返回结果");
            return Result.success(analysis);
        } catch (Exception e) {
            log.error("自动分析过程中出现异常", e);
            return Result.error("分析失败：" + e.getMessage());
        }
    }

    @GetMapping("/detailedAnalysis")
    public Result detailedAnalysis() {
        try {
            // 获取用户信息
            Integer userId = JWTUtils.getCurrentUserId();
            String userRole = JWTUtils.getCurrentUserRole();
            log.info("用户ID:{} Role:{} 请求详细分析", userId, userRole);

            log.info("开始执行详细分析");
            
            // 获取完整数据
            List<Student> students = autoAnalysisService.getStudentMapper().findAll();
            List<Clazz> clazzes = autoAnalysisService.getClazzMapper().list();
            List<Emp> emps = autoAnalysisService.getEmpMapper().getAll();
            
            String studentsJson = objectMapper.writeValueAsString(students);
            String clazzesJson = objectMapper.writeValueAsString(clazzes);
            String empsJson = objectMapper.writeValueAsString(emps);
            
            String report = llmReportService.generateDetailedReport(studentsJson, clazzesJson, empsJson);
            log.info("详细分析完成，返回结果");
            return Result.success(report);
        } catch (Exception e) {
            log.error("详细分析过程中出现异常", e);
            return Result.error("生成详细分析报告失败：" + e.getMessage());
        }
    }
}