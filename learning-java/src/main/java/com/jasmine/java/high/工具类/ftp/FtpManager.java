package com.jasmine.java.high.工具类.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 连接ftp
 */
@Component
public class FtpManager{
    private List<String> pathList = Collections.synchronizedList(new ArrayList<String>());

    private FTPClient ftpClient;

    private static final String host = "192.168.1.2";
    private static final int port = 21;
    private static final String username = "ftpadmin";
    private static final String password = "admin";


    public FtpManager () {
        createFtpClient();
    }

    /**
     * 获取FTPClient对象
     */
    public FTPClient createFtpClient() {
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);       // 连接FTP服务器
            ftpClient.login(username, password); // 登陆FTP服务器

            // 开启被动模式
            ftpClient.enterLocalPassiveMode();

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                System.out.println("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                System.out.println("FTP连接成功。");
            }
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /**
     * 获取根目录
     */
    public String printWorkingDirectory() {
        if (!ftpClient.isConnected())
            return "";

        try {
            return ftpClient.printWorkingDirectory();
        } catch (IOException e) {
            // do nothing
        }

        return "";
    }

    /**
     * 递归列出ftp上文件目录下的文件
     * @param path ftp上文件目录
     */
    public List<String> listFileNames(String path) throws IOException {
        // 1. 列出目录下的文件及文件夹
        FTPFile[] files = ftpClient.listFiles(path);
        if (null == files || files.length <= 0)
            return pathList;

        if (path.equals("/")) path = "";

        // 2. 遍历文件夹,该根目录文件夹下不存在图片,图片存在以设备名称命名的文件夹下
        for (FTPFile file : files) {
            if (file.isFile() && (file.getName().contains(".jpg"))) {
                pathList.add(path + "/" + file.getName());
            } else if (file.isDirectory()) {
                listFileNames( path + "/" + file.getName());
            }
        }
        return pathList;
    }


    /**
     * 下载ftp文件到本地上
     * @param fileName    ftp文件路径名称
     */
    public void downloadToOss(String fileName) throws IOException {
        InputStream ips = null;
        try {
            Long start = System.currentTimeMillis();
            ips = ftpClient.retrieveFileStream(fileName);
            System.out.println("获取流用时:" + String.valueOf(System.currentTimeMillis() - start));

            Long start2 = System.currentTimeMillis();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length());
//            String url = OssManager.uploadImageV2(ossClient,OssManager.ALI_YUN_OSS_IMG_CAMERA,fileName,ips);
            System.out.println("上传流用时:" + String.valueOf(System.currentTimeMillis() - start2));
//            System.out.println("文件地址 : " + url);
            System.out.println("───────────────────────────────────────────");
        } finally {
            if (null != ips) {
                ips.close();
            }
            ftpClient.completePendingCommand();
        }
    }

    /**
     * 关闭ftp连接
     */
    public void closeFtp() {
        System.out.println("===> 关闭 ftp");
        if(null != ftpClient && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                System.out.println("FTP关闭错误 : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }



    public void toOss() throws IOException {
        String root = this.printWorkingDirectory();
        List<String> pathList = this.listFileNames(root);

//        OSS ossClient = OssManager.getOssInstance();
        // 循环上传
        for (String fileName : pathList) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length());

            InputStream is = null;
            OutputStream os  = null;
            FileOutputStream fos = null;
            FileInputStream fis = null;

            System.out.println(fileName);
            Long ftpStart = System.currentTimeMillis();
            try {
//                ips = ftpClient.retrieveFileStream(fileName);
//                System.out.println("获取流用时:" + String.valueOf(System.currentTimeMillis() - ftpStart));
//
////                 ================ 上传到oss ==============
//                Long ossStart = System.currentTimeMillis();
//                System.out.println(fileName);
//                String url = OssManager.inputStreamUpLoad(ossClient, ips, fileName);
//                System.out.println("上传流用时:" + String.valueOf(System.currentTimeMillis() - ossStart));


//                out = new BufferedOutputStream(new FileOutputStream("E:\\WorkSpace\\ftp\\test"));
//                if (!ftpClient.retrieveFile(fileName, out)) {
//                    throw new IOException("Error loading file " + fileName + " from FTP server. Check FTP permissions and path.");
//                }
//                out.flush();

                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setFileTransferMode(FTPClient.STREAM_TRANSFER_MODE);
                is = ftpClient.retrieveFileStream(fileName);
                fos = new FileOutputStream(new File("E:\\WorkSpace\\ftp\\test\\" + fileName));
                byte[] b = new byte[1024];
                int len = 0;
                while ((len = is.read(b)) != -1) {
                    fos.write(b,0,len);
                }
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
                }
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
                if(fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
                if(fos != null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
//                ftpClient.completePendingCommand();
                System.out.println("────────────────────────────────────────────────");
            }
        }
//        ossClient.shutdown();
    }

    public static void main(String[] args) throws IOException {
        FtpManager ftp = new FtpManager();

        ftp.toOss();



//        try {
            // 遍历文件上传至oss
//        } finally {
//            ftp.closeOss();
//            ftp.closeFtp();
//        }

//        List list1 = pathList.subList(0,10);
//        List list2 = pathList.subList(10,20);
//        List list3 = pathList.subList(20,30);
//        List list4 = pathList.subList(30,40);
//        List list5 = pathList.subList(40,pathList.size());
//
//        System.out.println(pathList.size());
//        System.out.println(list1.size());
//        System.out.println(list2.size());
//        System.out.println(list3.size());
//        System.out.println(list4.size());
//        System.out.println(list5.size());


//        UplOss upl1 = new UplOss(list1);
//        UplOss upl2 = new UplOss(list2);
//        UplOss upl3 = new UplOss(list3);
//        UplOss upl4 = new UplOss(list4);
//        UplOss upl5 = new UplOss(list5);

//        ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                5,  //核心池的大小（即线程池中的线程数目大于这个参数时，提交的任务会被放进任务缓存队列）
//                10, //线程池最大能容忍的线程数
//                200, //线程存活时间
//                TimeUnit.MILLISECONDS, //参数keepAliveTime的时间单位
//                new ArrayBlockingQueue<Runnable>(5) //任务缓存队列，用来存放等待执行的任务
//        );

//        executor.execute(upl1);
//        executor.execute(upl2);
//        executor.execute(upl3);
//        executor.execute(upl4);
//        executor.execute(upl5);

//        executor.shutdown();
//        while (true) {
//            if (executor.isTerminated()) {
//                long time = System.currentTimeMillis() - start;
//                System.out.println("程序结束了，总耗时：" + time + " ms(毫秒)！\n");
//                break;
//            }
//        }

//        try {
//            // 遍历文件上传至oss
//            for (String s : pathList) {
//                if(s.contains(".jpg") || s.contains(".jpeg") || s.contains(".png")) {
//                    ftp.downloadToOss(s);
//                    System.out.println(s);
//                    num++;
//                }
//            }
//        } finally {
//            ftp.closeOss();
//            ftp.closeFtp();
//        }


//        long end = System.currentTimeMillis() - start;
//        System.out.println("总共上传:" + num);
//        System.out.println("总共用时:" + end);
    }

//    /**
//     * 线程
//     */
//    public static class UplOss implements Runnable {
//        private List<String> pathList;
//        private FTPClient ftpClient;
//        private OSSClient ossClient;
//
//        public UplOss (List<String> list,OSSClient ossClient) {
//            this.pathList = list;
//            this.ossClient = ossClient;
//        }
//
//        @Override
//        public void run() {
//            for (String fileName : pathList) {
//                InputStream ips = null;
//                Long start = System.currentTimeMillis();
//                try {
//                    ips = ftpClient.retrieveFileStream(fileName);
//                    System.out.println("获取流用时:" + String.valueOf(System.currentTimeMillis() - start));
//
//                    Long start2 = System.currentTimeMillis();
//                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1,fileName.length());
//                    String url = null;
////                    try {
////                        url = OssManager.uploadImageV2(ossClient,OssManager.ALI_YUN_OSS_IMG_CAMERA,fileName,ips);
////                    } catch (ServerException e) {
////                        e.printStackTrace();
////                    }
//                    System.out.println("上传流用时:" + String.valueOf(System.currentTimeMillis() - start2));
//                    System.out.println("文件地址 : " + url);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (null != ips) {
//                        try {
//                            System.out.println("ips -> close");
//                            ips.close();
//                            ftpClient.completePendingCommand();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                System.out.println("───────────────────────────────────────────");
//                try {
//                    Thread.currentThread().sleep(0);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }





































}
