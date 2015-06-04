package com.esoft.core.application;

import java.util.Iterator;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.jdp2p.message.service.MailService;
import com.esoft.jdp2p.message.service.impl.MailServiceImpl;

public class ArcherExceptionHandler extends ExceptionHandlerWrapper {

    private ExceptionHandler wrapped;

    public ArcherExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterable<ExceptionQueuedEvent> events = this.wrapped.getUnhandledExceptionQueuedEvents();
        for(Iterator<ExceptionQueuedEvent> it = events.iterator(); it.hasNext();) {
            try {
                ExceptionQueuedEvent event = it.next();
                ExceptionQueuedEventContext eqec = event.getContext();
                if (!(eqec.getException() instanceof ViewExpiredException)) {
                    FacesContext context = eqec.getContext();
                    //HttpSession session = (HttpSession)context.getCurrentInstance().getExternalContext().getSession(false);
                    LoginUserInfo loginUserInfo = new LoginUserInfo();
                    String userId = loginUserInfo.getLoginUserId();
                    HttpServletRequest request = (HttpServletRequest)context.getCurrentInstance().getExternalContext().getRequest();
                    String RequestUrl = request.getRequestURL()+"?";
                    if (request.getMethod().equals("GET")) {
                        RequestUrl += request.getQueryString();
                    } else {
                        Map<String, String[]> params = request.getParameterMap();
                        String queryString = "";
                        for (String key : params.keySet()) {
                            String[] values = params.get(key);
                            for (int i = 0; i < values.length; i++) {
                                String value = values[i];
                                queryString += key + "=" + value + "&";
                            }
                        }
                        queryString = queryString.substring(0, queryString.length() - 1);
                        RequestUrl += queryString;
                    }
                    StringBuffer sbException = new StringBuffer();
                    StackTraceElement[] stackTraceElements =  eqec.getException().getStackTrace();
                    for (StackTraceElement i: stackTraceElements){
                        sbException.append(i.toString());
                        sbException.append("\n");
                    }
                    MailService mailService = new MailServiceImpl();
                    mailService.sendMail("all@tuotiansudai.com","系统异常报告:用户-"+userId+";URL-"+RequestUrl,sbException.toString());
                }
            } finally {
                it.remove();
            }


//            if(eqec.getException() instanceof ViewExpiredException) {
//                FacesContext context = eqec.getContext();
//                if(!context.isReleased()) {
//                    NavigationHandler navHandler = context.getApplication().getNavigationHandler();
//
//                    try {
//
//                    	//FIXME:session超时挂掉等等，跳转到相应页面，给提示。
////                    	FacesUtil.addErrorMessage("长时间未操作。");
//                    	String originalViewId = context.getViewRoot().getViewId();
//                    	if (originalViewId.endsWith(".xhtml")) {
//                    		String redirectUrl = originalViewId.substring(0, originalViewId.length()-6);
//                    		navHandler.handleNavigation(context, null, redirectUrl+"?faces-redirect=true&expired=true");
//						}
////                        navHandler.handleNavigation(context, null, "pretty:home?faces-redirect=true&expired=true");
//                    }
//                    finally {
//                        it.remove();
//                    }
//                }
//
//            }
        }

        this.wrapped.handle();
    }

}
