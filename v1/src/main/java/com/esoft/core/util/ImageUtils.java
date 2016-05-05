package com.esoft.core.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esoft.archer.config.ConfigConstants;

public final class ImageUtils {
	private final static Log log = LogFactory.getLog(ImageUtils.class);
	private final static StringManager sm = StringManager
			.getManager(ConfigConstants.Package);

	public ImageUtils() {

	}

	/**
	 * 把图片印刷到图片上
	 * 
	 * @param pressImg
	 *            -- 水印文件
	 * @param targetImg
	 *            -- 目标文件
	 * @param x
	 *            --x坐标
	 * 
	 * @param y
	 * 
	 *            --y坐标
	 */

	public final static BufferedImage pressImage(String pressImg,
			String targetImg, int x, int y) {
		BufferedImage image = null;
		try {
			// 目标文件
			File _file = new File(targetImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			// 水印文件
			File _filebiao = new File(pressImg);
			Image src_biao = ImageIO.read(_filebiao);
			int wideth_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			g.drawImage(src_biao, (wideth - wideth_biao) / 2,
					(height - height_biao) / 2, wideth_biao, height_biao, null);
			// 水印文件结束
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 
	 * 打印文字水印图片
	 * 
	 * 
	 * 
	 * @param pressText
	 * 
	 *            --文字
	 * 
	 * @param targetImg
	 *            --
	 * 
	 *            目标图片
	 * 
	 * @param fontName
	 *            --
	 * 
	 *            字体名
	 * 
	 * @param fontStyle
	 *            --
	 * 
	 *            字体样式
	 * 
	 * @param color
	 *            --
	 * 
	 *            字体颜色
	 * 
	 * @param fontSize
	 *            --
	 * 
	 *            字体大小
	 * 
	 * @param x
	 *            --
	 * 
	 *            偏移量
	 * 
	 * @param y
	 */

	public static BufferedImage addWaterMark(String pressText,
			String sourceImg, String fontName, int fontStyle,
			Color color, int fontSize, int x, int y) {
		BufferedImage image = null;
		try {
			File _file = new File(sourceImg);
			Image src = ImageIO.read(_file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			image = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			g.setColor(color);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.drawString(pressText, wideth - fontSize - fontSize * 5, height
					- fontSize / 2 - fontSize * 2);
			g.dispose();
		} catch (Exception e) {
			log.error(sm.getString("log.error.watermark"));
			e.printStackTrace();
		}
		return image;
	}

}
