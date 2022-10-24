package com.jasmine.A1_java.high.工具类.ftp;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * OSS链接
 * @author : jasmineXz
 */
public class OssManager {
    private static final String REGIONID = "cn-hangzhou";                               /** 地区 */
    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";      /** 端点 */
    /** 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。*/
    private static final String ACCESSKEYID = "LTAI4FsUACce6yj7o1qp1tYA";
    private static final String ACCESSKEYSECRET = "bUHGeJB94kEUWGY48LAnxMa6nokcpV";     /** 密码 */
    private static final String BUCKET_NAME = "xiaozei-bucket";                         /** bucket名称 */
    private static final String FILE_PATH_LINQMI = "linqmi/img/";                       /** 文件路径 */
    private static final String FILE_PATH_XIAOZ_FTPTEST = "xiaozei/ftptest/";                       /** 文件路径 */
    private final static String ALIYUN_ACCESS_DOMAIN = "https://xiaozei-bucket.oss-cn-hangzhou.aliyuncs.com/";  /** 自定义域名 */
    private final static long DURATION_SECONDS = 900L;
    private static final String ROLEARN = "acs:ram::1200498707099906:role/aliyunosstokengeneratorrole";

    public static void main(String[] args) throws FileNotFoundException {
        // 获取全部BUCKET
//        OssManager.getBuckets();

        // 获取指定前缀BUCKET
//        OssManager.getBucketsForPrefix("指定前缀");

        // 存储空间是否存在
//        System.out.println(OssManager.existsBucket(BUCKET_NAME));

        //获取bucket详情
//        OssManager.printBucketDetails(BUCKET_NAME);

//        OssManager.localFileUpLoad();

        File file =  new File("F:\\01_学习\\gamersky_05origin_09_2019101219073D.jpg");
        InputStream inputStream = new FileInputStream(file);
        OssManager.inputStreamUpLoad(null,inputStream,"gamersky_05origin_09_2019101219073D.jpg");
    }

    public static OSS getOssInstance(){
        return new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
    }


    /**
     * 获取全部BUCKETS
     */
    public static void getBuckets(){
        OSS ossClient = getOssInstance();

        // 列举存储空间。
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }

        ossClient.shutdown();
    }

    /**
     * 根据指定前缀获取BUCKETS
     * @param bucketPrefix bucket前缀
     */
    public static void getBucketsForPrefix(String bucketPrefix){
        OSS ossClient = getOssInstance();

        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 列举指定前缀的存储空间。
        listBucketsRequest.setPrefix(bucketPrefix);
        BucketList bucketList = ossClient.listBuckets(listBucketsRequest);
        for (Bucket bucket : bucketList.getBucketList()) {
            System.out.println(" - " + bucket.getName());
        }

        ossClient.shutdown();
    }

    /**
     * 存储空间是否存在
     * @param bucketName bucket名称
     */
    public static boolean existsBucket(String bucketName){
        OSS ossClient = getOssInstance();

        boolean exists = ossClient.doesBucketExist(bucketName);

        ossClient.shutdown();
        return exists;
    }


    /**
     * 获取Bucket信息
     * @param bucketName bucket名称
     */
    public static void printBucketDetails(String bucketName){
        OSS ossClient = getOssInstance();

        BucketInfo info = ossClient.getBucketInfo(bucketName);
        // 获取地域。
        System.out.println("地域 : " + info.getBucket().getLocation());
        // 获取创建日期。
        System.out.println("创建日期 : " + info.getBucket().getCreationDate());
        // 获取拥有者信息。
        System.out.println("拥有者信息 : " + info.getBucket().getOwner());
        // 获取权限信息。
        System.out.println("权限信息 : " + info.getGrants());

        ossClient.shutdown();
    }


    /**
     * 上传本地图片
     */
    public static void localFileUpLoad(){
        OSS ossClient = getOssInstance();
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, FILE_PATH_XIAOZ_FTPTEST + "test.jpg",
                new File("F:\\01_学习\\gamersky_05origin_09_2019101219073D.jpg"));
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
    }


    /**
     * 上传文件流
     * @param file 文件
     * @return 返回阿里云文件路径
     */
    public static String fileUpLoad(MultipartFile file){
        OSS ossClient = getOssInstance();
        try {
            // 添加 ContentType
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType("image/jpg");
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(BUCKET_NAME,FILE_PATH_LINQMI + file.getOriginalFilename() ,inputStream, objectMetadata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ossClient.shutdown();
        return ALIYUN_ACCESS_DOMAIN + FILE_PATH_LINQMI + file.getOriginalFilename();
    }

    /**
     * 上传文件流
     * @return 返回阿里云文件路径
     */
    public static String inputStreamUpLoad(OSS ossClient1,InputStream inputStream,String key){
        OSS ossClient = getOssInstance();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        ossClient.putObject(BUCKET_NAME,FILE_PATH_XIAOZ_FTPTEST + key ,inputStream, objectMetadata);
        ossClient.shutdown();
        return ALIYUN_ACCESS_DOMAIN + FILE_PATH_XIAOZ_FTPTEST + key;
    }


}

