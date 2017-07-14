package com.tuotiansudai.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CurrentWrapperClient extends BaseClient {

    static Logger logger = Logger.getLogger(CurrentWrapperClient.class);

    @Value("${current.rest.host}")
    protected String host;

    @Value("${current.rest.port}")
    protected String port;

    private final static String CURRENT_WITHDRAW = "/withdraw/";


    public CurrentWrapperClient() {
        this.okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        this.okHttpClient.setWriteTimeout(10, TimeUnit.SECONDS);
    }



    @Override
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String getApplicationContext() {
        return "";
    }

}
