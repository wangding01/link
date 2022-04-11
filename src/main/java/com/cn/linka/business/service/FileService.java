package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileUploadDao;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    BaseDaoForHttp<FileUploadDao> upload(MultipartFile file,String userId);
}
