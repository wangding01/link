package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.ThemeDao;

import java.util.List;

public interface ThemeService {
    BaseDaoForHttp<List<ThemeDao>> getAllTheme();

    BaseDaoForHttp insert(ThemeDao themeDao);

    BaseDaoForHttp update(ThemeDao themeDao);

    BaseDaoForHttp<ThemeDao> getThemeById(Long id);
}
