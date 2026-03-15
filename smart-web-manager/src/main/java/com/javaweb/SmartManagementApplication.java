package com.javaweb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 启动类
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan   //开启sevlet组件扫描
public class SmartManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartManagementApplication.class, args);
        log.info("项目启动成功...");
    }

}
