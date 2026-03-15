package com.javaweb.controller;

import com.javaweb.annotation.LogOperate;
import com.javaweb.entity.Emp;
import com.javaweb.entity.EmpLoginInfo;
import com.javaweb.entity.Result;
import com.javaweb.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private EmpService empService;

    /**
     * 员工登录
     *
     * @param emp
     * @return
     */
    @LogOperate("登录账号")
    @PostMapping
    public Result login(@RequestBody Emp emp) {
        log.info("员工登录：{}", emp);
        EmpLoginInfo loginInfo = empService.login(emp);
        if (loginInfo == null) {
            return Result.error("用户名或密码错误！！");
        }
        return Result.success(loginInfo);
    }
}
