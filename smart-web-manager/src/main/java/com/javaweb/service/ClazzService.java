package com.javaweb.service;

import com.javaweb.entity.Clazz;
import com.javaweb.entity.ClazzQueryParam;
import com.javaweb.entity.PageBean;

import java.util.List;

public interface ClazzService {
    /**
     * 分页查询
     * @param param
     * @return
     */
    PageBean page(ClazzQueryParam param);
    /**
     * 删除班级
     * @param id
     */
    void delete(Integer id);
    /**
     * 新增班级
     * @param clazz
     */
    void save(Clazz clazz);

    /**
     * 根据ID查询班级
     * @param id
     * @return
     */
    Clazz getById(Integer id);

    /**
     * 修改班级
     * @param clazz
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级
     * @return
     */
    List<Clazz> list();
}