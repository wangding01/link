package com.cn.linka.business.mapper;


import com.cn.linka.business.dao.MemberMenuDao;
import com.cn.linka.business.dao.ThemeDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMenuMapper {
    List<MemberMenuDao> queryAll();

    void insert(MemberMenuDao memberMenuDao);

    void update(MemberMenuDao memberMenuDao);
}

