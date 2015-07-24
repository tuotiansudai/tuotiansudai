package com.ttsd.mobile.mobileFilter;

import com.esoft.core.jsf.util.FacesUtil;

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
        if (isMobileBrowser) {
            if (visitURI.equals("/register")){
                ((HttpServletResponse) response).sendRedirect("/mobile/register");
            } else if (visitURI.equals("/user/get_investor_permission")) {
                ((HttpServletResponse) response).sendRedirect("/mobile/certification");
            }
        } else {
            if (visitURI.equals("/mobile/register")) {
                ((HttpServletResponse) response).sendRedirect("/register");
            } else if (visitURI.equals("/mobile/certification")) {
                ((HttpServletResponse) response).sendRedirect("/user/get_investor_permission");
            }
        }
        chain.doFilter(req,res);
    }

    @Override
    public void destroy() {

    }
}
