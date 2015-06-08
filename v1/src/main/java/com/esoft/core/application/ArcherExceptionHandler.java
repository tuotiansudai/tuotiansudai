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
                FacesContext context = eqec.getContext();
                LoginUserInfo loginUserInfo = new LoginUserInfo();
                String userId = loginUserInfo.getLoginUserId();
                HttpServletRequest request = (HttpServletRequest)context.getCurrentInstance().getExternalContext().getRequest();
                String RequestUrl = request.getRequestURL().toString();
                StringBuffer sbException = new StringBuffer();
                sbException.append("\n");
                if (request.getMethod().equals("GET")) {
                    RequestUrl += "?"+request.getQueryString();
                } else {
                    Map<String, String[]> params = request.getParameterMap();
                    sbException.append("请求参数为：");
                    sbException.append("\n");
                    for (String key : params.keySet()) {
                        String[] values = params.get(key);
                        for (int i = 0; i < values.length; i++) {
                            String value = values[i];
                            sbException.append(key + "=" + value + ";");
                            sbException.append("\n");
                        }
                    }
                }
                sbException.append("******************************************************************************************************");
                sbException.append("\n");
                StackTraceElement[] stackTraceElements =  eqec.getException().getCause().getStackTrace();
                sbException.append(eqec.getException().getCause().toString()+"\n");
                for (StackTraceElement i: stackTraceElements){
                    sbException.append(i.toString());
                    sbException.append("\n");
                }
                if (eqec.getException().getCause().getCause()!=null) {
                    sbException.append("******************************************************************************************************");
                    sbException.append("\n");
                    sbException.append("Caused by:"+eqec.getException().getCause().getCause().toString()+"\n");
                    StackTraceElement[] stackTraceElementsCause = eqec.getException().getCause().getCause().getStackTrace();
                    for (StackTraceElement i: stackTraceElementsCause){
                        sbException.append(i.toString());
                        sbException.append("\n");
                    }
                }
                if (eqec.getException().getCause().getCause().getCause()!=null) {
                    sbException.append("******************************************************************************************************");
                    sbException.append("\n");
                    sbException.append("Caused by:"+eqec.getException().getCause().getCause().getCause().toString()+"\n");
                    StackTraceElement[] stackTraceElementsCause = eqec.getException().getCause().getCause().getCause().getStackTrace();
                    for (StackTraceElement i: stackTraceElementsCause){
                        sbException.append(i.toString());
                        sbException.append("\n");
                    }
                }
                MailService mailService = new MailServiceImpl();
                mailService.sendMail("all@tuotiansudai.com","系统异常报告:用户-"+userId+";"+request.getMethod()+"-"+RequestUrl,sbException.toString());
                throw new FacesException(sbException.toString());
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
