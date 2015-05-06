package com.esoft.core.application;

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
            cached = new ArcherExceptionHandler(base.getExceptionHandler());
        }
        
        return cached;
    }
}
