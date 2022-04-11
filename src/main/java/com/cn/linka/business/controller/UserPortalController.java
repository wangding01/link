package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.UserPortalDao;
import com.cn.linka.business.dao.UserRegisteredDao;
import com.cn.linka.business.service.UserPortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: linka
 * @description: 用户主页
 * @author: wangding
 * @create: 2022-04-11 14:39
 */
@RestController
@Api(value = "用户主页controller", tags = {"用户主页接口"})
public class UserPortalController {
    @Resource
    private UserPortalService userPortalService;

    @PostMapping("/portal-insert")
    @ApiOperation("主页新增，allMsg是kv的json")
    public BaseDaoForHttp portalInsert(UserPortalDao userPortalDao) {
        return userPortalService.portalInsert(userPortalDao);
    }
    @GetMapping("/get-portal-by-userId")
    @ApiOperation("userId查询主页信息")
    public BaseDaoForHttp<UserPortalDao> getPortalByUserId(String userId) {
        return userPortalService.getPortalByUserId(userId);
    }

    @RequestMapping(value = "/get-portal-by-index",method = RequestMethod.GET)
    @ApiOperation("index查询-展示主页信息")
    public BaseDaoForHttp<UserPortalDao> getPortalByUserIndex( String index) {
        return userPortalService.getPortalByIndex(index);
    }
}