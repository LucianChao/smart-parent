package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class LLMReportService {

    @Value("${qwen.api-key}")
    private String apiKey;

    public String generateReport(String summary) throws Exception {
        String prompt = """
                你是一名【教育管理专家 + 数据分析专家】。
                请基于以下数据分析结果，生成一份详细的【智能分析报告】。
                完成以下任务：
                1️⃣ 给出整体运行状态结论（是否健康、是否存在风险）
                2️⃣ 从【学员管理】【班级管理】【员工管理】三个维度分别分析问题
                3️⃣ 指出潜在风险（如违规集中、管理失衡等）
                4️⃣ 给出【具体、可执行】的改进建议（不少于5条）
                要求：
                1. 总体结论
                2. 学员分析
                3. 班级分析
                4. 员工分析
                5. 改进建议
                6. 表达专业、像真实管理报告
                7. 不要使用“根据以上数据可知”这种模板话
                
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

    public String generateDetailedReport(String studentsJson,
                                 String clazzJson,
                                 String empJson) throws Exception {

        String prompt = """
                你是一名教育管理系统的数据分析专家。
                请基于以下完整数据，生成一份详细的【智能分析报告】。
                
                要求：
                1. 总体结论
                2. 学员分析
                3. 班级分析
                4. 员工分析
                5. 改进建议
                
                【学员数据】：
                %s
                
                【班级数据】：
                %s
                
                【员工数据】：
                %s
                """.formatted(studentsJson, clazzJson, empJson);

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
