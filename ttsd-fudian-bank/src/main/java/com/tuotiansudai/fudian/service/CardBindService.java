package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.CardBindRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardBindService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CardBindService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public CardBindService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public CardBindRequestDto bind(String userName, String accountNo) {
        CardBindRequestDto dto = new CardBindRequestDto(userName, accountNo);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[card bind] sign error, userName: {}, accountNo: {}", userName, accountNo);

            return null;
        }


        insertMapper.insertCardBind(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[card bind] data is {}", responseData);

        ResponseDto responseDto = ApiType.CARD_BIND.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[card bind] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateCardBind(responseDto);
        return responseDto;
    }
}
