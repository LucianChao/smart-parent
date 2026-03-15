package com.javaweb.controller;

import com.javaweb.annotation.LogOperate;
import com.javaweb.entity.Clazz;
import com.javaweb.entity.ClazzQueryParam;
import com.javaweb.entity.PageBean;
import com.javaweb.entity.Result;
import com.javaweb.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @GetMapping
    public Result page(ClazzQueryParam param) {
        log.info("分页查询：{},{},{},{},{}", param.getName(), param.getBegin(), param.getEnd(), param.getPage(), param.getPageSize());
        PageBean pageBean = clazzService.page(param);
        return Result.success(pageBean);
    }

    /**
     * 删除班级
     *
     * @param id
     * @return
     */
    @LogOperate("删除班级")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        //1.调用Service
        clazzService.delete(id);
        //2.响应结果
        return Result.success();
    }

    /**
     * 新增班级
     *
     * @param clazz
     * @return
     */
    @LogOperate("新增班级")
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        //1.调用Service
        clazzService.save(clazz);
        //2.响应结果
        return Result.success();
    }

    /**
     * 根据ID查询班级
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        //1.调用Service
        Clazz clazz = clazzService.getById(id);
        //2.响应结果
        return Result.success(clazz);
    }

    /**
     * 修改班级
     *
     * @param clazz
     * @return
     */
    @LogOperate("修改班级")
    @PutMapping
    public Result update(@RequestBody Clazz clazz) {
        //1.调用Service
        clazzService.update(clazz);
        //2.响应结果
        return Result.success();
    }

    /**
     * 查询所有班级
     *
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        //1.调用Service
        List<Clazz> clazzList = clazzService.list();
        //2.响应结果
        return Result.success(clazzList);
    }
}
