package com.javaweb.controller;


import com.javaweb.entity.PageBean;
import com.javaweb.entity.Result;
import com.javaweb.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result page(int  page, int pageSize){
        PageBean pageBean = logService.page(page, pageSize);
        return Result.success(pageBean);
    }
}
