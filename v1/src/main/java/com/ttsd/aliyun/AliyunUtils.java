package com.ttsd.aliyun;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.esoft.core.util.ImageUploadUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.UploadedFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 *
 *@Description: 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 *
 * 该示例代码的执行过程是：
 * 1. 创建一个Bucket（如果已经存在，则忽略错误码）；
 * 2. 上传一个文件到OSS；
 * 3. 下载这个文件到本地；
 * 4. 清理测试资源：删除Bucket及其中的所有Objects。
 *
 * 尝试运行这段示例代码时需要注意：
 * 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 *    示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！
 * 2. 请使用您的API授权密钥填充ACCESS_KEYID和ACCESS_KEY常量；
 * 3. 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径；
 *    修改常量downloadFilePath为下载文件的路径。
 * 4. 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
     *OSS Java API手册：http://aliyun_portal_storage.oss.aliyuncs.com/oss_api/oss_javahtml/index.html?spm=5176.7150518.1996836753.8.d5TjaG
     *OSS Java SDK开发包:http://help.aliyun.com/view/13438814.html
     *OSSClient:www.mvnrepository.com/artifact/cglib/cglib/2.2

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
     * @throws FileNotFoundException
     */
    public static String uploadFileBlur(String fileName ,InputStream inputStream ,String rootPath)
            throws OSSException, ClientException, FileNotFoundException ,IOException{
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        String waterPath = rootPath + "/site/themes/default/images/watermark.png";
        ByteArrayInputStream in = new ByteArrayInputStream(WaterMarkUtils.pressImage(waterPath,inputStream,0,0).toByteArray());
        objectMeta.setContentLength(in.available());
        objectMeta.setContentType("image/jpeg");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        fileName = sdf.format(new Date()) + ImageUploadUtil.getFileExt(fileName);
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
        fileName = sdf.format(new Date()) + ImageUploadUtil.getFileExt(fileName);
        String filePath = sitePath+ fileName;
        PutObjectResult result = client.putObject(BUCKET_NAME, fileName, input, objectMeta);
        log.debug("result etag :" + result.getETag() + "filepath:" + filePath);
        return filePath;
    }




    public static String uploadFileInputStream(UploadedFile uploadedFile)
            throws OSSException, ClientException ,IOException{
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        String filename = uploadedFile.getFileName();
        //判断上传类型，多的可根据自己需求来判定
        if (filename.endsWith("xml")) {
            objectMeta.setContentType("text/xml");
        }
        else if (filename.endsWith("jpg")) {
            objectMeta.setContentType("image/jpeg");
        }
        else if (filename.endsWith("png")) {
            objectMeta.setContentType("image/png");
        }
        objectMeta.setContentLength(uploadedFile.getSize());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        filename = sdf.format(new Date()) + ImageUploadUtil.getFileExt(uploadedFile.getFileName());
        String filepath = "/"+sitePath+ filename;
        PutObjectResult result = client.putObject(BUCKET_NAME, filename, uploadedFile.getInputstream(), objectMeta);
        log.info("filepath : " + filepath + "etag:" + result.getETag());
        return filepath;
    }




}
