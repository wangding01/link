package com.cn.linka.business.mapper;
import com.cn.linka.business.dao.EventLogDao;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLogMapper {
    void insert(EventLogDao eventLogDao);
}

