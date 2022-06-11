package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberThemeDao;

/**
 * @program: linka
 * @description:
 * @author: wangding
 * @create: 2022-04-27 17:17
 */
public interface MemberThemeService {
    BaseDaoForHttp<MemberThemeDao> selectTheme();
}