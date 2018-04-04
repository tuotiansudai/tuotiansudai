package com.tuotiansudai.client;

import com.squareup.okhttp.OkHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaiDuWebMasterWrapperClient {
    static Logger logger = Logger.getLogger(BaiDuWebMasterWrapperClient.class);

    @Autowired
    private OkHttpClient okHttpClient;




}
