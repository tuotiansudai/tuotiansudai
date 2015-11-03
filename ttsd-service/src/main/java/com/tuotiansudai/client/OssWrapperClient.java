package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

@Component
public class OssWrapperClient {

    static Logger logger = Logger.getLogger(OssWrapperClient.class);

    // 文件允许格式
    private static final String[] allowFiles = {".rar", ".doc", ".docx", ".zip", ".pdf",
            ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp"};

    /**
     * 阿里云ACCESS_KEYID
     */
    @Value("${plat.oss.access_keyid}")
    private String ACCESS_KEYID;
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    @Value("${plat.oss.access_keysecret}")
    private String ACCESS_KEYSECRET;
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    @Value("${plat.oss.oss_endpoint}")
    private String OSS_ENDPOINT;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${plat.oss.bucket_name}")
    private String BUCKET_NAME;

    @Value("${plat.sitePath}")
    private String SITEPATH;

    public OSSClient getOSSClient() {
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);
        return client;
    }

    public String upload(String fileExtName, InputStream inputStream, String rootPath) throws Exception {
        if (!isAllowedFileExtName(fileExtName)) {
            throw new Exception("不允许的文件格式");
        }
        String newFileName = generateRandomFileName(fileExtName);
        String absoluteUrl = uploadFileBlur(newFileName, inputStream, rootPath);
        return absoluteUrl;
    }

    private boolean isAllowedFileExtName(String fileName) {
        Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    private String generateRandomFileName(String fileExtName) {
        Random random = new Random();
        return String.format("{0}{1}.{2}",
                random.nextInt(10000),
                System.currentTimeMillis(),
                fileExtName);
    }


    private String uploadFileBlur(String fileName, InputStream inputStream, String rootPath) {
        ByteArrayInputStream in = null;
        String filePath = "";
        try {
            ObjectMetadata objectMeta = new ObjectMetadata();
            String waterPath = rootPath + "images" + File.separator + "watermark.png";
            in = new ByteArrayInputStream(pressImage(waterPath, inputStream).toByteArray());
            objectMeta.setContentLength(in.available());
            objectMeta.setContentType("image/jpeg");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String sitePath = SITEPATH + format.format(new Date()) + File.separator;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
            fileName = sdf.format(new Date()) + "." + FilenameUtils.getExtension(fileName);
            filePath = sitePath + fileName;
            OSSClient client = getOSSClient();
            PutObjectResult result = client.putObject(BUCKET_NAME, fileName, in, objectMeta);
            logger.info("result etag :" + result.getETag() + "filepath:" + filePath);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return filePath;
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
            logger.error("upload oss fail ");
            e.printStackTrace();
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
