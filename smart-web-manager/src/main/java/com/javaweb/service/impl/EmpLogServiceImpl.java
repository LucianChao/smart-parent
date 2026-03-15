package com.javaweb.service.impl;

import com.javaweb.entity.EmpLog;
import com.javaweb.mapper.EmpLogMapper;
import com.javaweb.service.EmpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements EmpLogService {
    @Autowired
    private EmpLogMapper empLogMapper;

    /**
     * 插入操作日志
     *
     * @param empLog
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void insertLog(EmpLog empLog) {
         empLogMapper.insert(empLog);
    }
}
