package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.LoanFullContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class LoanFullResponseParser implements ResponseParserInterface<LoanFullContentDto> {

    private static Logger logger = LoggerFactory.getLogger(LoanFullResponseParser.class);

    @Override
    public ResponseDto<LoanFullContentDto> parse(String data) {
        try {
            ResponseDto<LoanFullContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<LoanFullContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
