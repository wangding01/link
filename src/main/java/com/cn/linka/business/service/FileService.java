package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileUploadDao;
import com.cn.linka.business.dao.ThemeDao;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    BaseDaoForHttp<FileUploadDao> upload(MultipartFile file,String userId);
}
