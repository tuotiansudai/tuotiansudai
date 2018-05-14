package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.dto.response.WithdrawContentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class WithdrawResponseParser implements ResponseParserInterface<WithdrawContentDto> {

    private static Logger logger = LoggerFactory.getLogger(WithdrawResponseParser.class);

    @Override
    public ResponseDto<WithdrawContentDto> parse(String data) {
        try {
            ResponseDto<WithdrawContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<WithdrawContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
