package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.AuthorizationContentDto;
import com.tuotiansudai.fudian.dto.response.QueryUserContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizationService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final static String BANK_AUTHORIZATION_MESSAGE_KEY = "BANK_AUTHORIZATION_MESSAGE_{0}";

    private final static String BANK_AUTHORIZATION_ORDER_KEY = "BANK_AUTHORIZATION_ORDER_{0}";

    private static final ApiType API_TYPE = ApiType.AUTHORIZATION;

    private final SignatureHelper signatureHelper;

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, InsertMapper insertMapper, UpdateMapper updateMapper, SelectMapper selectMapper) {
        this.signatureHelper = signatureHelper;
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public AuthorizationRequestDto auth(Source source, BankBaseDto bankBaseDto, boolean isOpen) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(source, bankBaseDto, isOpen);

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[authorization callback] parse callback data error, data is {}", bankBaseDto);
            return null;
        }

        insertMapper.insertAuthorization(dto);

        BankAuthorizationMessage bankAuthorizationMessage = new BankAuthorizationMessage(
                bankBaseDto.getLoginName(),
                bankBaseDto.getMobile(),
                bankBaseDto.getBankUserName(),
                bankBaseDto.getBankAccountNo(),
                dto.getOrderNo(),
                dto.getOrderDate());

        String bankAuthorizationMessageKey = MessageFormat.format(BANK_AUTHORIZATION_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankAuthorizationMessageKey, dto.getOrderNo(), gson.toJson(bankAuthorizationMessage));
        redisTemplate.expire(bankAuthorizationMessageKey, 7, TimeUnit.DAYS);
        redisTemplate.opsForValue().set(MessageFormat.format(BANK_AUTHORIZATION_ORDER_KEY, dto.getOrderNo()), String.valueOf(isOpen), 7, TimeUnit.DAYS);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[authorization] callback data is {}", responseData);

        ResponseDto<AuthorizationContentDto> responseDto = ApiType.AUTHORIZATION.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[authorization] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[authorization] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        if (responseDto.isSuccess()){
            AuthorizationContentDto contentDto = responseDto.getContent();
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            String message = hashOperations.get(MessageFormat.format(BANK_AUTHORIZATION_MESSAGE_KEY, contentDto.getOrderDate()), contentDto.getOrderNo());
            BankAuthorizationMessage bankAuthorizationMessage = gson.fromJson(message, BankAuthorizationMessage.class);
            bankAuthorizationMessage.setOpen(contentDto.isOpen());
            bankAuthorizationMessage.setAmount(AmountUtils.toCent(contentDto.getAmount()));
            bankAuthorizationMessage.setEndTime(contentDto.getEndTime());
            messageQueueClient.publishMessage(MessageTopic.Authorization, gson.toJson(bankAuthorizationMessage));
            logger.info("[authorization Schedule] authorization is success, send message: {}", message);
        }

        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String isAuthorizationOpen = redisTemplate.opsForValue().get(MessageFormat.format(BANK_AUTHORIZATION_ORDER_KEY, orderNo));

        if (Strings.isNullOrEmpty(isAuthorizationOpen)){
            return null;
        }

        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        String queryResponseData = this.selectMapper.selectQueryResponseData(API_TYPE.name().toLowerCase(), orderNo);

        boolean isOpen = Boolean.valueOf(isAuthorizationOpen);
        if (!Strings.isNullOrEmpty(responseData)) {
            ResponseDto<AuthorizationContentDto> responseDto = (ResponseDto<AuthorizationContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess() && ((responseDto.getContent().isOpen() && isOpen) || (!responseDto.getContent().isOpen() && !isOpen));
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryUserContentDto> queryResponseDto = (ResponseDto<QueryUserContentDto>) ApiType.QUERY_USER.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && ((queryResponseDto.getContent().isAuthorization() && isOpen) || (!queryResponseDto.getContent().isAuthorization() && !isOpen));
        }

        return null;
    }
}
