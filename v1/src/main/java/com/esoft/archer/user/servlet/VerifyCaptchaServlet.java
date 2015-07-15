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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class VerifyCaptchaServlet extends HttpServlet {

	private static final  String VERIFYCAPTCHAERROR = "verifyCaptchaError";

	private static final  String SENDSMSERROR = "sendSmsError";

	private static final  String ERROR = "error";

	@Resource
	CaptchaService captchaSrv;

	@Resource
	UserService userService;

	@Logger
	static Log log;

	public void destroy() {
		super.destroy();
	}


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String captcha = request.getParameter("image_captcha");
		String mobileNumber = request.getParameter("mobileNumber");
		PrintWriter out = null;
		boolean verifyCaptchaResult = false;
		boolean sendSmsResult = false;
		String message = "";
		try {
			verifyCaptchaResult = captchaSrv.verifyCaptcha(captcha, request.getSession());

			if (verifyCaptchaResult){
				sendSmsResult = userService.sendRegisterByMobileNumberSMS(mobileNumber);
				if (!sendSmsResult){
					message = SENDSMSERROR;
				}
			}else{
				message = VERIFYCAPTCHAERROR;
			}
			out = response.getWriter();
			out.println("[{\"message\":\"" + message + "\"}]");
			out.flush();
		}catch (Exception e){
			message = ERROR;
			out = response.getWriter();
			out.println("[{\"message\":\""+message+"\"}]");
			out.flush();
			log.error(e.getStackTrace());
		}finally {
			if(out != null){
				out.close();

			}
		}


	}


	public void init() throws ServletException {
		// Put your code here
	}

}
