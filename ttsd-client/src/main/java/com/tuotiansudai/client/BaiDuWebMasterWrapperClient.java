package com.tuotiansudai.client;

import com.google.common.collect.Lists;
import com.squareup.okhttp.ResponseBody;
import com.tuotiansudai.dto.Environment;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaiDuWebMasterWrapperClient extends BaseClient {
    static Logger logger = Logger.getLogger(BaiDuWebMasterWrapperClient.class);

    @Value("baidu.web.master.url")
    private String baiDuWebMasterUrl;


    public String executeForBaiDu(List<String> stringList) {
        String requestStr = StringUtils.join(stringList, "\n");
        ResponseBody responseBody = this.newCallForBaiDu(baiDuWebMasterUrl, requestStr);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

    }



}
