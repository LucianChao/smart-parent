package com.javaweb.mapper;

import com.javaweb.entity.Clazz;
import com.javaweb.entity.ClazzQueryParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClazzMapper {
    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    List<Clazz> page(ClazzQueryParam param);

    /**
     * 删除班级
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 新增班级
     *
     * @param clazz
     */
    void save(Clazz clazz);

    /**
     * 根据ID查询班级
     *
     * @param id
     * @return
     */
    Clazz getById(Integer id);

    /**
     * 修改班级
     *
     * @param clazz
     */
    void update(Clazz clazz);

    /**
     * 查询所有班级
     *
     * @return
     */
    List<Clazz> list();
}
