package com.esoft.archer.system.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.system.service.AppLocalFilter;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.ImageUtils;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.StringManager;

@Service
public class WatermarkFilterImpl implements AppLocalFilter {
	@Logger
	static Log log;

	// @Resource
	HibernateTemplate ht;
	private final static StringManager sm = StringManager
			.getManager(ConfigConstants.Package);

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
		Config config = null;
		try {
			config = ht.get(Config.class,
					ConfigConstants.WaterMark.IF_OPEN_WATERMARK);
		} catch (Exception e) {
			log.error(sm.getString("log.error.configWatermarkNotFound",
					ConfigConstants.WaterMark.IF_OPEN_WATERMARK, e.toString()));
			e.printStackTrace();
			return;
		}
		String isOpen = ConfigConstants.WaterMark.UN_OPEN_WATERMARK;
		if (config == null) {
			log.warn(sm.getString("log.notFoundConfig",
					ConfigConstants.WaterMark.IF_OPEN_WATERMARK));
		} else {
			isOpen = config.getValue();
		}
		if (isOpen.equals(ConfigConstants.WaterMark.UN_OPEN_WATERMARK)) {
			return;
		}

		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();
		boolean needDeal = url.endsWith(".gif") || url.endsWith(".png")
				|| url.endsWith(".jpg") || url.endsWith(".jpeg")
				|| url.endsWith(".bmp");

		final String uploadPath = "/upload/";
		final String uploadCachePath = "/upload/cache/";

		boolean needDealConfirm = StringUtils.contains(url, uploadPath);
		if ((needDealConfirm && needDeal) == false) {
			return;
		}

		String contextPath = req.getContextPath();
		url = url.replaceFirst(contextPath, "");

		String pictureAbsolutePath = req.getSession().getServletContext()
				.getRealPath(url);// 得到图片所在的据对路径
		// 原始图片
		File picDirFile = new File(pictureAbsolutePath);
		if (!picDirFile.exists()) {
			return; // 如果文件不存在，直接返回
		}
		// 缓存的图片
		String picCachePath = url.replaceFirst(uploadPath, uploadCachePath);
		String picCacheAbsolutePath = req.getSession().getServletContext()
				.getRealPath(picCachePath);
		File picCacheFile = new File(picCacheAbsolutePath);
		BufferedImage image;
		if (!picCacheFile.exists()) {
			// 缓存不存在，创建
			String folderPath = picCacheAbsolutePath.substring(0,
					picCacheAbsolutePath.lastIndexOf(File.separator));
			File folder = new File(folderPath);
			if (!folder.exists()) {
				folder.mkdirs();
			}
			Color c = new Color(0, 0, 0);
			Config configWibeSite = ht.get(Config.class,
					ConfigConstants.Website.SITE_NAME);
			image = ImageUtils.addWaterMark(configWibeSite.getValue(),
					pictureAbsolutePath, sm.getString("waterMarkFontFamily"),
					Font.LAYOUT_RIGHT_TO_LEFT, c, 18, 300, 200);
			ImageIO.write(image, "JPEG", picCacheFile);
		}
		RequestDispatcher dispatcher = req.getRequestDispatcher(picCachePath);// 转发到缓存
		dispatcher.forward(request, response);// 实现转发
	}
}
