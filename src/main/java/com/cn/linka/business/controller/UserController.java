package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.User;
import com.cn.linka.business.dao.UserRegisteredDao;
import com.cn.linka.business.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试第一个controller
 */
@Controller
@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/getUser")
    @ResponseBody
    public BaseDaoForHttp<List<User>>  getUser() {
        return BaseDaoForHttp.success(userService.queryUserList());
    }

    @PostMapping("/registered-email")
    @ResponseBody
    @ApiOperation("邮箱注册")
    public BaseDaoForHttp<UserRegisteredDao> registeredForEmail(String email, String verifyCode, String password) {
        return userService.registered(email,verifyCode,password);
    }
    @GetMapping("/check-email")
    @ResponseBody
    @ApiOperation("检查邮箱是否被注册")
    public BaseDaoForHttp checkEmail(String email) {
        return userService.checkEmail(email);
    }
    @GetMapping("/get-verify-code")
    @ResponseBody
    @ApiOperation("邮箱获取验证码")
    public BaseDaoForHttp getVerifyCode(String email) {
        return userService.email(email);
    }

}
