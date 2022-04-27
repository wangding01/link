package com.cn.linka.business.service.impl;

import com.cn.linka.business.dao.EventLogDao;
import com.cn.linka.business.mapper.EventLogMapper;
import com.cn.linka.business.service.EventService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-27 17:18
 */
@Service
public class EventServiceImpl implements EventService {
    @Resource
    private EventLogMapper eventLogMapper;

    @Override
    public void insert(EventLogDao eventLogDao) {
        eventLogMapper.insert(eventLogDao);
    }
}