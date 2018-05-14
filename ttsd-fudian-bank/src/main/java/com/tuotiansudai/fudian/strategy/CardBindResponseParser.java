package com.tuotiansudai.fudian.strategy;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.dto.response.CardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class CardBindResponseParser implements ResponseParserInterface<CardBindContentDto> {

    private static Logger logger = LoggerFactory.getLogger(CardBindResponseParser.class);

    @Override
    public ResponseDto<CardBindContentDto> parse(String data) {
        try {
            ResponseDto<CardBindContentDto> dto = gson.fromJson(data, new TypeToken<ResponseDto<CardBindContentDto>>() {
            }.getType());
            dto.setReqData(data);
            return dto;
        } catch (JsonSyntaxException e) {
            logger.error(MessageFormat.format("[Response Parse] deserialize error, data is {0}", data), e);
        }

        return null;
    }
}
