package com.cn.linka.business.bean;

import com.alibaba.fastjson.JSONObject;
import com.cn.linka.business.dao.FactorPortalDao;
import com.cn.linka.business.dao.UserPortalDao;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: linka
 * @description: 用户主页信息
 * @author: wangding
 * @create: 2022-04-11 14:41
 */
@Data
public class UserPortalBean {
    private String userId;
    private String indexUrl;
    private String domain;
    private Long defaultThemeId;
    private String allMsg;
    private Date createDt;

    public static UserPortalDao transferToDao(UserPortalBean userPortalBean){
        UserPortalDao userPortalDao = new UserPortalDao();
        userPortalDao.setCreateDt(userPortalBean.getCreateDt());
        userPortalDao.setDomain(userPortalBean.getDomain());
        userPortalDao.setIndexUrl(userPortalBean.getIndexUrl());
        userPortalDao.setDefaultThemeId(userPortalBean.getDefaultThemeId());
        userPortalDao.setUserId(userPortalBean.getUserId());
        List<FactorPortalDao> factorPortalDaos = JSONObject.parseArray(userPortalBean.getAllMsg(), FactorPortalDao.class);
        userPortalDao.setFactorPortalDaos(factorPortalDaos);
        return userPortalDao;
    }
}