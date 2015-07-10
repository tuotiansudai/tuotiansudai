package com.esoft.core.application;

import com.esoft.core.util.SpringBeanUtil;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

public class ArcherExceptionHandlerFactory extends ExceptionHandlerFactory {

    private ExceptionHandlerFactory base;
    
    private ArcherExceptionHandler cached;
    
    public ArcherExceptionHandlerFactory(ExceptionHandlerFactory base) {
        this.base = base;
    }
    
    @Override
    public ExceptionHandler getExceptionHandler() {
        if(cached == null) {
            this.cached = (ArcherExceptionHandler) SpringBeanUtil.getBeanByName("archerExceptionHandler");
            cached.setWrapped(base.getExceptionHandler());
        }
        
        return cached;
    }
}
