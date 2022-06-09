package com.cn.linka.business.controller;

import com.cn.linka.business.dao.*;
import com.cn.linka.business.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Email;
import java.util.List;

/**
 * 测试第一个controller
 */
@Validated
@RestController
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
    public BaseDaoForHttp<UserRegisteredDao> registeredForEmail(@Email(message = "邮箱格式异常") String email, @Length(message = "验证码长度为6位", max = 6) String verifyCode, @Length(message = "密码最小6位，最大为20位", max = 20, min = 6) String password) {
        return userService.registered(email, verifyCode, password);
    }

    @GetMapping("/check-email")
    @ResponseBody
    @ApiOperation("检查邮箱是否被注册")
    public BaseDaoForHttp checkEmail(@Email(message = "邮箱格式异常") String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/get-email-verify-code")
    @ResponseBody
    @ApiOperation("邮箱获取验证码")
    public BaseDaoForHttp getVerifyCode(@Email(message = "邮箱格式异常") String email) {
        return userService.email(email);
    }

    @GetMapping("/check-email-verifyCode")
    @ResponseBody
    @ApiOperation("检查邮箱验证码")
    public BaseDaoForHttp checkEmailVerifyCode(@Email(message = "邮箱格式异常") String email, @Length(message = "验证码长度为6位", max = 6) String verifyCode) {
        return userService.checkEmailVerifyCode(email, verifyCode);
    }

    @GetMapping("/user-email-login")
    @ResponseBody
    @ApiOperation("用户邮箱登录")
    public BaseDaoForHttp<UserLogin> userEmailLogin(@Email(message = "邮箱格式异常") String email, @Length(message = "密码最小6位，最大为20位", max = 20, min = 6) String passWord) {
        return userService.userEmailLogin(email, passWord);
    }

    @GetMapping("/user-verifyCode-login")
    @ResponseBody
    @ApiOperation("用户邮箱+验证码登录")
    public BaseDaoForHttp<UserLogin> userEmailVerifyCodeLogin(@Email(message = "邮箱格式异常") String email, @Length(message = "验证码长度为6位", max = 6) String verifyCode) {
        return userService.userEmailVerifyCodeLogin(email, verifyCode);
    }

    @PostMapping("/user-update")
    @ResponseBody
    @ApiOperation("用户信息维护")
    public BaseDaoForHttp userUpdate(@RequestBody UserUpdate user) {
        return userService.userUpdate(user);
    }

    @PostMapping("/user-update-password")
    @ResponseBody
    @ApiOperation("用户修改密码")
    public BaseDaoForHttp userUpdatePassword(@RequestBody UserUpdatePasswordDao user) {
        return userService.userUpdatePassword(user);
    }

    @PostMapping("/get-user-by-userId")
    @ResponseBody
    @ApiOperation("用户信息查询")
    public BaseDaoForHttp<User> getUserByUserId(String userId) {
        return userService.getUserByUserId(userId);
    }

    @GetMapping("/user-detail")
    @ResponseBody
    @ApiOperation("用户展示信息（包含基本信息-主页信息-会员到期时间）")
    public BaseDaoForHttp<UserLinkBase> userDetail(String userId) {
        return userService.userDetail(userId);
    }

    @GetMapping("/user-wx-login")
    @ResponseBody
    @ApiOperation("用户微信登录")
    public BaseDaoForHttp<UserLogin> userWxLogin(String openId, String wxNickName, String headUrl) {
        return userService.userWxLogin(openId, wxNickName, headUrl);
    }
}
