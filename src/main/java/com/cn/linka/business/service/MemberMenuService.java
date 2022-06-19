package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberMenuDao;

import java.util.List;

public interface MemberMenuService {
    BaseDaoForHttp<List<MemberMenuDao>> getAll();

    BaseDaoForHttp insert(MemberMenuDao memberMenuDao);

    BaseDaoForHttp update(MemberMenuDao memberMenuDao);

    MemberMenuDao getMenuById(Long id);

    BaseDaoForHttp<List<MemberMenuDao>> getAllToUser();
}
