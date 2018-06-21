package com.tuotiansudai.fudian.util;

import com.aliyun.oss.OSSClient;
import com.tuotiansudai.fudian.config.OssConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;

@Component
public class OssClient {

    private static Logger logger = LoggerFactory.getLogger(OssClient.class);

    private final OssConfig ossConfig;

    private static OSSClient ossClient;

    @Autowired
    public OssClient(OssConfig ossConfig){
        this.ossConfig = ossConfig;
        ossClient = new OSSClient(this.ossConfig.getOssEndpoint(), this.ossConfig.getAccessKeyId(), this.ossConfig.getAccessKeySecret());
    }

    public void upload(String fileName, InputStream inputStream){
        ossClient.putObject(ossConfig.getBucketName(), fileName, inputStream);
    }
}
