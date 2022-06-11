package com.cn.linka.business.mapper;
import com.cn.linka.business.dao.MemberThemeDao;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberThemeMapper {
    MemberThemeDao selectThemeMember(String memberType);
}

