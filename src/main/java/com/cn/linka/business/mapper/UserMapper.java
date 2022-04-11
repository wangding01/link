package com.cn.linka.business.mapper;

import com.cn.linka.business.dao.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMapper {
    List<User> queryUserList();

    void insert(User user);

    Optional<User> selectByEmail(String email);
}

