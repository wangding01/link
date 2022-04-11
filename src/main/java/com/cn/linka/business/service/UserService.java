package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserLogin;
import com.cn.linka.business.dao.UserRegisteredDao;

import java.util.List;

public interface UserService {
    List<User> queryUserList();

    BaseDaoForHttp email(String to);

    BaseDaoForHttp<UserRegisteredDao> registered(String email, String verifyCode, String password);

    BaseDaoForHttp checkEmail(String email);

    BaseDaoForHttp checkEmailVerifyCode(String email, String verifyCode);

    BaseDaoForHttp<UserLogin> userEmailLogin(String email, String passWord);
}
