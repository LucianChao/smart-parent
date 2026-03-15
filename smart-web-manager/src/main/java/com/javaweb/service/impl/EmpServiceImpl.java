package com.javaweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaweb.entity.*;
import com.javaweb.mapper.EmpExprMapper;
import com.javaweb.mapper.EmpMapper;
import com.javaweb.service.EmpLogService;
import com.javaweb.service.EmpService;
import com.javaweb.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;

    /**
     * 分页查询员工信息
     *
     * @param param
     * @return
     */
    @Override
    public PageBean page(EmpQueryParam param) {
        //1.设置分页参数
        PageHelper.startPage(param.getPage(), param.getPageSize());
        //2.调用mapper方法
        List<Emp> empList = empMapper.list(param);
        Page p = (Page) empList;    //强转对象，Page继承了ArrayList
        //3.封装PageBean对象并返回
        return new PageBean(p.getTotal(), p.getResult());
    }

    /**
     * 新增员工
     *
     * @param emp
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        try {
            //1.调用mapper方法，保存员工的基本信息到emp表
            //1.1补充缺失字段
            emp.setPassword("123456");
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            //1.2调用mapper方法
            empMapper.insert(emp);

            Integer id = emp.getId();
            log.info("id={}", id);
            //2.调用mapper方法，保存员工的工作经历到emp_expr表
            List<EmpExpr> exprList = emp.getExprList();
            //2.1关联员工id
            if (!CollectionUtils.isEmpty(exprList)) {
                for (EmpExpr empExpr : exprList) {
                    empExpr.setEmpId(id);
                    log.info("id设置为={}", id);
                }
                //2.2调用mapper方法，批量保存工作经历
                empExprMapper.insertBatch(exprList);
            }
        } finally {
            //无论新增员工是否成功，都需要添加操作日志
            EmpLog empLog = new EmpLog();
            empLog.setOperateTime(LocalDateTime.now());
            empLog.setInfo("插入员工信息 " + emp);
            empLogService.insertLog(empLog);
        }
    }

    /**
     * 删除员工
     *
     * @param ids
     */
    @Override
    @Transactional  //开启事务（涉及多表操作）
    public void delete(List<Integer> ids) {
        //1.删除员工基本信息 emp表
        empMapper.deleteBatch(ids);
        //2.删除员工工作经历 emp_expr表
        empExprMapper.deleteBatch(ids);
    }

    /**
     * 回显员工信息
     *
     * @param id
     * @return
     */
    @Override
    public Emp getById(Integer id) {
        //方式一
        //1.调用mapper的查询方法，查询员工基本信息以及经历列表信息
//        return empMapper.getById(id);

        //方式二
        //1.查询员工基本信息，封装到Emp对象中
        Emp emp = empMapper.getById2(id);
        //2.查询员工工作经历，封装到Emp对象中
        List<EmpExpr> empExprList = empExprMapper.getByEmpId(id);
        emp.setExprList(empExprList);
        //3.返回Emp对象
        return emp;
    }

    /**
     * 修改员工信息
     *
     * @param emp
     */
    @Override
    @Transactional
    public void update(Emp emp) {
        //1.修改员工的基本信息   emp表
        //1.1 补充缺失字段 -- 更新时间
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.update(emp);
        //2.修改员工的工作经历信息  emp_expr表
        //先删后增
        empExprMapper.deleteByEmpId(emp.getId());
        List<EmpExpr> exprList = emp.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            //关联员工id
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 查询所有员工信息
     *
     * @return
     */
    @Override
    public List<Emp> list() {
        //1.调用mapper方法
        List<Emp> empList = empMapper.getAll();
        //2.返回结果
        return empList;
    }

    /**
     * 员工登录
     *
     * @param emp
     * @return
     */
    @Override
    public EmpLoginInfo login(Emp emp) {
        //1.调用mapper方法查询
        Emp empDB = empMapper.selectByUsernameAndPassword(emp);
        //2.判断登录数据是否正确
        if (empDB != null) {
            //3.查到数据构造EmpLoginInfo对象并返回
            //完善登录逻辑，登录成功生成JWT令牌
            Map<String, Object> claims = new HashMap<>();

            //自定义载荷
            claims.put("id", empDB.getId());
            claims.put("username", empDB.getUsername());
            String jwt = JWTUtils.generateJwt(claims);
            return new EmpLoginInfo(empDB.getId(), empDB.getUsername(), empDB.getName(), jwt, empDB.getRole());
        }
        //3.如果数据为空，代表登录失败，返回null
        return null;
    }
    
    /**
     * 修改密码
     * @param id 员工ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    @Override
    public boolean updatePassword(Integer id, String oldPassword, String newPassword) {
        // 1. 验证旧密码是否正确
        Emp emp = empMapper.getByIdAndOldPassword(id, oldPassword);
        if (emp == null) {
            return false; // 旧密码不正确
        }
        
        // 2. 更新密码
        int rows = empMapper.updatePasswordById(id, newPassword);
        return rows > 0; // 返回是否更新成功
    }
}