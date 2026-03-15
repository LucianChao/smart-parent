package com.javaweb.mapper;

import com.javaweb.entity.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    /**
     * 批量插入员工工作经历
     * @param exprList
     */
    //基于XML开发-动态SQL -- <foreach>
    void insertBatch(List<EmpExpr> exprList);

    /**
     * 批量删除员工工作经历
     * @param ids
     */
    //基于XML开发-动态SQL -- <foreach>
    void deleteBatch(List<Integer> ids);

    /**
     * 根据员工id查询员工工作经历列表
     * @param empId
     * @return
     */
    //基于XML开发-动态SQL -- <foreach>
    List<EmpExpr> getByEmpId(Integer empId);

    /**
     * 根据员工id删除员工工作经历
     * @param id
     */
    void deleteByEmpId(Integer id);
}
