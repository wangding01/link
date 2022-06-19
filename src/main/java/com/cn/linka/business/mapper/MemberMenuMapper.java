package com.cn.linka.business.mapper;


import com.cn.linka.business.dao.MemberMenuDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMenuMapper {
    List<MemberMenuDao> queryAll();

    List<MemberMenuDao> queryAllToUser();

    void insert(MemberMenuDao memberMenuDao);

    void update(MemberMenuDao memberMenuDao);

    Optional<MemberMenuDao> getMenuById(Long id);
}

