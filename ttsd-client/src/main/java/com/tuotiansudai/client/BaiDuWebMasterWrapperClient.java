package com.tuotiansudai.client;

import com.google.common.collect.Lists;
import com.squareup.okhttp.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaiDuWebMasterWrapperClient extends BaseClient {
    static Logger logger = Logger.getLogger(BaiDuWebMasterWrapperClient.class);

    public String executeForBaiDu(List<String> stringList) {
        String requestStr = StringUtils.join(stringList, "\n");
        ResponseBody responseBody = this.newCallForBaiDu(requestStr);
        try {
            return responseBody != null ? responseBody.string() : null;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

    }

    public static void main(String args[]) {

        BaiDuWebMasterWrapperClient baiDuWebMasterWrapperClient = new BaiDuWebMasterWrapperClient();
        String returnString = baiDuWebMasterWrapperClient.executeForBaiDu(Lists.newArrayList("https://tuotiansudai.com/ask/question/111493", "https://tuotiansudai.com/ask/question/111494"));
        System.out.println(returnString);
    }


}
