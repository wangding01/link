package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberMenuDao;
import com.cn.linka.business.dao.ThemeDao;

import java.util.List;

public interface MemberMenuService {
    BaseDaoForHttp<List<MemberMenuDao>> getAll();

    BaseDaoForHttp insert(MemberMenuDao memberMenuDao);

    BaseDaoForHttp update(MemberMenuDao memberMenuDao);

    MemberMenuDao getMenuById(Long id);
}
