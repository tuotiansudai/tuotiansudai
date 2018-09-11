package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.collect.Lists;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class OssWrapperClient {

    private static Logger logger = Logger.getLogger(OssWrapperClient.class);

    // 文件允许格式
    private static final List<String> ALLOW_SUFFIXES = Lists.newArrayList(".rar", ".doc", ".docx", ".zip", ".pdf", ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp");

    private final static String bucketName;

    private final static OSSClient ossClient;

    @Value("${common.oss.upload.folder}")
    private String sitePath;

    private final ResourceLoader resourceLoader;


    static {
        bucketName = ETCDConfigReader.getReader().getValue("common.oss.bucket");
        ossClient = new OSSClient(ETCDConfigReader.getReader().getValue("common.oss.endpoint"),
                ETCDConfigReader.getReader().getValue("common.oss.id"),
                ETCDConfigReader.getReader().getValue("common.oss.secret"));
    }

    @Autowired
    public OssWrapperClient(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String upload(String suffix, InputStream inputStream, boolean waterImage) throws Exception {
        if (!isAllowedFileExtName(suffix)) {
            throw new Exception("不允许的文件格式");
        }
        String newFileName = this.generateRandomFileName(suffix);
        logger.info("[OSS UPLOAD] newFileName:" + newFileName);

        ByteArrayOutputStream byteArrayOutputStream = pressImage(inputStream, waterImage);
        if (byteArrayOutputStream == null) {
            return null;
        }

        return uploadFileBlur(newFileName, byteArrayOutputStream.toByteArray());
    }

    private boolean isAllowedFileExtName(final String suffix) {
        return ALLOW_SUFFIXES.stream().anyMatch(allowSuffix -> allowSuffix.endsWith(suffix.toLowerCase()));
    }

    private String generateRandomFileName(String suffix) {
        return MessageFormat.format("{0}-{1}.{2}", new DateTime().toString("yyyyMMdd"), String.valueOf(System.currentTimeMillis()), suffix);
    }

    private String uploadFileBlur(String fileName, byte[] bytes) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(in.available());
            objectMeta.setContentType("image/jpeg");
            String sitePath = this.sitePath + new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separator;
            String filePath = sitePath + fileName;

            logger.info(MessageFormat.format("{0}|{1}", "[OSS UPLOAD] filePath:", filePath));
            ossClient.putObject(bucketName, fileName, in, objectMeta);
            return filePath;
        } catch (Exception e) {
            logger.error(MessageFormat.format("{0}|{1}", "[OSS UPLOAD]", e.getLocalizedMessage()), e);
        }
        return null;
    }

    private ByteArrayOutputStream pressImage(InputStream inStream, boolean water) {
        try (ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(swapStream)) {
            //目标文件
            Image srcTarget = ImageIO.read(inStream);
            int width = srcTarget.getWidth(null);
            int height = srcTarget.getHeight(null);
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
            imageWriter.setOutput(ios);
            JPEGImageWriteParam jpegParams = (JPEGImageWriteParam) imageWriter.getDefaultWriteParam();
            jpegParams.setCompressionMode(JPEGImageWriteParam.MODE_EXPLICIT);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(srcTarget, 0, 0, width, height, null);

            if (!water) {
                jpegParams.setCompressionQuality(1.0f);
                imageWriter.write(null, new IIOImage(image, null, null), jpegParams);
                imageWriter.dispose();
                return swapStream;
            }

            //水印文件
            Image waterImage = ImageIO.read(this.resourceLoader.getResource("classpath:watermark.png").getFile());
            graphics.drawImage(waterImage, 0, 0, width, height, null);
            //水印文件结束
            graphics.dispose();

            jpegParams.setCompressionQuality(0.35f);

            IIOMetadata data = imageWriter.getDefaultImageMetadata(new ImageTypeSpecifier(image), jpegParams);
            IIOMetadataNode tree = (IIOMetadataNode) data.getAsTree("javax_imageio_jpeg_image_1.0");
            IIOMetadataNode jfif = (IIOMetadataNode) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", Integer.toString(96));
            jfif.setAttribute("Ydensity", Integer.toString(96));
            jfif.setAttribute("resUnits", "1");
            data.mergeTree(data.getNativeMetadataFormatName(), tree);

            imageWriter.write(null, new IIOImage(image, null, data), jpegParams);
            imageWriter.dispose();
            return swapStream;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }
}
