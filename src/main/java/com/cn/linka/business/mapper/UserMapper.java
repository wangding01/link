package com.cn.linka.business.mapper;

import com.cn.linka.business.dao.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> queryUserList();
}

