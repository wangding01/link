package com.cn.linka.business.service;

import com.cn.linka.business.dao.EventLogDao;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-27 17:17
 */
public interface EventService {
    void insert(EventLogDao eventLogDao);
}