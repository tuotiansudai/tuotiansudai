package com.ttsd.mobile.mobileFilter;

import com.esoft.archer.user.service.UserService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.ttsd.mobile.Util.MobileUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tuotian on 15/7/15.
 */
public class BrowserFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        boolean isMobileBrowser = FacesUtil.isMobileRequestForMobile(req);
        String visitURI = req.getRequestURI();
        MobileUtil mobileUtil = new MobileUtil();
        UserService userService = (UserService)SpringBeanUtil.getBeanByName("userService");
        if (isMobileBrowser) {
            if (visitURI.equals("/register")){
                ((HttpServletResponse) response).sendRedirect("/mobile/register");
                return;
            } else if (visitURI.equals("/user/get_investor_permission") && !userService.hasRole(mobileUtil.getLoginUserId(),"INVESTOR")) {
                ((HttpServletResponse) response).sendRedirect("/mobile/certification");
                return;
            }
        } else {
            if (visitURI.equals("/mobile/register")) {
                ((HttpServletResponse) response).sendRedirect("/register");
                return;
            } else if (visitURI.equals("/mobile/certification")) {
                ((HttpServletResponse) response).sendRedirect("/user/get_investor_permission");
                return;
            }
        }
        chain.doFilter(req,res);
    }

    @Override
    public void destroy() {

    }
}
