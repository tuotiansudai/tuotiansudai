package com.ttsd.aliyun;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.awt.image.JPEGImageDecoder;

public final class WaterMarkUtils {

    static Log log = LogFactory.getLog(WaterMarkUtils.class);

    public WaterMarkUtils() {

    }



    /**
     * 把图片印刷到图片上
     *
     * @param waterImg --水印文件
     * @param inStream --输入流
     * @param x --x坐标
     * @param y --y坐标
     */
    public final static ByteArrayOutputStream pressImage(String waterImg, InputStream inStream ,int x, int y) {
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
            int waterWidth = waterImage.getWidth(null);
            int waterHeight = waterImage.getHeight(null);
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
            log.error("upload oss fail ");
            e.printStackTrace();
        }
            return swapStream;
    }

}
