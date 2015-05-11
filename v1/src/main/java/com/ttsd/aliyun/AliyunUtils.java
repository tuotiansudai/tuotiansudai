package com.ttsd.aliyun;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.aliyun.oss.model.ObjectMetadata;
import  java.io.*;

import com.esoft.core.util.ImageUploadUtil;
import com.sun.management.VMOption;
import org.primefaces.model.UploadedFile;
import sun.text.normalizer.UBiDiProps;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 *@ClassName: TestOSS
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
    /**
     * 阿里云ACCESS_KEYID
     */
    private static String ACCESS_KEYID = "ivtvevKLeCfSZXHg";
    /**
     * 阿里云ACCESS_KEYSECRET
     */
    private	 static String ACCESS_KEYSECRET = "EBvUQ60BoWh3t7Ut7OcI3bpbwF8S0U";
    /**
     * 阿里云OSS_ENDPOINT  杭州Url
     */
    private static String OSS_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";

    /**
     * 阿里云BUCKET_NAME  OSS
     */
    private static String BUCKET_NAME = "ttsd-web-uploadfiles";


    public static void main(String[] args) {
        String Objectkey = "20150508062132.png";
        String uploadFilePath = "/Users/lance/Documents/workspace/oss/banner1.jpg";
        String downloadFilePath = "/Users/lance/Documents/workspace/oss/downbanner1.png";

        // 使用默认的OSS服务器地址创建OSSClient对象,不叫OSS_ENDPOINT代表使用杭州节点，青岛节点要加上不然包异常
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);

        //如果你想配置OSSClient的一些细节的参数，可以在构造OSSClient的时候传入ClientConfiguration对象。
        //ClientConfiguration是OSS服务的配置类，可以为客户端配置代理，最大连接数等参数。
        //具体配置看http://aliyun_portal_storage.oss.aliyuncs.com/oss_api/oss_javahtml/OSSClient.html#id2
        //ClientConfiguration conf = new ClientConfiguration();
        //OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET, conf);


        try {
//            ensureBucket(client, BUCKET_NAME);
//            setBucketPublicReadable(client, BUCKET_NAME);
//
//            System.out.println("正在上传...");
//            uploadFile(Objectkey, uploadFilePath);
//            uploadFileInputStream("banner1.jpg",filename,input);
//
//            System.out.println("正在下载...");
//            downloadFile(client, BUCKET_NAME, Objectkey, downloadFilePath);
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            deleteAllBuckets(client);
//          deleteBucket(client, BUCKET_NAME);
        }
    }


    public static OSSClient getOSSClient(){
        OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_KEYID, ACCESS_KEYSECRET);
        return client;

    }

    /**
     * 创建Bucket 并设置白名单
     *
     * @param client  OSSClient对象
     * @param bucketName  BUCKET名
     * @throws OSSException
     * @throws ClientException
     */
    public static void ensureBucket(OSSClient client, String bucketName)throws OSSException, ClientException {
        try{
            List<String> refererList = new ArrayList<String>();
            // 添加referer项 设置白名单
            refererList.add("localhost");
            refererList.add("127.0.0.1");
            refererList.add("http://www.aliyun.com");
            refererList.add("http://www.tuotiansudai.com");
            refererList.add("http://www.?.aliyuncs.com");
            // 允许referer字段为空，并设置Bucket Referer列表
            BucketReferer br = new BucketReferer(true, refererList);
            client.createBucket(bucketName);
            client.setBucketReferer(bucketName, br);
            System.out.println(bucketName +" is created!");
        }catch(ServiceException e){
            if(!OSSErrorCode.BUCKET_ALREADY_EXISTS.equals(e.getErrorCode())){
                throw e;
            }
        }
    }

    /**
     * 删除一个Bucket和其中的Objects
     *
     * @param client  OSSClient对象
     * @param bucketName  Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    private static void deleteBucket(OSSClient client, String bucketName)throws OSSException, ClientException{
        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for(int i = 0; i < listDeletes.size(); i++){
            String objectName = listDeletes.get(i).getKey();
            //如果不为空，先删除bucket下的文件
            System.out.println(" bucketname : " + bucketName + " [objectName] :" + objectName);
//            client.deleteObject(bucketName, objectName);
        }
//        client.deleteBucket(bucketName);
        System.out.println(bucketName + " is deleted!");
    }

    /**
     * 把Bucket设置成所有人可读
     *
     * @param client  OSSClient对象
     * @param bucketName  Bucket名
     * @throws OSSException
     * @throws ClientException
     */
    private static void setBucketPublicReadable(OSSClient client, String bucketName)throws OSSException, ClientException{
        //创建bucket
        client.createBucket(bucketName);

        //设置bucket的访问权限， public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    /**
     * 上传文件
     *
     * @param Objectkey  上传到OSS起的名
     * @param filename  本地文件名
     * @throws OSSException
     * @throws ClientException
     * @throws FileNotFoundException
     */
    public static String uploadFile(String filename ,InputStream input )
            throws OSSException, ClientException, FileNotFoundException ,IOException{
        OSSClient client = getOSSClient();
//        File file = new File(filepath);
//        InputStream input = new FileInputStream(f);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(input.available());

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        filename = sdf.format(new Date()) + ImageUploadUtil.getFileExt(filename);

        String location = client.getBucketLocation(BUCKET_NAME);
        System.out.println("upload key : " + filename );
        System.out.println("location :"+location);

        String filepath = sitePath+ filename;
        PutObjectResult result = client.putObject(BUCKET_NAME, filename, input, objectMeta);
        System.out.println("result etag : " + result.getETag());
        System.out.println("downing ..... ");
        downloadFile(getOSSClient(),BUCKET_NAME,filename,"/Users/lance/Documents/workspace/oss/"+filename);
        return filepath;
    }




    public static String uploadFileInputStream(UploadedFile uploadedFile)
            throws OSSException, ClientException ,IOException{
        OSSClient client = getOSSClient();
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(uploadedFile.getSize());
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

//        InputStream input =  new FileInputStream(file);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhh");
        String sitePath = PropertiesUtils.getPro("plat.sitePath")+format.format(new Date())+"/";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");

        filename = sdf.format(new Date()) + ImageUploadUtil.getFileExt(uploadedFile.getFileName());

        String location = client.getBucketLocation(BUCKET_NAME);
        System.out.println("upload key : " + filename );
        System.out.println("location :"+location);

        String filepath = sitePath+ filename;
        PutObjectResult result = client.putObject(BUCKET_NAME, filename, uploadedFile.getInputstream(), objectMeta);
        System.out.println("result etag : " + result.getETag());
        System.out.println("downing ..... ");
        downloadFile(getOSSClient(),BUCKET_NAME,filename,"/Users/lance/Documents/workspace/oss/"+filename);
        return filepath;
    }

    /**
     *  下载文件
     *
     * @param client  OSSClient对象
     * @param bucketName  Bucket名
     * @param Objectkey  上传到OSS起的名
     * @param filename 文件下载到本地保存的路径
     * @throws OSSException
     * @throws ClientException
     */
    private static void downloadFile(OSSClient client, String bucketName, String Objectkey, String filename)
            throws OSSException, ClientException {
        ObjectMetadata objectMetadata = client.getObject(new GetObjectRequest(bucketName, Objectkey),new File(filename));

    }


    private static void queryAllBuckets(OSSClient client) {
        // 获取用户的Bucket列表
        List<Bucket> buckets = client.listBuckets();
        // 遍历Bucket
        for (Bucket bucket : buckets) {
            System.out.println(bucket.getName());
        }
    }

    private static void deleteAllBuckets(OSSClient client) {
        // 获取用户的Bucket列表
        List<Bucket> buckets = client.listBuckets();
        // 遍历Bucket
        for (Bucket bucket : buckets) {
            System.out.println("bucket_name : " +bucket.getName());
            if(bucket.getName().equals("ttsd-uploadfiles")){
                continue;
            }
            deleteBucket(client, bucket.getName());

        }
    }







}
