package com.esoft.archer.user.servlet;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;

/**
 * 邮件激活账号
 * 
 * @author yinjunlu
 * 
 */
@Component
public class ActivateAccountServlet extends HttpServlet {

	private static final long serialVersionUID = -6074598131359403903L;

	@Logger
	static Log log;

	@Resource
	UserService userService;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("activeCode");
		if (StringUtils.isNotEmpty(code)) {
			try {
				userService.activateUserByEmailActiveCode(code);
				response.sendRedirect(request.getContextPath() + "/regSuccess?showTitle=EMAIL_CHECK_TITILE");
			} catch (AuthInfoOutOfDateException e) {
				log.error(e.getStackTrace());
				response.sendRedirect(request.getContextPath() + "/activefail?showTitle=outOfDate");
			} catch (UserNotFoundException e) {
				log.error(e.getStackTrace());
				response.sendRedirect(request.getContextPath() + "/activefail?showTitle=userNotFound");
			} catch (NoMatchingObjectsException e) {
				log.error(e.getStackTrace());
				response.sendRedirect(request.getContextPath() + "/activefail");
			} catch (AuthInfoAlreadyActivedException e) {
				log.error(e.getStackTrace());
				response.sendRedirect(request.getContextPath() + "/activefail?showTitle=alreadyActived");
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/activefail?showTitle=emptyActiveCode");
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	public void init() throws ServletException {
		// Put your code here
	}

}
