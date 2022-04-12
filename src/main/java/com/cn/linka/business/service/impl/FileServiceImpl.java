package com.cn.linka.business.service.impl;


import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileUploadDao;
import com.cn.linka.business.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Value("${link.fileStorePath}")
    private String filePath;

    @Override
    public BaseDaoForHttp<FileUploadDao> upload(MultipartFile uploadFile, String userId) {
        long currentTimeMillis = System.currentTimeMillis();
        String tempPath = filePath;
        File file = new File(tempPath);
        // Temp文件夹是否存在
        if (!file.exists()) {
            file.mkdir();
        }
        String path = tempPath + currentTimeMillis +"-"+ uploadFile.getOriginalFilename();
        file = new File(path);
        try {
            // 保存文件
            uploadFile.transferTo(file);
            String url = "/linkPath/" + currentTimeMillis +"-"+ uploadFile.getOriginalFilename();
            return BaseDaoForHttp.success(FileUploadDao.builder().loadUrl(url).build());
        } catch (IOException e) {
            return BaseDaoForHttp.fail(7007, "文件上传异常");
        }
    }
}
