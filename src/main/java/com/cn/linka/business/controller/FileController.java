package com.cn.linka.business.controller;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileUploadDao;
import com.cn.linka.business.service.FileService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: linka
 * @description: 文件处理
 * @author: wangding
 * @create: 2022-04-11 16:09
 */
@RestController
@Api(value = "文件处理controller", tags = {"用户文件处理接口"})
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping(value = "/upload")
    public BaseDaoForHttp<FileUploadDao> upload(@ApiParam(value = "上传的文件", required = true) @RequestPart("file") MultipartFile file, @RequestParam("userId") String userId) {
        return fileService.upload(file, userId);
    }

}