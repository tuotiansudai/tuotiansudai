package com.ttsd.mobile.mobileFilter;

import com.esoft.core.jsf.util.FacesUtil;
import com.ttsd.mobile.Util.MobileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        String functionVersion = request.getParameter("functionVersion");
        boolean isMobileBrowser = FacesUtil.isMobileRequestForMobile(req);
        HttpSession session = req.getSession();
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) session.getAttribute("SPRING_SECURITY_CONTEXT");
        boolean notLogin = (securityContextImpl == null);
        String visitURL = req.getRequestURL().toString();
        if(!visitURL.contains(".js") && !visitURL.contains(".css") && !visitURL.contains(".png") && !visitURL.contains(".jpg")  && !visitURL.contains(".jpeg") && !visitURL.contains(".gif")){
            if (functionVersion != null && isMobileBrowser && notLogin){
                if ("computer".equals(functionVersion)){
                    req.getRequestDispatcher("/").forward(req, res);
                }else if ("mobile".equals(functionVersion)){
                    req.getRequestDispatcher("/mobile/register").forward(req, res);
                }
            }else if (functionVersion == null && isMobileBrowser && notLogin){
                if (isMobileBrowser){
                    req.getRequestDispatcher("/mobile/register").forward(req, res);
                }
            }
        }
        chain.doFilter(req,res);
    }

    @Override
    public void destroy() {

    }
}
