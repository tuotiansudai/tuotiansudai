package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Component
public class OssWrapperClient {

    static Logger logger = Logger.getLogger(OssWrapperClient.class);

    // 文件允许格式
    private static final List<String> ALLOW_SUFFIXES = Lists.newArrayList(".rar", ".doc", ".docx", ".zip", ".pdf",
            ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp");

    /**
     * 阿里云ACCESS_KEYID
     */
    @Value("${plat.oss.access_keyid}")
    private String accessKeyId;
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    @Value("${plat.oss.access_keysecret}")
    private String accessKeySecret;
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    @Value("${plat.oss.oss_endpoint}")
    private String ossEndpoint;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${plat.oss.bucket_name}")
    private String bucketName;

    @Value("${plat.sitePath}")
    private String sitePath;

    public OSSClient getOSSClient() {
        return new OSSClient(ossEndpoint, accessKeyId, accessKeySecret);
    }

    public String upload(String suffix, InputStream inputStream, String rootPath) throws Exception {
        if (!isAllowedFileExtName(suffix)) {
            throw new Exception("不允许的文件格式");
        }
        String newFileName = generateRandomFileName(suffix);
        return uploadFileBlur(newFileName, inputStream, rootPath);
    }

    private boolean isAllowedFileExtName(final String suffix) {
        return Iterators.any(ALLOW_SUFFIXES.iterator(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.endsWith(suffix.toLowerCase());
            }
        });
    }

    private String generateRandomFileName(String suffix) {
        Random random = new Random();
        return MessageFormat.format("{0}{1}.{2}", String.valueOf(random.nextInt(10000)), String.valueOf(System.currentTimeMillis()), suffix);
    }


    private String uploadFileBlur(String fileName, InputStream inputStream, String rootPath) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        String waterPath = rootPath + "images" + File.separator + "watermark.png";
        ByteArrayInputStream in = new ByteArrayInputStream(pressImage(waterPath, inputStream).toByteArray());
        try {
            objectMeta.setContentLength(in.available());
            objectMeta.setContentType("image/jpeg");
            String sitePath = this.sitePath + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator;
            String filePath = sitePath + new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date()) + File.separator + FilenameUtils.getExtension(fileName);
            OSSClient client = getOSSClient();
            client.putObject(bucketName, fileName, in, objectMeta);
            return filePath;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    private static ByteArrayOutputStream pressImage(String waterImg, InputStream inStream) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            //目标文件
            Image srcTarget = ImageIO.read(inStream);
            int width = srcTarget.getWidth(null);
            int height = srcTarget.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(srcTarget, 0, 0, width, height, null);
            //水印文件
            File waterFile = new File(waterImg);
            Image waterImage = ImageIO.read(waterFile);
            graphics.drawImage(waterImage, 0, 0, width, height, null);
            //水印文件结束
            graphics.dispose();
            ImageIO.write(image, "jpg", swapStream);
        } catch (Exception e) {
            logger.error("upload oss fail");
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                swapStream.close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return swapStream;
    }
}
