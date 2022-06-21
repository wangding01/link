package com.cn.linka.business.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * UserPortalService实现类
 */
@Service
@Slf4j
public class UserPortalServiceImpl implements UserPortalService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
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
        if (!portalByUserId.isPresent()) {
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_IS_NULL);
        }
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(portalByUserId.get());
        if(userPortalDao.getFactorPortalDaos()!=null && userPortalDao.getFactorPortalDaos().size()>0){
            List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
            userPortalDao.setFactorPortalDaos(collect);
        }
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    public BaseDaoForHttp<UserPortalDao> getPortalByIndex(String index) {
        UserPortalDao userPortalDao = new UserPortalDao();
        if (stringRedisTemplate.hasKey(index)) {
            log.info("redis缓存查询");
            String jsonStr = stringRedisTemplate.opsForValue().get(index);
            userPortalDao = JSON.parseObject(jsonStr, UserPortalDao.class);
            return BaseDaoForHttp.success(userPortalDao);
        }

        Optional<UserPortalBean> portalByIndex = userPortalMapper.getPortalByIndex(index);
        if (!portalByIndex.isPresent()) {
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_IS_NULL);
        }
        userPortalDao = UserPortalBean.transferToDao(portalByIndex.get());
        if (userPortalDao.getFactorPortalDaos() != null) {
            List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
            userPortalDao.setFactorPortalDaos(collect);
        }
        stringRedisTemplate.opsForValue().set(index, JSON.toJSONString(userPortalDao),60, TimeUnit.DAYS);
        return BaseDaoForHttp.success(userPortalDao);
    }

    @Override
    @Transactional
    public BaseDaoForHttp portalUpdate(UserPortalDao userPortalDao) {
        if (StringUtils.isEmpty(userPortalDao.getUserId())) {
            throw new BusException(BusinessExceptionEnum.USER_ID_ISNULL);
        }
        if (userPortalMapper.portalUpdate(UserPortalDao.transferToBean(userPortalDao)) < 1) {
            log.info("当前无主页数据，新增操作");
            userPortalDao.setCreateDt(new Date());
            userPortalMapper.insert(UserPortalDao.transferToBean(userPortalDao));
        }
        UserPortalDao byUserIdInternal = getByUserIdInternal(userPortalDao.getUserId());
        stringRedisTemplate.opsForValue().set(byUserIdInternal.getIndexUrl(), JSON.toJSONString(byUserIdInternal),60, TimeUnit.DAYS);
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp checkIndexExist(String index) {
        Optional<UserPortalBean> portalByIndex = userPortalMapper.getPortalByIndex(index);
        if (portalByIndex.isPresent()) {
            throw new BusException(BusinessExceptionEnum.INDEX_EXIST);
        }
        return BaseDaoForHttp.success();
    }

    /**
     * 内部查询接口
     * @param userId
     * @return
     */
    public UserPortalDao getByUserIdInternal(String userId){
        Optional<UserPortalBean> portalByUserId = userPortalMapper.selectByUserId(userId);
        if (!portalByUserId.isPresent()) {
            throw new BusException(BusinessExceptionEnum.USER_PORTAL_IS_NULL);
        }
        UserPortalDao userPortalDao = UserPortalBean.transferToDao(portalByUserId.get());
        List<FactorPortalDao> collect = userPortalDao.getFactorPortalDaos().stream().sorted(Comparator.comparing(FactorPortalDao::getOrder)).collect(Collectors.toList());
        userPortalDao.setFactorPortalDaos(collect);
        return userPortalDao;
    }
}
