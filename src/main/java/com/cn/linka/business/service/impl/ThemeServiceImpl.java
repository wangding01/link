package com.cn.linka.business.service.impl;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.BaseDaoForHttpByPageNo;
import com.cn.linka.business.dao.ThemeDao;
import com.cn.linka.business.mapper.ThemeMapper;
import com.cn.linka.business.service.ThemeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ThemeServiceImpl implements ThemeService {
    @Resource
    private ThemeMapper themeMapper;

    @Override
    public BaseDaoForHttp<List<ThemeDao>> getAllTheme() {
        return BaseDaoForHttp.success(themeMapper.queryTheme());
    }

    @Override
    public BaseDaoForHttp insert(ThemeDao themeDao) {
        themeDao.setCreateDt(new Date());
        themeMapper.insert(themeDao);
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp update(ThemeDao themeDao) {
        themeMapper.update(themeDao);
        return BaseDaoForHttp.success();
    }

    @Override
    public BaseDaoForHttp<ThemeDao> getThemeById(Long id) {
        return BaseDaoForHttp.success(themeMapper.getThemeById(id).orElse(null));
    }

    @Override
    public BaseDaoForHttpByPageNo<List<ThemeDao>> getAllThemePage(int pageSize, int offset) {
        int total = themeMapper.getTotal();
        offset = pageSize * (offset - 1);
        List<ThemeDao> themeDaoList = themeMapper.getAllThemePage(pageSize, offset);
        return BaseDaoForHttpByPageNo.success(themeDaoList, offset, total, themeDaoList.size());
    }
}
