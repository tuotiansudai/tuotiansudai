package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;

@Component
public class OssApkWrapperClient {

    static Logger logger = Logger.getLogger(OssApkWrapperClient.class);

    // 文件允许格式
    private static final List<String> ALLOW_SUFFIXES = Lists.newArrayList(".json", ".apk");

    /**
     * 阿里云ACCESS_KEYID
     */
    @Value("${app.oss.id}")
    private String accessKeyId;
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    @Value("${app.oss.secret}")
    private String accessKeySecret;
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    @Value("${app.oss.endpoint}")
    private String ossEndpoint;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${app.oss.bucket}")
    private String bucketName;


    @Value("${common.environment}")
    private String environment;

    public OSSClient getOSSClient() {
        return new OSSClient(ossEndpoint, accessKeyId, accessKeySecret);
    }

    public String upload(String fileName, InputStream inputStream) throws Exception {
        if (!isAllowedFileExtName(FilenameUtils.getExtension(fileName))) {
            throw new Exception("不允许的文件格式");
        }
        logger.info("[OSS UPLOAD] FileName:" + fileName);
        return uploadFileBlur(fileName, inputStream);
    }

    private boolean isAllowedFileExtName(final String suffix) {
        return Iterators.any(ALLOW_SUFFIXES.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.endsWith(suffix.toLowerCase());
            }
        });
    }


    private String uploadFileBlur(String fileName, InputStream inputStream) {
        try {
            logger.info(MessageFormat.format("{0}|{1}", "[OSS UPLOAD] filePath:", fileName));
            OSSClient client = getOSSClient();
            client.putObject(bucketName, fileName, inputStream);
            return fileName;
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}|{1}", "[OSS UPLOAD]", e.getLocalizedMessage()), e);
        }
        return null;
    }

}
