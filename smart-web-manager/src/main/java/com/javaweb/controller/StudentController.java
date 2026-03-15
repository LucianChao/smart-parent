package com.javaweb.controller;

import com.javaweb.annotation.LogOperate;
import com.javaweb.entity.PageBean;
import com.javaweb.entity.Result;
import com.javaweb.entity.Student;
import com.javaweb.entity.StudentQueryParam;
import com.javaweb.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    /**
     * 学生列表分页查询
     * @param param
     * @return
     */
    @GetMapping
    public Result page(StudentQueryParam param){
        PageBean pageBean = studentService.page(param);
        return Result.success(pageBean);
    }

    /**
     * 批量删除学生
     * @param ids
     * @return
     */
    @LogOperate("删除学员")
    @DeleteMapping("/{ids}")
    public Result deleteBatch(@PathVariable List<Integer> ids) {
        //1.调用Service
        studentService.deleteBatch(ids);
        //2.响应结果
        return Result.success();
    }

    /**
     * 新增学生
     * @param student
     * @return
     */
    @LogOperate("新增学员")
    @PostMapping
    public Result save(@RequestBody Student student){
        //1.调用Service
        studentService.save(student);
        //2.响应结果
        return Result.success();
    }

    /**
     * 根据ID查询学生
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        //1.调用Service
        Student student =studentService.getById(id);
        //2.响应结果
        return Result.success(student);
    }

    /**
     * 修改学生信息
     * @param student
     * @return
     */
    @LogOperate("修改学生信息")
    @PutMapping
    public Result update(@RequestBody Student student){
        //1.调用Service
        studentService.update(student);
        return Result.success();
    }

    /**
     * 违纪处理
     * @param id
     * @param score
     * @return
     */
    @LogOperate("学生违纪处理")
    @PutMapping("/violation/{id}/{score}")
    public Result violation(@PathVariable Integer id,@PathVariable Integer score){
        studentService.violation(id,score);
        return Result.success();
    }
}
