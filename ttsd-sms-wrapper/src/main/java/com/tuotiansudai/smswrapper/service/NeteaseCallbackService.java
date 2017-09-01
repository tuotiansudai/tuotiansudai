package com.tuotiansudai.smswrapper.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.dto.sms.NeteaseCallbackRequestDto;
import com.tuotiansudai.dto.sms.NeteaseCallbackRequestItemDto;
import com.tuotiansudai.smswrapper.client.CheckSumBuilder;
import com.tuotiansudai.smswrapper.repository.mapper.NeteaseCallbackRequestMapper;
import com.tuotiansudai.smswrapper.repository.model.NeteaseCallbackRequestModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

@Service
public class NeteaseCallbackService {

    private final static Logger logger = Logger.getLogger(NeteaseCallbackService.class);

    private ObjectMapper objectMapper;

    @Value("${sms.netease.appKey}")
    private String appKey;

    @Value("${sms.netease.appSecret}")
    private String appSecret;

    private final NeteaseCallbackRequestMapper neteaseCallbackRequestMapper;

    @Autowired
    public NeteaseCallbackService(NeteaseCallbackRequestMapper neteaseCallbackRequestMapper) {
        this.neteaseCallbackRequestMapper = neteaseCallbackRequestMapper;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public void updateStatus(String curTime, String md5, String checkSum, String body) {
        String verifyMd5 = CheckSumBuilder.encode("md5", body);
        String verifyCheckSum = CheckSumBuilder.getCheckSum(appSecret, verifyMd5, curTime);

        if (!verifyMd5.equals(md5) || !verifyCheckSum.equals(checkSum)) {
            logger.error(MessageFormat.format("request is invalid: md5={0} verifyMd5={1} checkSum={2} verifyCheckSum={3}", md5, verifyMd5, checkSum, verifyCheckSum));
            return;
        }

        try {
            NeteaseCallbackRequestDto neteaseCallbackRequestDto = objectMapper.readValue(body, NeteaseCallbackRequestDto.class);
            if (CollectionUtils.isEmpty(neteaseCallbackRequestDto.getObjects())) {
                return;
            }

            Date callbackTime = new Date();
            for (NeteaseCallbackRequestItemDto item : neteaseCallbackRequestDto.getObjects()) {
                NeteaseCallbackRequestModel model = neteaseCallbackRequestMapper.findByMobileAndSendid(item.getMobile(), item.getSendid());
                if (model != null) {
                    model.setResult(item.getResult());
                    model.setSendTime(item.getSendTime());
                    model.setReportTime(item.getReportTime());
                    model.setSpliced(item.getSpliced());
                    model.setCallbackTime(callbackTime);
                    neteaseCallbackRequestMapper.update(model);
                }
            }
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
