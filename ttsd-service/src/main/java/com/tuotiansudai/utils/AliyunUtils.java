package com.tuotiansudai.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/20.
 */
public class AliyunUtils {

    static Log log = LogFactory.getLog(AliyunUtils.class);
    /**
     * 阿里云ACCESS_KEYID
     */
    private static String ACCESS_KEYID = getOssConfig("plat.oss.access_keyid");
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    private	 static String ACCESS_KEYSECRET = getOssConfig("plat.oss.access_keysecret");
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    private static String OSS_ENDPOINT = getOssConfig("plat.oss.oss_endpoint");

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    private static String BUCKET_NAME = getOssConfig("plat.oss.bucket_name");

    public static String getOssConfig(String key){
        return PropertiesUtils.getPro(key);
    }

    public static OSSClient getOSSClient(){
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);
        return client;
    }

    /**
     * UE上传文件
     *
     * objectkey 上传到OSS起的名
     * @param fileName  本地文件名
     * @throws OSSException
     * @throws ClientException
     * @throws java.io.FileNotFoundException
     */
    public static String uploadFileBlur(String fileName ,InputStream inputStream ,String rootPath)
            throws OSSException, ClientException, FileNotFoundException,IOException {
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        String waterPath = rootPath + "/site/themes/default/images/watermark.png";
        ByteArrayInputStream in = new ByteArrayInputStream(WaterMarkUtils.pressImage(waterPath,inputStream,0,0).toByteArray());
        objectMeta.setContentLength(in.available());
        objectMeta.setContentType("image/jpeg");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        fileName = sdf.format(new Date()) + getFileExt(fileName);
        String filePath = sitePath+ fileName;
        PutObjectResult result = client.putObject(BUCKET_NAME, fileName, in, objectMeta);
        log.info("result etag :" + result.getETag() + "filepath:" + filePath);
        return filePath;
    }

    public static String uploadFile(String fileName ,InputStream input )
            throws OSSException, ClientException, FileNotFoundException ,IOException{
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(input.available());
        objectMeta.setContentType("image/jpeg");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        fileName = sdf.format(new Date()) + getFileExt(fileName);
        String filePath = sitePath+ fileName;
        PutObjectResult result = client.putObject(BUCKET_NAME, fileName, input, objectMeta);
        log.debug("result etag :" + result.getETag() + "filepath:" + filePath);
        return filePath;
    }

    private static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
