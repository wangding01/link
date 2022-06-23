package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.BaseDaoForHttpByPageNo;
import com.cn.linka.business.dao.ThemeDao;
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
@RequestMapping("/admin/theme")
@RestController
@Api(value = "主题controller", tags = {"管理后台主题接口"})
public class ThemeController {
    @Resource
    private ThemeService themeService;

    @GetMapping(value = "/get-all-theme")
    @ApiOperation("查询所有的主题")
    public BaseDaoForHttp<List<ThemeDao>> getAllTheme() {
        return themeService.getAllTheme();
    }

    @GetMapping(value = "/get-all-theme-page")
    @ApiOperation("查询所有的主题-分页")
    public BaseDaoForHttpByPageNo<List<ThemeDao>> getAllThemePage(int pageSize, int offset) {
        return themeService.getAllThemePage(pageSize,offset);
    }

    @PostMapping("/insert")
    @ApiOperation("主题新增")
    public BaseDaoForHttp insert(@RequestBody ThemeDao themeDao) {
        return themeService.insert(themeDao);
    }

    @PostMapping("/update")
    @ApiOperation("主题更新")
    public BaseDaoForHttp update(@RequestBody ThemeDao themeDao) {
        return themeService.update(themeDao);
    }
}