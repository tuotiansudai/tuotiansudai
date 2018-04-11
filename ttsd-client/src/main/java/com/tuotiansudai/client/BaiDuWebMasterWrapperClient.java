package com.tuotiansudai.client;

import com.squareup.okhttp.ResponseBody;
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

    private static final String BAI_DU_WEB_MASTER_URL = "http://data.zz.baidu.com/urls?site=https://tuotiansudai.com&token=TRCKe3BVZH8842bI";


    public String executeForBaiDu(List<String> stringList) {
        String requestStr = StringUtils.join(stringList, "\n");
        ResponseBody responseBody = this.newCallForBaiDu(BAI_DU_WEB_MASTER_URL, requestStr);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

    }



}
