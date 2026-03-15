package com.javaweb.mapper;


import com.javaweb.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
    /**
     * 查询所有部门
     *
     * @return
     */
    //数据封装方式一：手动结果映射
//    @Results({
//            @Result(column = "create_time", property = "createTime"),
//            @Result(column = "update_time", property = "updateTime")
//    })
    @Select("select * from dept;")
    public List<Dept> list();

    /**
     * 根据id删除部门
     *
     * @param id
     */
    @Delete("delete from dept where id = #{id};")
    void delete(Integer id);

    @Insert("insert into dept(name,create_time,update_time) values (#{name},#{createTime},#{updateTime})")
    void save(Dept dept);

    /**
     * 根据id查询部门
     * @param id
     * @return
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * 根据id更新部门
     * @param dept
     */
//    @Update("update dept set name = #{name}, update_time= #{updateTime} where id= #{id}")
    void update(Dept dept);
}
