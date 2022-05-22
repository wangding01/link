package com.cn.linka.business.service;

import com.cn.linka.business.dao.*;

import java.util.List;

public interface UserService {
    List<User> queryUserList();

    BaseDaoForHttp email(String to);

    BaseDaoForHttp<UserRegisteredDao> registered(String email, String verifyCode, String password);

    BaseDaoForHttp checkEmail(String email);

    BaseDaoForHttp checkEmailVerifyCode(String email, String verifyCode);

    BaseDaoForHttp<UserLogin> userEmailLogin(String email, String passWord);

    BaseDaoForHttp userUpdate(UserUpdate user);

    BaseDaoForHttp<User> getUserByUserId(String userId);

    BaseDaoForHttp userUpdatePassword(UserUpdatePasswordDao user);

    BaseDaoForHttp<UserLinkBase> userDetail(String userId);

    BaseDaoForHttp<UserLogin> userWxLogin(String openId, String wxNickName, String headUrl);
}
