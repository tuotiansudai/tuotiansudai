package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.CardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.CardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardBindService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CardBindService.class);

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public CardBindService(MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public CardBindRequestDto bind(Source source, String loginName, String mobile, String bankUserName, String bankAccountNo) {
        CardBindRequestDto dto = new CardBindRequestDto(source, loginName, mobile, bankUserName, bankAccountNo);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[card bind] sign error, userName: {}, accountNo: {}", bankUserName, bankAccountNo);

            return null;
        }


        insertMapper.insertCardBind(dto);
        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[card bind] data is {}", responseData);

        ResponseDto<CardBindContentDto> responseDto = ApiType.CARD_BIND.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[card bind] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateCardBind(responseDto);

        if (responseDto.isSuccess()) {
            CardBindContentDto content = responseDto.getContent();
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(responseDto.getContent().getExtMark(), ExtMarkDto.class);

            this.messageQueueClient.publishMessage(MessageTopic.BindBankCard,
                    Maps.newHashMap(ImmutableMap.<String, String>builder()
                            .put("loginName", extMarkDto.getLoginName())
                            .put("mobile", extMarkDto.getMobile())
                            .put("bankUserName", content.getUserName())
                            .put("bankAccountNo", content.getAccountNo())
                            .put("bank", content.getBank())
                            .put("bankCode", content.getBankCode())
                            .put("cardNumber", content.getBankAccountNo())
                            .put("bankOrderNo", content.getOrderNo())
                            .put("bankOrderDate", content.getOrderDate())
                            .build()));
        }
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.CARD_BIND.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<CardBindContentDto> responseDto = (ResponseDto<CardBindContentDto>) ApiType.CARD_BIND.getParser().parse(responseData);

        return responseDto.getContent().isSuccess();
    }

}
