package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.Environment;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
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
    @Value("${common.oss.id}")
    private String accessKeyId;
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    @Value("${common.oss.secret}")
    private String accessKeySecret;
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    @Value("${common.oss.endpoint}")
    private String ossEndpoint;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${common.oss.bucket}")
    private String bucketName;

    @Value("${common.oss.upload.folder}")
    private String sitePath;

    @Value("${common.environment}")
    private Environment environment;

    public OSSClient getOSSClient() {
        return new OSSClient(ossEndpoint, accessKeyId, accessKeySecret);
    }

    public String upload(String suffix, InputStream inputStream, String rootPath, String address, boolean waterImage) throws Exception {
        if (!isAllowedFileExtName(suffix)) {
            throw new Exception("不允许的文件格式");
        }
        String newFileName = generateRandomFileName(suffix);
        return uploadFileBlur(newFileName, inputStream, rootPath, address, waterImage);
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

    private String mkdir(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return path;
    }

    private String uploadFileBlur(String fileName, InputStream inputStream, String rootPath, String address, boolean waterImage) {
        ObjectMetadata objectMeta = new ObjectMetadata();
        String waterPath = rootPath + "images" + File.separator + "watermark.png";
        ByteArrayInputStream in = new ByteArrayInputStream(pressImage(waterPath, inputStream, waterImage).toByteArray());
        try {
            objectMeta.setContentLength(in.available());
            objectMeta.setContentType("image/jpeg");
            String sitePath = this.sitePath + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator;
            String filePath = sitePath + fileName;
            if (Environment.DEV == environment) {
                String savefile = mkdir(rootPath + sitePath) + fileName;
                FileOutputStream out = new FileOutputStream(new File(savefile));
                BufferedOutputStream output = new BufferedOutputStream(out);
                Streams.copy(in, output, true);
                return address + filePath;
            } else {
                OSSClient client = getOSSClient();
                client.putObject(bucketName, fileName, in, objectMeta);
                return filePath;
            }
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

    private static ByteArrayOutputStream pressImage(String waterImg, InputStream inStream, boolean water) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        ImageOutputStream ios = null;
        try {
            //目标文件
            Image srcTarget = ImageIO.read(inStream);
            int width = srcTarget.getWidth(null);
            int height = srcTarget.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(srcTarget, 0, 0, width, height, null);
            if (water) {
                //水印文件
                File waterFile = new File(waterImg);
                Image waterImage = ImageIO.read(waterFile);
                graphics.drawImage(waterImage, 0, 0, width, height, null);
                //水印文件结束
            }
            graphics.dispose();
            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
            ios = ImageIO.createImageOutputStream(swapStream);
            imageWriter.setOutput(ios);

            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.35f);

            IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), jpegParams);
            IIOMetadataNode tree = (IIOMetadataNode)data.getAsTree("javax_imageio_jpeg_image_1.0");
            IIOMetadataNode jfif = (IIOMetadataNode)tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(96));
            jfif.setAttribute("Ydensity", Integer.toString(96));
            jfif.setAttribute("resUnits", "1");

            data.mergeTree(data.getNativeMetadataFormatName(), tree);

            imageWriter.write(null, new IIOImage(image, null, data), jpegParams);
            imageWriter.dispose();
        } catch (Exception e) {
            logger.error("upload oss fail");
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            try {
                swapStream.close();
                ios.close();
            } catch (IOException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return swapStream;
    }

}
