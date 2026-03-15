package com.javaweb.entity;

import lombok.Data;

import java.util.List;

@Data
public class StudentCountList {
    private List<String> clazzList;
    private List<Integer> dataList;
}
