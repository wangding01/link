package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserLogin;
import com.cn.linka.business.dao.UserRegisteredDao;
import com.cn.linka.business.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试第一个controller
 */
@Controller
@Api(value = "用户controller", tags = {"用户操作接口"})
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/getUser")
    @ResponseBody
    public BaseDaoForHttp<List<User>> getUser() {
        return BaseDaoForHttp.success(userService.queryUserList());
    }

    @PostMapping("/registered-email")
    @ResponseBody
    @ApiOperation("邮箱注册")
    public BaseDaoForHttp<UserRegisteredDao> registeredForEmail(String email, String verifyCode, String password) {
        return userService.registered(email, verifyCode, password);
    }

    @GetMapping("/check-email")
    @ResponseBody
    @ApiOperation("检查邮箱是否被注册")
    public BaseDaoForHttp checkEmail(String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/get-email-verify-code")
    @ResponseBody
    @ApiOperation("邮箱获取验证码")
    public BaseDaoForHttp getVerifyCode(String email) {
        return userService.email(email);
    }

    @GetMapping("/check-email-verifyCode")
    @ResponseBody
    @ApiOperation("检查邮箱验证码")
    public BaseDaoForHttp checkEmailVerifyCode(String email, String verifyCode) {
        return userService.checkEmailVerifyCode(email, verifyCode);
    }

    @GetMapping("/user-email-login")
    @ResponseBody
    @ApiOperation("用户邮箱登录")
    public BaseDaoForHttp<UserLogin> userEmailLogin(String email, String passWord) {
        return userService.userEmailLogin(email, passWord);
    }

    @PostMapping("/user-update")
    @ResponseBody
    @ApiOperation("用户信息维护")
    public BaseDaoForHttp userUpdate(@RequestBody User user) {
        return userService.userUpdate(user);
    }

    @PostMapping("/get-user-by-userId")
    @ResponseBody
    @ApiOperation("用户信息查询")
    public BaseDaoForHttp<User> getUserByUserId(String userId) {
        return userService.getUserByUserId(userId);
    }
}
