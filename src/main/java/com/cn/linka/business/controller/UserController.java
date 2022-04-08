package com.cn.linka.business.controller;

import com.cn.linka.business.dao.User;
import com.cn.linka.business.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试第一个controller
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/getUser")
    @ResponseBody
    List<User> getUser() {
        return userService.queryUserList();
    }
}
