package com.cn.linka.business.mapper;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserPortalDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPortalMapper {
    void insert(UserPortalDao userPortalDao);

    Optional<UserPortalDao> selectByUserId(String userId);

    Optional<UserPortalDao> getPortalByIndex(String index);
}

