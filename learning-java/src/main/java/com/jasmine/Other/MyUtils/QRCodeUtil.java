package com.jasmine.Other.MyUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.jasmine.Java高级.工具类.字符串工具.StringUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

/**
 * 生成二维码工具类
 *
 * 引入如下Jar包
 * <pre>
 * <dependency>
 *     <groupId>com.google.zxing</groupId>
 *     <artifactId>core</artifactId>
 *     <version>3.3.3</version>
 * </dependency>
 *
 * <dependency>
 *     <groupId>net.coobird</groupId>
 *     <artifactId>thumbnailator</artifactId>
 *     <version>0.4.8</version>
 * </dependency>
 * </pre>
 *
 * @author : wangyf
 */
@SuppressWarnings("all")
public class QRCodeUtil {

    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int WIDTH = 80;
    // LOGO高度
    private static final int HEIGHT = 80;


    /**
     * 生成二维码
     * @param content 二维码内容
     * @param logoPath logo地址
     * @param needCompress 是否压缩
     * @return 图片流
     */
    public static BufferedImage createQRImage(String content, String logoPath, Boolean needCompress) {
        // 二维码参数
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        } catch (WriterException e) {
            throw new RuntimeException("二维码生成错误:" + content, e);
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (StringUtil.isNotBlank(logoPath)) {
            // 插入LOGO 图片
            try {
                image = QRCodeUtil.insertImage(image, logoPath, needCompress);
            } catch (Exception e) {
                throw new RuntimeException("二维码插入LOGO错误:" + content, e);
            }
        }
        return image;
    }

    /**
     * 生成base64格式二维码
     * @param content 二维码内容
     * @param logoPath logo地址
     * @param needCompress 是否压缩
     * @return 图片BASE64字符串
     */
    public static String createQRBase64(String content, String logoPath, Boolean needCompress) {
        BufferedImage image = createQRImage(content, logoPath, needCompress);
        try {
            return encodeToString(image);
        } catch (IOException e) {
                throw new RuntimeException("二维码转化BASE64错误:" + content, e);
        }
    }

    /**
     * 插入logo
     * @param source 偷油婆
     * @param logoPath logo url 地址
     */
    private static BufferedImage insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {
        BufferedImage logoImage = ImageIO.read(new URL(logoPath));
        if (needCompress) { // 压缩LOGO
            logoImage = Thumbnails.of(logoImage).size(WIDTH, HEIGHT).asBufferedImage();
        }
        // 压缩并且添加图片
        source = Thumbnails.of(source)
                .size(QRCODE_SIZE, QRCODE_SIZE)
                .watermark(Positions.CENTER, logoImage, 1.0f)
                .asBufferedImage();
        return source;
    }

    /**
     * 生成二维码(内嵌LOGO)
     * @param content      内容
     * @param imgPath      LOGO地址
     * @param destPath     存放目录
     * @param needCompress 是否压缩LOGO
     */
    private static void encode(String content, String imgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createQRImage(content, imgPath, needCompress);
        FileOutputStream file = new FileOutputStream(destPath);
        ImageIO.write(image, FORMAT_NAME, file);
    }
    /**
     * 图片流转BASE64字符串
     * @param bufferedImage 图片流
     * @return 图片BASE64字符串
     */
    private static String encodeToString(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg",outputStream);
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(outputStream.toByteArray());
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = createQRImage(
                "https://www.linqmi.com/react/#/login",
                "https://wyf-bkt.oss-cn-hangzhou.aliyuncs.com/bjstk/work/other/profile/jasminexz.jpg",
                true);
        File outputfile = new File("C:\\Users\\jasmine\\Desktop\\save\\qr.png");
        ImageIO.write(bufferedImage, "png", outputfile);
    }
}
