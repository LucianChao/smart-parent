package com.javaweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装分页列表结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageBean {
    private Long total;     //总记录数
    private List rows;      //分页结果列表
}
