package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.CardBindRequestDto;
import com.tuotiansudai.fudian.dto.response.CardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
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

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final MessageQueueClient messageQueueClient;

    @Autowired
    public CardBindService(MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public CardBindRequestDto bind(String loginName, String mobile, String bankUserName, String bankAccountNo) {
        CardBindRequestDto dto = new CardBindRequestDto(loginName, mobile, bankUserName, bankAccountNo);
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

        updateMapper.updateCardBind(responseDto);
        responseDto.setReqData(responseData);

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
}
