package com.javaweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaweb.entity.Clazz;
import com.javaweb.entity.ClazzQueryParam;
import com.javaweb.entity.PageBean;
import com.javaweb.mapper.ClazzMapper;
import com.javaweb.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @Override
    public PageBean page(ClazzQueryParam param) {
        //1.用pageHelper设置分页参数
        PageHelper.startPage(param.getPage(), param.getPageSize());
        //2.调用mapper方法
        List<Clazz> clazzList = clazzMapper.page(param);
        LocalDate now = LocalDate.now();
        for (Clazz clazz : clazzList) {
            if (clazz.getBeginDate().isAfter(now)) {
                clazz.setStatus("未开班");
            } else if (clazz.getBeginDate().isBefore(now) && clazz.getEndDate().isAfter(now)) {
                clazz.setStatus("已开班");
            } else {
                clazz.setStatus("已结课");
            }
        }
        Page page = (Page) clazzList;
        //3.封装PageBean对象并返回
        return new PageBean(page.getTotal(), page.getResult());
    }

    /**
     * 删除班级
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //1.调用mapper方法
        clazzMapper.delete(id);
    }

    /**
     * 新增班级
     *
     * @param clazz
     */
    @Override
    public void save(Clazz clazz) {
        //1.1补齐字段
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        //1.2调用mapper方法
        clazzMapper.save(clazz);
    }

    /**
     * 根据ID查询班级
     *
     * @param id
     * @return
     */
    @Override
    public Clazz getById(Integer id) {
        //1.调用mapper方法
        return clazzMapper.getById(id);
    }

    /**
     * 修改班级
     *
     * @param clazz
     */
    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }

    /**
     * 查询所有班级
     *
     * @return
     */
    @Override
    public List<Clazz> list() {
        //1.调用mapper方法
        List<Clazz> clazzList = clazzMapper.list();
        //2.返回结果
        return clazzList;
    }
}
