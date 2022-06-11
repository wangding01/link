package com.cn.linka.business.mapper;


import com.cn.linka.business.dao.ThemeDao;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeMapper {
    List<ThemeDao> queryTheme();

    void insert(ThemeDao themeDao);

    void update(ThemeDao themeDao);

    Optional<ThemeDao> getThemeById(Long id);

    List<ThemeDao> selectByIds(HashSet<String> themeIds);
}

