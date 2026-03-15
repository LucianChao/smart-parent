package com.javaweb.controller;

import com.javaweb.annotation.LogOperate;
import com.javaweb.entity.Dept;
import com.javaweb.entity.Result;
import com.javaweb.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 请求处理类
 */
@Slf4j  //会自动生成日志对象，用于日志记录
@RequestMapping("/depts")
@RestController   //等价于@Controller + @ResponseBody
public class DeptController {

//    @Resource(name = "deptServiceImpl")
    @Autowired
    private DeptService deptService;

    /**
     * 部门列表查询
     *
     * @return
     */
    @GetMapping  //限制请求方式为GET
    public Result getAll() {
//        1. 调用deptService
        List<Dept> deptList = deptService.list();
//        2.响应数据（json格式）
        return Result.success(deptList);
    }

    //    @ReqeuestMapping(value = "/depts",method = RequestMethod.DELETE)
    //    public Result delete(@RequestParam("id") Integer deptId) {

    /**
     * 删除部门
     * @param id
     * @return
     */
    @LogOperate("删除部门")
    @DeleteMapping
    public Result delete(Integer id) {
//        log.info("deptId=" + id);
        log.info("id = {}", id);
        //调用service方法
        deptService.delete(id);
        return Result.success();
    }

    /**
     * 保存部门
     *
     * @param dept
     * @return
     */
    @LogOperate("新增部门")
    @PostMapping
    public Result save(@RequestBody Dept dept) {
        log.info("dept= {}", dept);
        //调用service方法
        deptService.save(dept);
        return Result.success();
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     * @PathVariable 路径变量
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("id = {}", id);
        //调用service方法
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     *
     * @param dept
     * @return
     */
    @LogOperate("修改部门")
    @PutMapping
    public Result update(@RequestBody Dept dept) {
        log.info("dept = {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
