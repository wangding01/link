package com.cn.linka.business.service.impl;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserPortalDao;
import com.cn.linka.business.mapper.UserPortalMapper;
import com.cn.linka.business.service.UserPortalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * UserPortalService实现类
 */
@Service
@Slf4j
public class UserPortalServiceImpl implements UserPortalService {
    @Resource
    private UserPortalMapper userPortalMapper;
    @Override
    public BaseDaoForHttp portalInsert(UserPortalDao userPortalDao) {
        userPortalDao.setCreateDt(new Date());
        userPortalMapper.insert(userPortalDao);
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByUserId(String userId) {
        return BaseDaoForHttp.success(userPortalMapper.selectByUserId(userId).get());
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByIndex(String index) {
        return BaseDaoForHttp.success(userPortalMapper.getPortalByIndex(index));
    }
}
