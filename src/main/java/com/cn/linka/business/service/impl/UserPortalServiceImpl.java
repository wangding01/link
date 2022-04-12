package com.cn.linka.business.service.impl;
import com.cn.linka.business.bean.UserPortalBean;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FactorPortalDao;
import com.cn.linka.business.dao.UserPortalDao;
import com.cn.linka.business.mapper.UserPortalMapper;
import com.cn.linka.business.service.UserPortalService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        userPortalMapper.insert(UserPortalDao.transferToBean(userPortalDao));
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByUserId(String userId) {
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(userPortalMapper.selectByUserId(userId).get());
        List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
        userPortalDao.setFactorPortalDaos(collect);
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByIndex(String index) {
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(userPortalMapper.getPortalByIndex(index).get());
        List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
        userPortalDao.setFactorPortalDaos(collect);
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    public BaseDaoForHttp portalUpdate(UserPortalDao userPortalDao) {
        if(StringUtils.isEmpty(userPortalDao.getUserId())){
            return BaseDaoForHttp.fail(7008,"用户id不能为空");
        }
        if(userPortalMapper.portalUpdate(UserPortalDao.transferToBean(userPortalDao))<1){
            return BaseDaoForHttp.fail(7010,"用户主页信息修改失败");
        }
        return BaseDaoForHttp.success();
    }
}
