package com.esoft.core.application;

import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.jdp2p.message.service.MailService;
import com.esoft.jdp2p.message.service.SendCloudMailService;
import com.esoft.jdp2p.message.service.impl.MailServiceImpl;
import com.esoft.jdp2p.message.service.impl.SendCloudMailServiceImpl;
import com.ttsd.util.CommonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
@Service
public class ArcherExceptionHandler extends ExceptionHandlerWrapper {

    @Resource
    SendCloudMailService sendCloudMailService;

    private ExceptionHandler wrapped;

    public void setWrapped(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

//    public ArcherExceptionHandler(ExceptionHandler wrapped) {
//        this.wrapped = wrapped;
//    }
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
                Enumeration headerNames = request.getHeaderNames();
                String RequestUrl = request.getRequestURL().toString();
                StringBuffer exceptionStringBuffer = new StringBuffer();
                exceptionStringBuffer.append("headers：");
                exceptionStringBuffer.append("\n");
                while (headerNames.hasMoreElements()) {
                    String key = (String) headerNames.nextElement();
                    String value = request.getHeader(key);
                    exceptionStringBuffer.append(key + "=" + value + ";");
                    exceptionStringBuffer.append("\n");
                }
                exceptionStringBuffer.append("\n");
                if (request.getMethod().equals("GET")) {
                    RequestUrl += "?"+request.getQueryString();
                } else {
                    Map<String, String[]> params = request.getParameterMap();
                    exceptionStringBuffer.append("请求参数为：");
                    exceptionStringBuffer.append("\n");
                    for (String key : params.keySet()) {
                        String[] values = params.get(key);
                        for (int i = 0; i < values.length; i++) {
                            String value = values[i];
                            exceptionStringBuffer.append(key + "=" + value + ";");
                            exceptionStringBuffer.append("\n");
                        }
                    }
                }
                exceptionStringBuffer.append("\n");
                Throwable throwable = eqec.getException().getCause();
                int flag = 0;
                while (throwable != null) {
                    if (flag != 0) {
                        exceptionStringBuffer.append("Caused by:"+throwable.toString()+"\n");
                    } else {
                        exceptionStringBuffer.append(throwable.toString()+"\n");
                    }
                    StackTraceElement[] stackTraceElementsCause = throwable.getStackTrace();
                    for (StackTraceElement i: stackTraceElementsCause){
                        exceptionStringBuffer.append(i.toString());
                        exceptionStringBuffer.append("\n");
                    }
                    exceptionStringBuffer.append("\n");
                    throwable = throwable.getCause();
                    flag += 1;
                }
                if (!CommonUtils.isDevEnvironment("environment")) {
                    sendCloudMailService.sendMailException(CommonUtils.administratorEmailAddress(), "系统异常报告:用户-" + userId + ";" + request.getMethod() + "-" + RequestUrl, exceptionStringBuffer.toString());
                }
                throw new RuntimeException(exceptionStringBuffer.toString());
            }

            finally {
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
