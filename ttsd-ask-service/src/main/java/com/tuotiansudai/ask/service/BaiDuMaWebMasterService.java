package com.tuotiansudai.ask.service;

import com.tuotiansudai.client.BaiDuWebMasterWrapperClient;
import com.tuotiansudai.client.SiteMapRedisWrapperClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BaiDuMaWebMasterService {
    private static final Logger logger = LoggerFactory.getLogger(BaiDuMaWebMasterService.class);
    @Autowired
    private BaiDuWebMasterWrapperClient baiDuWebMasterWrapperClient;

    @Autowired
    private SiteMapRedisWrapperClient siteMapRedisWrapperClient;

    private final static String siteMapKey = "ask:sitemap";

    public void sendBaiDuWebMaster(List<String> urlList) {
        try {
            String requestStr = StringUtils.join(urlList, "\n");
            String returnString = baiDuWebMasterWrapperClient.executeForBaiDu(urlList);
            logger.info(String.format("[baiDu:] json:%s", returnString));
            siteMapRedisWrapperClient.set(siteMapKey,String.format("%s\n%s",siteMapRedisWrapperClient.get(siteMapKey), requestStr));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }


    }


}
