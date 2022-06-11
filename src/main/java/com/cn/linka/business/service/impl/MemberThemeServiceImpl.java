package com.cn.linka.business.service.impl;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberThemeDao;
import com.cn.linka.business.mapper.MemberThemeMapper;
import com.cn.linka.business.service.MemberThemeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MemberThemeServiceImpl implements MemberThemeService {
    private static final String MEMBER_TYPE = "1";
    private static final String NOT_MEMBER_TYPE = "2";
    @Resource
    private MemberThemeMapper memberThemeMapper;

    @Override
    public BaseDaoForHttp<MemberThemeDao> selectTheme() {
        MemberThemeDao memberThemeDao = memberThemeMapper.selectThemeMember(MEMBER_TYPE);
        return BaseDaoForHttp.success(memberThemeDao);
    }
}
