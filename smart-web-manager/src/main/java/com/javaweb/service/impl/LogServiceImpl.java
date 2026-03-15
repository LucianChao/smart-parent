package com.javaweb.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.javaweb.entity.OperateLog;
import com.javaweb.entity.PageBean;
import com.javaweb.mapper.LogMapper;
import com.javaweb.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public PageBean page(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OperateLog> logs = logMapper.page();
        Page res  = (Page) logs;
        return new PageBean(res.getTotal(), res.getResult());

    }
}
