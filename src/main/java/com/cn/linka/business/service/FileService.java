package com.cn.linka.business.service;

import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileDownLoadDao;
import com.cn.linka.business.dao.FileUploadDao;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    BaseDaoForHttp<FileUploadDao> upload(MultipartFile file,String userId);

    BaseDaoForHttp<FileUploadDao> uploadToCos(MultipartFile file,String userId);

    BaseDaoForHttp<FileDownLoadDao> downLoadByCos(String key);
}

