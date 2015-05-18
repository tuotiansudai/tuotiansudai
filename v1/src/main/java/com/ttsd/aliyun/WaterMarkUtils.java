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
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);
            //水印文件
            File _filebiao = new File(waterImg);
            Image src_biao = ImageIO.read(_filebiao);
            int width_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            g.drawImage(src_biao, (width - width_biao) / 2, (height - height_biao) / 2, width_biao, height_biao, null);
            //水印文件结束
            g.dispose();

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


    public static String inputStream2String(InputStream in_st){
        BufferedReader in = new BufferedReader(new InputStreamReader(in_st));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 打印文字水印图片
     *
     * @param pressText--文字
     * @param targetImg --目标图片
     * @param fontName --字体名
     * @param fontStyle --字体样式
     * @param color --字体颜色
     * @param fontSize --字体大小
     * @param x --偏移量
     * @param y
     */

    public static void pressText(String pressText, String targetImg,
                                 String fontName, int fontStyle, int color, int fontSize, int x,
                                 int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int width = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, width, height, null);

            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));

            g.drawString(pressText, width - fontSize - x, height - fontSize / 2 - y);
            g.dispose();
            FileOutputStream out = new FileOutputStream(targetImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
