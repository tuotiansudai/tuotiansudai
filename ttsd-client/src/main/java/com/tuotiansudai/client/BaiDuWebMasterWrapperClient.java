package com.tuotiansudai.client;

import com.google.common.base.Strings;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BaiDuWebMasterWrapperClient extends BaseClient{
    static Logger logger = Logger.getLogger(BaiDuWebMasterWrapperClient.class);

    protected String executeForBaiDu(String requestStr) {
        RequestBody requestBody = RequestBody.create(text, !Strings.isNullOrEmpty(requestStr) ? requestStr : "");
        Request request = new Request.Builder()
                .url(baiDuWebMasterUrl)
                .method("POST", requestBody)
                .addHeader("User-Agent", "curl/7.12.1")
                .addHeader("Host", "data.zz.baidu.com")
                .addHeader("Content-Type", "text/plain; charset=UTF-8")
                .addHeader("Content-Length", "83")
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }

        return null;
    }





}
