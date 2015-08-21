package com.tuotiansudai.client;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;
import org.primefaces.webapp.MultipartRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    private OSSClient getOSSClient(){
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);
        return client;
    }

    public void upload(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            this.state = this.errorInfo.get("NOFILE");
            return;
        }
        if (request instanceof MultipartRequest) {
            Boolean switchBlur = request.getParameter("switchBlur")!=null;
            DiskFileItem dfi = (DiskFileItem) ((MultipartRequest) request).getFileItem("upfile");
            this.originalName = dfi.getName().substring(dfi.getName().lastIndexOf(System.getProperty("file.separator")) + 1);
            if (!this.checkFileType(this.originalName)) {
                this.state = this.errorInfo.get("TYPE");
                return;
            }
            this.fileName = this.getName(this.originalName);
            this.type = this.getFileExt(this.fileName);
            //add line mkdir
            this.url = savePath  + "/" + this.fileName;
            String rootPath = request.getSession().getServletContext().getRealPath("/");
            if(switchBlur){
                this.url = uploadFileBlur(fileName, dfi.getInputStream(), rootPath);
            }else{
                this.url = uploadFile(fileName, dfi.getInputStream());
            }
            this.title = url;
            this.state = this.errorInfo.get("SUCCESS");
        } else {
            DiskFileItemFactory dff = new DiskFileItemFactory();
            String savePath = this.getFolder(request, this.savePath);
            dff.setRepository(new File(savePath));
            try {
                ServletFileUpload sfu = new ServletFileUpload(dff);
                sfu.setSizeMax(this.maxSize * 1024);
                sfu.setHeaderEncoding("UTF-8");
                FileItemIterator fii = sfu.getItemIterator(request);
                while (fii.hasNext()) {
                    FileItemStream fis = fii.next();
                    if (!fis.isFormField()) {
                        this.originalName = fis
                                .getName()
                                .substring(
                                        fis.getName()
                                                .lastIndexOf(
                                                        System.getProperty("file.separator")) + 1);
                        if (!this.checkFileType(this.originalName)) {
                            this.state = this.errorInfo.get("TYPE");
                            continue;
                        }
                        this.fileName = this.getName(this.originalName);
                        this.type = this.getFileExt(this.fileName);
                        this.url = savePath + "/" + this.fileName;
                        BufferedInputStream in = new BufferedInputStream(
                                fis.openStream());
                        FileOutputStream out = new FileOutputStream(new File(
                                this.getPhysicalPath(request, this.url)));
                        BufferedOutputStream output = new BufferedOutputStream(
                                out);
                        Streams.copy(in, output, true);
                        this.state = this.errorInfo.get("SUCCESS");
                        // UE中只会处理单张上传，完成后即退出
                        break;
                    } else {
                        String fname = fis.getFieldName();
                        // 只处理title，其余表单请自行处理
                        if (!fname.equals("pictitle")) {
                            continue;
                        }
                        BufferedInputStream in = new BufferedInputStream(
                                fis.openStream());
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(in));
                        StringBuffer result = new StringBuffer();
                        while (reader.ready()) {
                            result.append((char) reader.read());
                        }
                        this.title = new String(result.toString().getBytes(),
                                "utf-8");
                        reader.close();
                    }
                }
            } catch (FileUploadBase.SizeLimitExceededException e) {
                this.state = this.errorInfo.get("SIZE");
            } catch (FileUploadBase.InvalidContentTypeException e) {
                this.state = this.errorInfo.get("ENTYPE");
            } catch (FileUploadException e) {
                this.state = this.errorInfo.get("REQUEST");
            } catch (Exception e) {
                this.state = this.errorInfo.get("UNKNOWN");
            }
        }
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
     * 获取文件扩展名
     *
     * @return string
     */
    private String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 依据原始文件名生成新文件名
     *
     * @return
     */
    private String getName(String fileName) {
        Random random = new Random();
        return this.fileName = "" + random.nextInt(10000)
                + System.currentTimeMillis() + this.getFileExt(fileName);
    }

    /**
     * 根据字符串创建本地目录 并按照日期建立子目录返回
     *
     * @param path
     * @return
     */
    private String getFolder(HttpServletRequest request, String path) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        path += "/" + formater.format(new Date());
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

    /**
     * add method by lance
     * mkdir
     * @param path
     */
    private String mkdir(final String path) {

        File dir = new File(path);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;
    }

    private String uploadFileBlur(String fileName ,InputStream inputStream ,String rootPath)
            throws OSSException, ClientException, FileNotFoundException,IOException {
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        String waterPath = rootPath + "/images/watermark.png";
        ByteArrayInputStream in = new ByteArrayInputStream(pressImage(waterPath,inputStream,0,0).toByteArray());
        objectMeta.setContentLength(in.available());
        objectMeta.setContentType("image/jpeg");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = SITEPATH + format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        fileName = sdf.format(new Date()) + getFileExt(fileName);
        String filePath = sitePath+ fileName;
        PutObjectResult result = client.putObject(BUCKET_NAME, fileName, in, objectMeta);
        logger.info("result etag :" + result.getETag() + "filepath:" + filePath);
        return filePath;
    }

    private String uploadFile(String fileName ,InputStream input )
            throws OSSException, ClientException, FileNotFoundException ,IOException{
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(input.available());
        objectMeta.setContentType("image/jpeg");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = SITEPATH + format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        fileName = sdf.format(new Date()) + getFileExt(fileName);
        String filePath = sitePath+ fileName;
        PutObjectResult result = client.putObject(BUCKET_NAME, fileName, input, objectMeta);
        logger.debug("result etag :" + result.getETag() + "filepath:" + filePath);
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
        return new File(realPath).getParent() + "/" + path;
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
