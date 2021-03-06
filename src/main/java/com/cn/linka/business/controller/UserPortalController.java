package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.GetPortalByIndexRequest;
import com.cn.linka.business.dao.UserPortalDao;
import com.cn.linka.business.service.UserPortalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern;

/**
 * @program: linka
 * @description: 用户主页
 * @author: wangding
 * @create: 2022-04-11 14:39
 */
@Validated
@RestController
@Api(value = "用户主页controller", tags = {"用户主页接口"})
public class UserPortalController {
    @Resource
    private UserPortalService userPortalService;

    @PostMapping("/portal-insert")
    @ApiOperation("主页新增")
    public BaseDaoForHttp portalInsert(@RequestBody @Validated UserPortalDao userPortalDao) {
        return userPortalService.portalInsert(userPortalDao);
    }
    @PostMapping("/portal-update")
    @ApiOperation("主页修改-修改信息是全量送给服务端")
    public BaseDaoForHttp portalUpdate(@RequestBody @Validated UserPortalDao userPortalDao) {
        return userPortalService.portalUpdate(userPortalDao);
    }
    @GetMapping("/get-portal-by-userId")
    @ApiOperation("userId查询主页信息")
    public BaseDaoForHttp<UserPortalDao> getPortalByUserId(String userId) {
        return userPortalService.getPortalByUserId(userId);
    }
    @PostMapping("/get-portal-by-index")
    @ApiOperation("index查询-展示主页信息-外部用户访问")
    public BaseDaoForHttp<UserPortalDao> getPortalByUserIndex(@RequestBody GetPortalByIndexRequest request) {
        return userPortalService.getPortalByIndex(request.getIndex());
    }
    @GetMapping("/check-index-exist")
    @ApiOperation("检查index是否被使用")
    public BaseDaoForHttp checkIndexExist(@Pattern(regexp = "^[0-9a-zA-Z_]{1,20}$",message = "链接非法")String index) {
        return userPortalService.checkIndexExist(index);
    }
}