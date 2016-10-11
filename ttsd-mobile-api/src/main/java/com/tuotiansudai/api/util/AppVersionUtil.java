package com.tuotiansudai.api.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.api.security.BufferedRequestWrapper;
import com.tuotiansudai.spring.CurrentRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class AppVersionUtil {
    private static Logger logger = Logger.getLogger(AppVersionUtil.class);

    private static String serverAppVersion;

    private final static int low = -1;

    private final static int equal = 0;

    private final static int high = 1;

    static {
        if (StringUtils.isEmpty(serverAppVersion)) {
            try {
                Resource resourceService = new ClassPathResource("/ttsd-env.properties");
                Properties properties = PropertiesLoaderUtils.loadProperties(resourceService);
                if (properties != null) {
                    serverAppVersion = properties.getProperty("mobile.app.version");
                }
            } catch (IOException e) {
                logger.error("ttsd-env.properties 不存在!");
                logger.error(e.getLocalizedMessage(), e);
            }
        }

    }

    public static int compareVersion() {
        String clientAppVersion = getAppVersion();
        List<String> clientAppVersionList = Lists.newArrayList(clientAppVersion.split("\\."));
        List<String> serverAppVersionList = Lists.newArrayList(serverAppVersion.split("\\."));
        int maxLength = Math.max(clientAppVersionList.size(), serverAppVersionList.size());

        int clientWeight = 0;
        int serverWeight = 0;

        for (int i = 0; i < clientAppVersionList.size(); i++) {
            clientWeight += Integer.parseInt(clientAppVersionList.get(i)) * Math.pow(10, maxLength - i);
        }
        for (int i = 0; i < serverAppVersionList.size(); i++) {
            serverWeight += Integer.parseInt(serverAppVersionList.get(i)) * Math.pow(10, maxLength - i);
        }

        if (clientWeight < serverWeight) {
            return low;
        } else if (clientWeight == serverWeight) {
            return equal;
        } else {
            return high;
        }

    }

    private static String getAppVersion() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        HttpServletRequest request = CurrentRequest.getCurrentRequest();
        try {
            BufferedRequestWrapper bufferedRequestWrapper = new BufferedRequestWrapper(request);
            BaseParamDto baseParamDto = objectMapper.readValue(bufferedRequestWrapper.getInputStreamString(), BaseParamDto.class);
            return baseParamDto != null ? baseParamDto.getBaseParam().getAppVersion() : "0.0.0";
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return "0.0.0";
        }

    }

}
