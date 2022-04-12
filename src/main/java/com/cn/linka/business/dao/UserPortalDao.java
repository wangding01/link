package com.cn.linka.business.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.linka.business.bean.UserPortalBean;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: linka
 * @description: 用户主页信息
 * @author: wangding
 * @create: 2022-04-11 14:41
 */
@Data
public class UserPortalDao implements Serializable {
    private String userId;
    private String indexUrl;
    private String domain;
    private List<FactorPortalDao> factorPortalDaos;
    private Date createDt;

    public static UserPortalBean transferToBean(UserPortalDao userPortalDao){
        UserPortalBean userPortalBean = new UserPortalBean();
        userPortalBean.setCreateDt(userPortalDao.getCreateDt());
        userPortalBean.setDomain(userPortalDao.getDomain());
        userPortalBean.setIndexUrl(userPortalDao.getIndexUrl());
        userPortalBean.setUserId(userPortalDao.getUserId());
        userPortalBean.setAllMsg(JSON.toJSONString( userPortalDao.getFactorPortalDaos()));
        return userPortalBean;
    }
}