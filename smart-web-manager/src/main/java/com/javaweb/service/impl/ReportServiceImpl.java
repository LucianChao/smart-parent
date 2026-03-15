package com.javaweb.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaweb.entity.*;
import com.javaweb.mapper.ReportMapper;
import com.javaweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Value("${qwen.api-key}")
    private String apiKey;

    /**
     * 获取员工性别数据
     *
     * @return
     */
    @Override
    public List<EmpGenderData> genderCount() {
        List<EmpGenderData> empGenderDataList = reportMapper.genderCount();
        return empGenderDataList;
    }

    /**
     * 获取员工职位数据
     *
     * @return
     */
    @Override
    public EmpJobData jobCount() {
        List<EmpJobCount> empJobDataList = reportMapper.jobCount();
        EmpJobData result = new EmpJobData();
        result.setJobList(empJobDataList.stream().map(EmpJobCount::getJobName).toList());
        result.setDataList(empJobDataList.stream().map(EmpJobCount::getCnt).toList());
        return result;
    }

    /**
     * 获取学生学历数据
     *
     * @return
     */
    @Override
    public List<StudentDegreeData> stuDegreeData() {
        List<StudentDegreeData> stuDegreeData = reportMapper.stuDegreeData();
        return stuDegreeData;
    }

    /**
     * 获取班级人数统计
     *
     * @return
     */
    @Override
    public StudentCountList studentCountData() {
        List<StudentCount> studentCounts = reportMapper.studentCountData();
        StudentCountList result = new StudentCountList();
        result.setClazzList(studentCounts.stream().map(StudentCount::getClazz).toList());
        result.setDataList(studentCounts.stream().map(StudentCount::getData).toList());
        return result;
    }
    
    /**
     * 生成AI分析报告
     * @param summary 数据摘要
     * @return AI生成的分析报告
     * @throws Exception
     */
    @Override
    public String generateReport(String summary) throws Exception {
        String prompt = """
                你是一名教育管理系统的数据分析专家。
                请基于以下数据分析结果，生成一份详细的【智能分析报告】。
                
                要求：
                1. 总体结论
                2. 学员分析
                3. 班级分析
                4. 员工分析
                5. 改进建议
                
                【数据分析摘要】：
                %s
                """.formatted(summary);

        String body = """
                {
                  "model": "qwen-turbo",
                  "input": {
                    "prompt": "%s"
                  }
                }
                """.formatted(prompt
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}