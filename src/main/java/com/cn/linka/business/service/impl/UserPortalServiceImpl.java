package com.cn.linka.business.service.impl;
import com.cn.linka.business.bean.UserPortalBean;
import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FactorPortalDao;
import com.cn.linka.business.dao.UserPortalDao;
import com.cn.linka.business.mapper.UserPortalMapper;
import com.cn.linka.business.service.UserPortalService;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        Optional<UserPortalBean> portalByUserId = userPortalMapper.selectByUserId(userId);
        if(!portalByUserId.isPresent()){
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_IS_NULL);
        }
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(portalByUserId.get());
        List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
        userPortalDao.setFactorPortalDaos(collect);
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByIndex(String index) {
        Optional<UserPortalBean> portalByIndex = userPortalMapper.getPortalByIndex(index);
        if(!portalByIndex.isPresent()){
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_IS_NULL);
        }
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(portalByIndex.get());
        if(userPortalDao.getFactorPortalDaos()!=null){
            List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
            userPortalDao.setFactorPortalDaos(collect);
        }
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    public BaseDaoForHttp portalUpdate(UserPortalDao userPortalDao) {
        if(StringUtils.isEmpty(userPortalDao.getUserId())){
            throw new BusException(BusinessExceptionEnum.USER_ID_ISNULL);
        }
        if(userPortalMapper.portalUpdate(UserPortalDao.transferToBean(userPortalDao))<1){
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_UPDATE_FAIL);
        }
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp checkIndexExist(String index) {
        Optional<UserPortalBean> portalByIndex = userPortalMapper.getPortalByIndex(index);
        if(portalByIndex.isPresent()){
            throw new BusException(BusinessExceptionEnum.INDEX_EXIST);
        }
        return BaseDaoForHttp.success();
    }
}
