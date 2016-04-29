package com.esoft.archer.common.service;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpSession;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 验证码service
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-6 下午8:37:47
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-6 wangzhi 1.0
 */
public interface CaptchaService {
	/**
	 * 生成验证码图片，验证码在session中
	 * 
	 * @return 验证码图片
	 */
	public BufferedImage generateCaptchaImg(HttpSession session);

	/**
	 * 验证验证码正确与否，验证码在session中
	 * 
	 * @param captchaStr
	 *            被验证的验证码
	 * @return
	 */
	public boolean verifyCaptcha(String captcha, HttpSession session);

	public void generateCaptchaInRedis(String sessionId ,String captcha) throws Exception;

	public void generateCaptchaStatusInRedis(String sessionId);

	public BufferedImage generateCaptchaImgByRedis(HttpSession session);

	public String getValueInRedisByKey(String key);

	public void deleteCaptchaFormRedis(String sessionId);

	public boolean imageCaptchaStatusIsSuccess(String sessionIdInRedisStatus);


}
