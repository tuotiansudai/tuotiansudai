package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.CancelCardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.CancelCardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelCardBindService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CancelCardBindService.class);

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public CancelCardBindService(MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public CancelCardBindRequestDto cancel(Source source, BankBaseDto bankBaseDto) {
        CancelCardBindRequestDto dto = new CancelCardBindRequestDto(source, bankBaseDto.getLoginName(), bankBaseDto.getMobile(), bankBaseDto.getBankUserName(), bankBaseDto.getBankAccountNo(), null);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[cancel card bind] sign error, data: {}", bankBaseDto);

            return null;
        }

        insertMapper.insertCancelCardBind(dto);
        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[cancel card bind] data is {}", responseData);

        ResponseDto<CancelCardBindContentDto> responseDto = ApiType.CANCEL_CARD_BIND.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[cancel card bind] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateCancelCardBind(responseDto);

        if (responseDto.isSuccess() && responseDto.getContent().isSuccess()) {
            CancelCardBindContentDto content = responseDto.getContent();
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(responseDto.getContent().getExtMark(), ExtMarkDto.class);
            BankBindCardMessage message = new BankBindCardMessage(extMarkDto.getLoginName(),
                    extMarkDto.getMobile(),
                    content.getUserName(),
                    content.getAccountNo(),
                    content.getBank(),
                    content.getBankCode(),
                    content.getBankAccountNo(),
                    content.getOrderNo(),
                    content.getOrderDate());
            this.messageQueueClient.sendMessage(MessageQueue.UnbindBankCard_Success, message);
        }
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.CANCEL_CARD_BIND.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<CancelCardBindContentDto> responseDto = (ResponseDto<CancelCardBindContentDto>) ApiType.CANCEL_CARD_BIND.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
