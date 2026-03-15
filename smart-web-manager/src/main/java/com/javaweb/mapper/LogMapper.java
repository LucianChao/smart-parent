package com.javaweb.mapper;

import com.javaweb.entity.OperateLog;
import com.javaweb.entity.PageBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    List<OperateLog> page();
}
