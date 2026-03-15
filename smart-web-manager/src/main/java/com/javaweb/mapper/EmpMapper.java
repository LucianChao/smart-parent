package com.javaweb.mapper;

import com.javaweb.entity.Emp;
import com.javaweb.entity.EmpQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface EmpMapper {
    /**
     * 统计员工总记录数
     * @return
     */
//    @Select("select count(*) from emp")
//    Long count();

    /**
     * 分页查询
     * @param start
     * @param pageSize
     * @return
     */
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id limit #{start},#{pageSize}")
//    List<Emp> page(Integer start, Integer pageSize);

    /**
     * 列表查询
     *
     * @return
     */
//    @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id")
    //基于XML开发-- 动态SQL
    List<Emp> list(EmpQueryParam param);

    /**
     * 新增员工基本信息
     *
     * @param emp
     */
//    @Options(useGeneratedKeys = true,keyProperty = "id")    //获取主键的值赋给id
//    @Insert("insert into emp values (null,#{username},#{password},#{name},#{gender},#{phone},#{job},#{salary},#{image}," +
//            "#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);

    /**
     * 批量删除员工基本信息
     *
     * @param ids
     */
    //基于XML开发- 动态SQL
    void deleteBatch(List<Integer> ids);

    /**
     * 根据id查询员工基本信息以及经历列表信息
     *
     * @param id
     * @return
     */
//    @Select("select * from emp e left join emp_expr ee on e.id = ee.emp_id where e.id = #{id}")
    //基于XML开发- 动态SQL
    Emp getById(Integer id);

    @Select("select * from emp where id = #{id}")
    Emp getById2(Integer id);

    /**
     * 修改员工基本信息
     * @param emp
     */
    //基于XML开发- 动态SQL
    void update(Emp emp);

    /**
     * 查询全部员工
     *
     * @return
     */
    List<Emp> getAll();

    /**
     * 登录
     * 根据用户名和密码查询员工
     *
     * @param emp
     * @return
     */
    Emp selectByUsernameAndPassword(Emp emp);
    
    /**
     * 根据ID更新密码
     * @param id 员工ID
     * @param password 新密码
     * @return 更新记录数
     */
    // 基于XML开发
    int updatePasswordById(Integer id, String password);
    
    /**
     * 根据ID和旧密码查询员工
     * @param id 员工ID
     * @param oldPassword 旧密码
     * @return 员工信息
     */
    // 基于XML开发
    Emp getByIdAndOldPassword(Integer id, String oldPassword);
}