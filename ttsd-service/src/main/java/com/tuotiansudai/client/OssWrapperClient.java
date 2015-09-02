package com.tuotiansudai.client;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/8/20.
 */
@Component
public class OssWrapperClient{

    static Logger logger = Logger.getLogger(OssWrapperClient.class);

    // 输出文件地址
    private String url = "";
    // 上传文件名
    private String fileName = "";
    // 状态
    private String state = "";
    // 文件类型
    private String type = "";
    // 原始文件名
    private String originalName = "";
    // 文件大小
    private String size = "";

    private String title = "";

    // 保存路径
    private String savePath = "upload";
    // 文件允许格式
    private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf",
            ".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
    // 文件大小限制，单位KB
    private int maxSize = 1024*10;

    private HashMap<String, String> errorInfo = new HashMap<String, String>();

    public OssWrapperClient() {
        HashMap<String, String> tmp = this.errorInfo;
        tmp.put("SUCCESS", "SUCCESS");
        tmp.put("NOFILE", "未包含文件上传域");
        tmp.put("TYPE", "不允许的文件格式");
        tmp.put("SIZE", "文件大小超出限制");
        tmp.put("ENTYPE", "请求类型ENTYPE错误");
        tmp.put("REQUEST", "上传请求异常");
        tmp.put("IO", "IO异常");
        tmp.put("DIR", "目录创建失败");
        tmp.put("UNKNOWN", "未知错误");
    }

    /**
     * 阿里云ACCESS_KEYID
     */
    @Value("${plat.oss.access_keyid}")
    private static String ACCESS_KEYID;
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    @Value("${plat.oss.access_keysecret}")
    private static String ACCESS_KEYSECRET;
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    @Value("${plat.oss.oss_endpoint}")
    private static String OSS_ENDPOINT;

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    @Value("${plat.oss.bucket_name}")
    private String BUCKET_NAME;

    @Value("${plat.sitePath}")
    private  String SITEPATH;

    private static OSSClient getOSSClient(){
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);
        return client;
    }

    public void upload(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            this.state = this.errorInfo.get("NOFILE");
            return;
        }
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        MultipartFile dfi = multiRequest.getFile("upfile");
        this.originalName = dfi.getOriginalFilename().substring(dfi.getOriginalFilename().lastIndexOf(System.getProperty("file.separator")) + 1);
        if (!this.checkFileType(this.originalName)) {
            this.state = this.errorInfo.get("TYPE");
            return;
        }
        this.fileName = this.getName(this.originalName);
        this.type = FilenameUtils.getExtension(this.fileName);
        this.url = savePath + File.separator + this.fileName;
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        this.url = uploadFileBlur(fileName, dfi.getInputStream(), rootPath);
        this.title = url;
        this.state = this.errorInfo.get("SUCCESS");
    }

    /**
     * 文件类型判断
     *
     * @param fileName
     * @return
     */
    private boolean checkFileType(String fileName) {
        Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
        while (type.hasNext()) {
            String ext = type.next();
            if (fileName.toLowerCase().endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return
     */
    private String getName(String fileName) {
        Random random = new Random();
        return this.fileName = "" + random.nextInt(10000)
                + System.currentTimeMillis() + FilenameUtils.getExtension(fileName);
    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     *
     * @param path
     * @return
     */
    private String getFolder(HttpServletRequest request, String path) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path += File.separator + formater.format(new Date());
        File dir = new File(this.getPhysicalPath(request, path));
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                this.state = this.errorInfo.get("DIR");
                return "";
            }
        }
        return path;
    }

    private String uploadFileBlur(String fileName ,InputStream inputStream ,String rootPath) {
        ByteArrayInputStream in = null;
        String filePath = "";
        try {
            ObjectMetadata objectMeta = new ObjectMetadata();
            String waterPath = rootPath + File.separator + "images" + File.separator + "watermark.png";
            in = new ByteArrayInputStream(pressImage(waterPath, inputStream, 0, 0).toByteArray());
            objectMeta.setContentLength(in.available());
            objectMeta.setContentType("image/jpeg");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String sitePath = SITEPATH + format.format(new Date()) + File.separator;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
            fileName = sdf.format(new Date()) + FilenameUtils.getExtension(fileName);
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

    private static ByteArrayOutputStream pressImage(String waterImg, InputStream inStream ,int x, int y) {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        try {
            //目标文件
            Image srcTarget = ImageIO.read(inStream);
            int width = srcTarget.getWidth(null);
            int height = srcTarget.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(srcTarget, 0, 0, width, height, null);
            //水印文件
            File waterFile = new File(waterImg);
            Image waterImage = ImageIO.read(waterFile);
            graphics.drawImage(waterImage, 0,0, width, height, null);
            //水印文件结束
            graphics.dispose();
            byte[] byteBuffer = new byte[100]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inStream.read(byteBuffer, 0, 100)) > 0) {
                swapStream.write(byteBuffer, 0, rc);
            }
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(swapStream);
            encoder.encode(image);
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

    /**
     * 根据传入的虚拟路径获取物理路径
     *
     * @param path
     * @return
     */
    private String getPhysicalPath(HttpServletRequest request, String path) {
        String servletPath = request.getServletPath();
        String realPath = request.getSession().getServletContext().getRealPath("") + servletPath;
        return new File(realPath).getParent() + File.separator + path;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void setAllowFiles(String[] allowFiles) {
        this.allowFiles = allowFiles;
    }

    public void setMaxSize(int size) {
        this.maxSize = size;
    }

    public String getSize() {
        return this.size;
    }

    public String getUrl() {
        return this.url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getState() {
        return this.state;
    }

    public String getTitle() {
        return this.title;
    }

    public String getType() {
        return this.type;
    }

    public String getOriginalName() {
        return this.originalName;
    }

}
