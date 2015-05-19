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

public final class WaterMarkUtils {
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
            Image src = ImageIO.read(inStream);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(src, 0, 0, width, height, null);
            //水印文件
            File waterFile = new File(waterImg);
            Image waterImage = ImageIO.read(waterFile);
            int water_width = waterImage.getWidth(null);
            int water_height = waterImage.getHeight(null);
            graphics.drawImage(waterImage, (width - water_width) / 2, (height - water_height) / 2, water_width, water_height, null);
            //水印文件结束
            graphics.dispose();

            byte[] buff = new byte[100]; //buff用于存放循环读取的临时数据
            int rc = 0;
            while ((rc = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(swapStream);
            encoder.encode(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
            return swapStream;
    }






}
