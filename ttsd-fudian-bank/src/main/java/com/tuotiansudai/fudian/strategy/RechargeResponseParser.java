package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class RechargeResponseParser implements ResponseParserInterface<RechargeContentDto> {

    private static Logger logger = LoggerFactory.getLogger(RechargeResponseParser.class);

    @Override
    public ResponseDto<RechargeContentDto> parse(String data) {
        try {
            ResponseDto<RechargeContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<RechargeContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
