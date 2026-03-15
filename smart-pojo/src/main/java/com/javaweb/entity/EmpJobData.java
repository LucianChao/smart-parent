package com.javaweb.entity;

import lombok.Data;

import java.util.List;

@Data
public class EmpJobData {
    private List<String> jobList;
    private List<Integer> dataList;
}
