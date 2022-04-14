package com.cn.linka.business.service.impl;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberMenuDao;
import com.cn.linka.business.dao.ThemeDao;
import com.cn.linka.business.mapper.MemberMenuMapper;
import com.cn.linka.business.mapper.ThemeMapper;
import com.cn.linka.business.service.MemberMenuService;
import com.cn.linka.business.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MemberMenuServiceImpl implements MemberMenuService {
    @Resource
    private MemberMenuMapper memberMenuMapper;

    @Override
    public BaseDaoForHttp<List<MemberMenuDao>> getAll() {
        return BaseDaoForHttp.success(memberMenuMapper.queryAll());
    }

    @Override
    public BaseDaoForHttp insert(MemberMenuDao memberMenuDao) {
        memberMenuDao.setCreateDt(new Date());
        memberMenuMapper.insert(memberMenuDao);
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp update(MemberMenuDao memberMenuDao) {
        memberMenuMapper.update(memberMenuDao);
        return BaseDaoForHttp.success();
    }
}
