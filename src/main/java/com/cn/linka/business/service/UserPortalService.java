package com.cn.linka.business.service;


import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserPortalDao;

public interface UserPortalService {
    BaseDaoForHttp portalInsert(UserPortalDao userPortalDao);

    BaseDaoForHttp<UserPortalDao> getPortalByUserId(String userId);

    BaseDaoForHttp<UserPortalDao> getPortalByIndex(String index);
}
