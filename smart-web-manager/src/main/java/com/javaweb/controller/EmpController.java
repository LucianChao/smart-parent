package com.javaweb.controller;

import com.javaweb.annotation.LogOperate;
import com.javaweb.entity.Emp;
import com.javaweb.entity.EmpQueryParam;
import com.javaweb.entity.PageBean;
import com.javaweb.entity.Result;
import com.javaweb.service.EmpService;
import com.javaweb.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j//  日志
@RestController//  //等价于@Controller + @ResponseBody
@RequestMapping("/emps")
public class EmpController {
    @Autowired
    private EmpService empService;

    //    @GetMapping("/emps")
//    public Result page(String name, Integer gender,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
//                       @RequestParam(defaultValue = "1") Integer page,
//                       @RequestParam(defaultValue = "10") Integer pageSize) {
//        log.info("分页查询: {}, {}, {}, {}, {}, {}", name, gender, begin, end, page, pageSize);
//        PageBean pageBean = empService.page(name, gender, begin, end, page, pageSize);
//        return Result.success(pageBean);
//    }
    @GetMapping
    public Result page(EmpQueryParam param) {
        log.info("分页查询: {}, {}, {}, {}, {}, {}", param.getName(), param.getGender(), param.getBegin(), param.getEnd(), param.getPage(), param.getPageSize());
        PageBean pageBean = empService.page(param);
        return Result.success(pageBean);
    }

    /**
     * 新增员工
     *
     * @param emp
     * @return
     */
    @LogOperate("新增员工")
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工:{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 删除员工
     *
     * @param ids
     * @return
     */
    //    public Result delete(Integer[] ids)
//    通过数组即可接收前端传递的数组值
    @LogOperate("删除员工")
    @DeleteMapping
//    通过集合类型接收前端传递的数组值，需要加注解 @RequestParam
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("ids = {}", ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * 回显员工信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("回显员工id = {}", id);
        Emp emp = empService.getById(id);
        return Result.success(emp);
    }

    /**
     * 修改员工信息
     *
     * @param emp
     * @return
     */
    @LogOperate("修改员工")
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工：emp = {}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 获取员工列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result list(){
        List<Emp> empList = empService.list();
        return Result.success(empList);
    }

    /**
     * 修改密码
     * @param emp 包含旧密码和新密码的员工对象
     * @return
     */
    @LogOperate("修改密码")
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Emp emp) {
        try {
            // 从JWT中获取当前用户ID
            Integer currentUserId = JWTUtils.getCurrentUserId();
            if (currentUserId == null) {
                return Result.error("用户未登录");
            }

            // 直接使用当前用户ID，不检查是否是本人操作
            // 调用服务层修改密码
            boolean success = empService.updatePassword(currentUserId, emp.getOldPassword(), emp.getNewPassword());
            if (success) {
                return Result.success("密码修改成功");
            } else {
                return Result.error("旧密码不正确");
            }
        } catch (Exception e) {
            log.error("修改密码失败", e);
            return Result.error("密码修改失败：" + e.getMessage());
        }
    }
}
