package com.esoft.archer.user.servlet;

import com.esoft.archer.common.service.CaptchaService;
import com.esoft.archer.user.controller.UserHome;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.context.RequestContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class VerifyCodeInRedisServlet extends HttpServlet {

	@Resource
	CaptchaService captchaSrv;
	@Logger
	Log log;


	public VerifyCodeInRedisServlet() {
		super();
	}


	public void destroy() {
		super.destroy();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			BufferedImage image = captchaSrv.generateCaptchaImgByRedis(request.getSession());
			if (image != null){
				ImageIO.write(captchaSrv.generateCaptchaImgByRedis(request.getSession()),
						"JPG", response.getOutputStream());

			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}


	public void init() throws ServletException {

	}

}
