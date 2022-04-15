package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.MemberMenuDao;
import com.cn.linka.business.dao.ThemeDao;
import com.cn.linka.business.service.MemberMenuService;
import com.cn.linka.business.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: linka
 * @description: 文件处理
 * @author: wangding
 * @create: 2022-04-11 16:09
 */
@RequestMapping("/admin/member")
@RestController
@Api(value = "会员付费菜单controller", tags = {"管理后台会员付费菜单接口"})
public class MemberMenuController {
    @Resource
    private MemberMenuService memberMenuService;

    @GetMapping(value = "/get-all")
    @ApiOperation("查询所有的会员付费菜单")
    public BaseDaoForHttp<List<MemberMenuDao>> getAllTheme() {
        return memberMenuService.getAll();
    }

    @PostMapping("/insert")
    @ApiOperation("会员付费菜单新增")
    public BaseDaoForHttp insert(@RequestBody MemberMenuDao memberMenuDao) {
        return memberMenuService.insert(memberMenuDao);
    }

    @PostMapping("/update")
    @ApiOperation("会员付费菜单更新")
    public BaseDaoForHttp update(@RequestBody MemberMenuDao memberMenuDao) {
        return memberMenuService.update(memberMenuDao);
    }
}