package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.ThemeDao;
import com.cn.linka.business.service.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: linka
 * @description: 用户主题查询
 * @author: wangding
 * @create: 2022-04-11 16:09
 */
@RequestMapping("/user/theme")
@RestController
@Api(value = "用户主题查询controller", tags = {"用户主题查询接口"})
public class ThemeUserController {
    @Resource
    private ThemeService themeService;

    @GetMapping(value = "/get-theme-by-id")
    @ApiOperation("根据id查询主题信息")
    public BaseDaoForHttp<ThemeDao> getThemeById(Long id) {
        return themeService.getThemeById(id);
    }

}