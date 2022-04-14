package com.cn.linka.business.mapper;


import com.cn.linka.business.dao.ThemeDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeMapper {
    List<ThemeDao> queryTheme();

    void insert(ThemeDao themeDao);

    void update(ThemeDao themeDao);
}

