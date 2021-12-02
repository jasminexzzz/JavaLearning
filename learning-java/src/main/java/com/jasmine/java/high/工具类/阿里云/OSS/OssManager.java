package com.jasmine.java.high.工具类.阿里云.OSS;

import com.aliyun.oss.*;
import com.aliyun.oss.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.jasmine.java.high.工具类.序列化工具.JackSon.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * OSS链接
 * @author : jasmineXz
 */
public class OssManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(OssManager.class);

    private static JsonNode POLICY;

    /**
     * 地区
     */
    private static final String REGIONID = "cn-hangzhou";

    /**
     * 端点
     */
    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";

    /**
     * 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
     */
    private static final String ACCESSKEYID = "LTAI4FsUACce6yj7o1qp1tYA";

    /**
     * 密码
     */
    private static final String ACCESSKEYSECRET = "bUHGeJB94kEUWGY48LAnxMa6nokcpV";

    private static final String BUCKET = "best-hzjl";

    private final static long DURATION_SECONDS = 900L;


    private final static String ALIYUN_ACCESSKEY_SECRET = "9fRBtt9Uj575VkCG06YDHmbt9NjJso";


    private static final String VERSION = "2015-04-01";

    private static final String ROLEARN = "acs:ram::1200498707099906:role/aliyunosstokengeneratorrole";

    private static final String FILEPATH = "oss/img/bar/";

    /**
     * 自定义域名
     */
    private final static String ALIYUN_ACCESS_DOMAIN = "https://best-hzjl.oss-cn-hangzhou.aliyuncs.com/";

    /**
     * 图片上传地址
     */
    private static final String ALI_YUN_OSS_IMG_HOST = "oss/img/bar/";


    static {
        POLICY = JsonUtil.readNode("{\"Statement\": [{\"Action\": [\"oss:*\"],\"Effect\": \"Allow\",\"Resource\": [\"acs:oss:*:*:*\"]}],\"Version\": \"1\"}");
    }


    public static void main(String[] args) {
        // 获取全部BUCKET
        OssManager.getBuckets();

        // 获取指定前缀BUCKET
        OssManager.getBucketsForPrefix("指定前缀");

        // 存储空间是否存在
//        System.out.println(OssManager.existsBucket("xiaozei"));
    }


    /**
     * 获取全部BUCKETS
     */
    public static void getBuckets(){
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

        // 列举存储空间。
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }
//
    /**
     * 根据指定前缀获取BUCKETS
     */
    public static void getBucketsForPrefix(String bucketPrefix){
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);

        ListBucketsRequest listBucketsRequest = new ListBucketsRequest();
        // 列举指定前缀的存储空间。
        listBucketsRequest.setPrefix(bucketPrefix);
        BucketList bucketList = ossClient.listBuckets(listBucketsRequest);
        for (Bucket bucket : bucketList.getBucketList()) {
            System.out.println(" - " + bucket.getName());
        }

        ossClient.shutdown();
    }

//    /**
//     * 存储空间是否存在
//     */
//    public static boolean existsBucket(String bucketName){
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESSKEYID, ACCESSKEYSECRET);
//
//        boolean exists = ossClient.doesBucketExist("<yourBucketName>");
//        System.out.println(bucketName + "是否存在 : " + exists);
//        // 关闭OSSClient。
//        ossClient.shutdown();
//        return exists;
//    }




//    public AliTokenDTO getOssSTS(String filePath) {
//        filePath = (filePath != null) ? filePath : FILEPATH;
//        try {
//            IClientProfile profile = DefaultProfile.getProfile(REGIONID, ACCESSKEYID, SECRET);
//            DefaultAcsClient client = new DefaultAcsClient(profile);
//
//            final AssumeRoleRequest request = new AssumeRoleRequest();
//            request.setVersion(VERSION);
//            request.setMethod(MethodType.POST);
//            request.setProtocol(ProtocolType.HTTPS);
//            request.setDurationSeconds(DURATION_SECONDS);
//            request.setRoleArn(ROLEARN);
//            request.setRoleSessionName(UUID.randomUUID().toString().replace("-", ""));
//            request.setPolicy(POLICY.toString());
//            final AssumeRoleResponse response = client.getAcsResponse(request);
//            AliTokenDTO token = new AliTokenDTO(
//                    response.getCredentials().getAccessKeyId(),
//                    response.getCredentials().getAccessKeySecret(),
//                    response.getCredentials().getSecurityToken(),DURATION_SECONDS,
//                    ENDPOINT, BUCKET, filePath);
//            return token;
//        } catch (ClientException e) {
//            LOGGER.error("get ali token failed! code : {}, message : {}.", e.getErrCode(), e.getErrMsg());
//            return null;
//        }
//    }


//    /**
//     * 字节数组上传
//     *
//     * @param key
//     * @param bytes
//     * @return
//     * @throws
//     */
//    public static String uploadImage(String key, byte[] bytes) throws Exception {
//        OSS ossClient = new OSSClient(ENDPOINT, ACCESSKEYID2, ALIYUN_ACCESSKEY_SECRET, new ClientConfiguration());
//        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentType("image/jpeg");
//        try {
//            ossClient.putObject(BUCKET, key, new ByteArrayInputStream(bytes), meta);
//            return ALIYUN_ACCESS_DOMAIN + "/" + key;
//        } catch (OSSException | com.aliyun.oss.ClientException e) {
//            System.out.println("图片上传异常");
//            return null;
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//    /**
//     * 数据流上传图片
//     *
//     * @param key
//     * @param input
//     * @return
//     * @throws ServerException
//     */
//    public static  String uploadImage(String key, InputStream input) throws ServerException {
//        key = String.format("%s%s%s", key, UUID.randomUUID().toString().replace("-", ""), ".jpeg");
//        // 创建OSS连接
//        OSS ossClient = new OSSClient(
//                ENDPOINT,                    // "https://oss-cn-hangzhou.aliyuncs.com";
//                ACCESSKEYID2,                // "LTAIAxXo72erNVr2"
//                ALIYUN_ACCESSKEY_SECRET,     // "9fRBtt9Uj575VkCG06YDHmbt9NjJso"
//                new ClientConfiguration());
//
//        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentType("image/jpeg");
//        try {
//            ossClient.putObject(BUCKET, key, input, meta);
//            String url = String.format("%s%s", ALIYUN_ACCESS_DOMAIN, key);
//            return url;
//        } catch (OSSException | com.aliyun.oss.ClientException e) {
//            throw new ServerException("上传图片异常");
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//    /**
//     * 上传文件 pdf,.word
//     * @param inputStream
//     * @param imgHost
//     * @param suffixName
//     * @return
//     * @throws Exception
//     */
//    public static String uploadFile(InputStream inputStream, String imgHost,String suffixName) throws Exception {
//        OSS ossClient = new OSSClient(ENDPOINT, ACCESSKEYID2,ALIYUN_ACCESSKEY_SECRET, new ClientConfiguration());
//        try {
//            String key = String.format("%s%s%s", imgHost, UUID.randomUUID().toString().replace("-", ""), suffixName);
//            ossClient.putObject(BUCKET, key, inputStream);
//            String url = String.format("%s%s", ALIYUN_ACCESS_DOMAIN, key);
//            return url;
//        } catch (OSSException | com.aliyun.oss.ClientException e) {
//            throw new ServerException("上传文件异常");
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//
//    /**
//     * 图片地址上传
//     *
//     * @param key
//     * @param imgUrl
//     * @return
//     * @throws ServerException
//     */
//    public static String uploadUrlImg(String key, String imgUrl) throws ServerException {
//        OSS ossClient = new OSSClient(ENDPOINT, ACCESSKEYID2,
//                ALIYUN_ACCESSKEY_SECRET, new ClientConfiguration());
//
//        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentType("image/jpeg");
//        try {
//            InputStream inputStream = new URL(imgUrl).openStream();
//            ossClient.putObject(BUCKET, key, inputStream, meta);
//            return ALIYUN_ACCESS_DOMAIN + "/" + key;
//        } catch (Exception e) {
//            throw new ServerException("上传图片异常");
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//    /**
//     * 删除图片
//     *
//     * @param photoUrl
//     * @return
//     * @throws ServerException
//     */
//    public static void deleteImg(String photoUrl) throws ServerException {
//        OSS ossClient = new OSSClient(ENDPOINT, ACCESSKEYID2,
//                ALIYUN_ACCESSKEY_SECRET, new ClientConfiguration());
//        try {
//            String[] split = photoUrl.split("com/");
//            ossClient.deleteObject(BUCKET, split[1]);
//        } catch (Exception e) {
//            throw new ServerException("上传图片异常");
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//
//    /**
//     * 阿里云批量下载文件,以ZIP压缩包下载
//     * @param fileNameList 文件名,作为key去获取文件
//     * @param zipName 压缩包名称
//     * @param response response
//     */
//    public static void downloadFile(List<String> fileNameList, String zipName, HttpServletResponse response) throws ServerException {
//        if (CollectionUtils.isEmpty(fileNameList))
//            throw new IllegalArgumentException("合同列表为空");
//
//        // 1.连接阿里云OSS服务
//        OSS ossClient = new OSSClient(ENDPOINT, ACCESSKEYID2, ALIYUN_ACCESSKEY_SECRET, new ClientConfiguration());
//        try {
//            setResponseHeader(zipName, response);
//            List<OSSObject> ossArr = new ArrayList<>(fileNameList.size());
//            for (String fileName : fileNameList) {
//                // 2.从阿里云OSS获取文件
//                ossArr.add(ossClient.getObject(BUCKET, ALI_YUN_OSS_IMG_HOST + standardFileName(fileName)));
//            }
//            // 3.写入流
//            download(ossArr, response);
//        } catch (Exception e) {
//            throw new RuntimeException("下载文件异常: " + e.getMessage());
//        } finally {
//            ossClient.shutdown();
//        }
//    }
//
//    /**
//     * 写入流
//     * @param ossArr
//     * @param response
//     * @throws ServerException
//     */
//    private static void download(List<OSSObject> ossArr,HttpServletResponse response) throws ServerException {
//        try (ZipOutputStream zipops = new ZipOutputStream(response.getOutputStream())){
//            // 将对象循环写入压缩包
//            for(OSSObject ossObject : ossArr){
//                InputStream ips = null;
//                try {
//                    ips = ossObject.getObjectContent();
//                    zipops.putNextEntry(new ZipEntry(standardFileName(ossObject.getKey())));
//                    byte[] buffer = new byte[1024];
//                    int r = 0;
//                    // 5.写入
//                    while ((r = ips.read(buffer)) != -1) {
//                        zipops.write(buffer, 0, r);
//                    }
//                    // 异常不影响其余文件处理
//                } catch (OSSException oos){
//                    LOGGER.error("查询文件异常: {}, 异常信息: {}", ossObject.getKey(), oos.getMessage());
//                } catch (Exception e) {
//                    LOGGER.error("文件写入压缩包异常: {}, 异常信息: {}", ossObject.getKey(), e.getMessage());
//                } finally {
//                    if(ips != null)
//                        ips.close();
//                }
//            }
//            zipops.flush();
//        } catch (Exception e) {
//            throw new ServerException("下载文件异常: " + e.getMessage());
//        }
//    }
//
//    /**
//     * 设置响应流头
//     * @param zipName
//     * @param response
//     */
//    private static void setResponseHeader(String zipName, HttpServletResponse response) {
//        try {
//            response.setCharacterEncoding("UTF-8");
//            response.addHeader("Access-Control-Expose-Headers","Content-Disposition");
//            // ZIP压缩格式
//            response.setContentType("application/zip;charset=UTF-8");
//            // 设置在下载框默认显示的文件名
//            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zipName, "UTF-8"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    /**
//     * 处理文件名,阿里云的key不包含HTTP路径,只有文件路径+文件名,需要对路径进行处理
//     * 且在添加入ZIP中时会,若包含路径则会在压缩包中添加对应文件夹
//     * @param fileName 文件名
//     */
//    private static String standardFileName(String fileName){
//        fileName = StringUtil.replace(fileName,ALIYUN_ACCESS_DOMAIN,"");
//        fileName = StringUtil.replace(fileName,ALI_YUN_OSS_IMG_HOST,"");
//        fileName = StringUtil.replace(fileName,"/","");
//        return fileName;
//    }
}

