package com.tuotiansudai.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.v2_0.BaseParamDto;
import com.tuotiansudai.spring.CurrentRequest;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MobileAppCurrentRequest {
    private static Logger logger = Logger.getLogger(MobileAppCurrentRequest.class);

    public static String getAppVersion(){
        ObjectMapper objectMapper = new ObjectMapper();
        HttpServletRequest request = CurrentRequest.getCurrentRequest();
        try {
            BufferedRequestWrapper bufferedRequestWrapper = new BufferedRequestWrapper(request);
            BaseParamDto baseParamDto = objectMapper.readValue(bufferedRequestWrapper.getInputStreamString(), BaseParamDto.class);
            return baseParamDto !=null ? baseParamDto.getBaseParam().getAppVersion() : null;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(),e);
            return null;
        }

    }
}
