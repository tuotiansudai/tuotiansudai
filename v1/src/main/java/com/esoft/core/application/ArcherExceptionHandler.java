package com.esoft.core.application;

import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import com.esoft.core.jsf.util.FacesUtil;

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
            ExceptionQueuedEvent event = it.next();
            ExceptionQueuedEventContext eqec = event.getContext();
            eqec.getException().printStackTrace();
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
