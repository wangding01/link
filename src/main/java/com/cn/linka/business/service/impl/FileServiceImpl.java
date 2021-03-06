package com.cn.linka.business.service.impl;


import com.cn.linka.business.dao.BaseDaoForHttp;
import com.cn.linka.business.dao.FileDownLoadDao;
import com.cn.linka.business.dao.FileUploadDao;
import com.cn.linka.business.service.FileService;
import com.cn.linka.common.config.COSConfig;
import com.cn.linka.common.config.SnowFlake;
import com.cn.linka.common.exception.BusException;
import com.cn.linka.common.exception.BusinessExceptionEnum;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    @Value("${link.fileStorePath}")
    private String filePath;
    @Autowired
    private COSConfig cosConfig;
    @Resource(name = "transferManager")
    private TransferManager transferManager;
    @Resource(name = "cosClient")
    private COSClient cosClient;

    @Override
    public BaseDaoForHttp<FileUploadDao> upload(MultipartFile uploadFile, String userId) {
        long currentTimeMillis = System.currentTimeMillis();
        String tempPath = filePath;
        File file = new File(tempPath);
        // Temp?????????????????????
        if (!file.exists()) {
            file.mkdir();
        }
        String path = tempPath + currentTimeMillis + "-" + uploadFile.getOriginalFilename();
        file = new File(path);
        try {
            // ????????????
            uploadFile.transferTo(file);
            String url = "/linkPath/" + currentTimeMillis + "-" + uploadFile.getOriginalFilename();
            return BaseDaoForHttp.success(FileUploadDao.builder().loadUrl(url).build());
        } catch (IOException e) {
            return BaseDaoForHttp.fail(7007, "??????????????????");
        }
    }

    @Override
    public BaseDaoForHttp<FileUploadDao> uploadToCos(MultipartFile file, String userId) {
        int imageSize = Integer.parseInt(cosConfig.getImageSize());
        int maxSize = imageSize << 20;
        if (file.getSize() > maxSize) {
            throw new BusException("??????????????????????????????" + imageSize + "M???");
        }
        //?????????????????????
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        Date dd = cale.getTime();
        String month = sdf.format(dd);
        String folderName = "link" + "/" + year + "/" + month + "/";
        //????????????
        String originalFilename = file.getOriginalFilename();
        //????????????????????????(?????????0-9999+??????????????????+???????????????)
        String key = userId + "-" + System.currentTimeMillis() + "-" + originalFilename;
        //???????????????
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), folderName + key, transferToFile(file));
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            return BaseDaoForHttp.success(FileUploadDao.builder().loadUrl(uploadResult.getKey())
                    .cosBaseUrl(cosConfig.getBaseUrl())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw  new  BusException(BusinessExceptionEnum.FILE_UPLOAD_FAIL);
        }

    }

    @Override
    public BaseDaoForHttp<FileDownLoadDao> downLoadByCos(String key) {
        COSObjectInputStream cosObjectInput = null;
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(cosConfig.getBucketName(), key);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            cosObjectInput = cosObject.getObjectContent();
            // ??????????????? CRC64
            String crc64Ecma = cosObject.getObjectMetadata().getCrc64Ecma();
            BaseDaoForHttp.success(FileDownLoadDao.builder().downLoadStr(crc64Ecma).build());
        }catch (Exception e){
            e.printStackTrace();
                throw  new  BusException(BusinessExceptionEnum.FILE_DOWNLOAD_FAIL);
        }finally {
            try {
                cosObjectInput.close();
            }catch (Exception e){
                log.error("???????????????");
            }
        }

// ???????????????

        return null;
    }

    private File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String prefix = originalFilename.split("\\.")[0];
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        return file;
    }
}
