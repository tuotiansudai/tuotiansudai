package com.tuotiansudai.paywrapper.ghb.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.exception.PayException;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestBaseOGW;
import com.tuotiansudai.paywrapper.ghb.message.request.RequestMessageContent;
import com.tuotiansudai.paywrapper.service.GHBMessageRecordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GHBClient {

    static Logger logger = Logger.getLogger(GHBClient.class);

    @Autowired
    private GHBMessageRecordService ghbMessageRecordService;

    public <T extends RequestBaseOGW> BaseDto<PayFormDataDto> generateFormData(RequestMessageContent<T> content) throws PayException {
        this.ghbMessageRecordService.saveRequestMessage(content);
        PayFormDataDto payFormDataDto = new PayFormDataDto("url",
                Maps.newHashMap(ImmutableMap.<String, String>builder()
                        .put("transCode", content.getBody().getTranscode())
                        .put("RequestData", content.getFullMessage())
                        .build()));

        return new BaseDto<>(payFormDataDto);
    }


}
