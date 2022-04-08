package com.cn.linka.business.service.impl;

import com.cn.linka.business.dao.User;
import com.cn.linka.business.mapper.UserMapper;
import com.cn.linka.business.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Override
    public List<User> queryUserList() {
        return userMapper.queryUserList();
    }
}
