package com.javaweb.service;

import com.javaweb.entity.PageBean;

public interface LogService {
    PageBean page(int page, int pageSize);
}
