package com.tuotiansudai.spring;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class MyAsyncUncaughtExceptionHandler extends SimpleAsyncUncaughtExceptionHandler {
    private final static Logger logger = Logger.getLogger(MyAsyncUncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.error(ex.getLocalizedMessage(), ex);
    }
}
