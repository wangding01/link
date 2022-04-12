package com.cn.linka.business.mapper;

import com.cn.linka.business.bean.UserPortalBean;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPortalMapper {
    void insert(UserPortalBean userPortalBean);

    Optional<UserPortalBean> selectByUserId(String userId);

    Optional<UserPortalBean> getPortalByIndex(String index);

    int portalUpdate(UserPortalBean userPortalBean);
}

