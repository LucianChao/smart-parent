package com.javaweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpLoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String token;
    private String role;
}
