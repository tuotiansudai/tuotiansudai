package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.AuthorizationContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.ReturnUpdateMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final SignatureHelper signatureHelper;

    private final MessageQueueClient messageQueueClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, MessageQueueClient messageQueueClient, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.messageQueueClient = messageQueueClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public AuthorizationRequestDto auth(Source source, String loginName, String mobile, String userName, String accountNo) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(source, loginName, mobile, userName, accountNo, null);

        signatureHelper.sign(dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[authorization] sign error, userName: {}, accountNo: {}", userName, accountNo);

            return null;
        }

        insertMapper.insertAuthorization(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateAuthorization(responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[authorization] data is {}", responseData);

        ResponseDto<AuthorizationContentDto> responseDto = ApiType.AUTHORIZATION.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[authorization] parse callback data error, data is {}", responseData);
            return null;
        }

        updateMapper.updateAuthorization(responseDto);
        responseDto.setReqData(responseData);

        if (responseDto.isSuccess()) {
            AuthorizationContentDto authorizationContentDto = responseDto.getContent();
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(authorizationContentDto.getExtMark(), ExtMarkDto.class);
            BankAuthorizationMessage message = new BankAuthorizationMessage(extMarkDto.getLoginName(), extMarkDto.getMobile(), authorizationContentDto.getUserName(), authorizationContentDto.getAccountNo(), authorizationContentDto.getOrderNo(), authorizationContentDto.getOrderDate());
            messageQueueClient.publishMessage(MessageTopic.Authorization, gson.toJson(message));
        }

        return responseDto;
    }

    @Override
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.AUTHORIZATION.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto responseDto = ApiType.AUTHORIZATION.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
