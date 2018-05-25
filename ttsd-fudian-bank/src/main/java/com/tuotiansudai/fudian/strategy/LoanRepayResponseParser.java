package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.LoanRepayContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class LoanRepayResponseParser implements ResponseParserInterface<LoanRepayContentDto> {

    private static Logger logger = LoggerFactory.getLogger(LoanRepayResponseParser.class);

    @Override
    public ResponseDto<LoanRepayContentDto> parse(String data) {
        try {
            ResponseDto<LoanRepayContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<LoanRepayContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
