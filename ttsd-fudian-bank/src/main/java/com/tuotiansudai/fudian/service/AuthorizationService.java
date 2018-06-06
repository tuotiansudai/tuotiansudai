package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.AuthorizationContentDto;
import com.tuotiansudai.fudian.dto.response.QueryUserContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizationService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final static String BANK_AUTHORIZATION_MESSAGE_KEY = "BANK_AUTHORIZATION_MESSAGE_{0}";

    private static final ApiType API_TYPE = ApiType.AUTHORIZATION;

    private final SignatureHelper signatureHelper;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final QueryUserService queryUserService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, MessageQueueClient messageQueueClient, InsertMapper insertMapper, UpdateMapper updateMapper, SelectMapper selectMapper, QueryUserService queryUserService) {
        this.signatureHelper = signatureHelper;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.messageQueueClient = messageQueueClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
        this.queryUserService = queryUserService;
    }

    public AuthorizationRequestDto auth(Source source, BankBaseDto bankBaseDto) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(source, bankBaseDto);

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

        return responseDto;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_AUTHORIZATION_QUERY_LOCK");

        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> authorizationRequests = selectMapper.selectResponseInOneHour(API_TYPE.name().toLowerCase());

                for (BaseRequestDto authorizationRequest : authorizationRequests) {
                    try {
                        String message = hashOperations.get(MessageFormat.format(BANK_AUTHORIZATION_MESSAGE_KEY, authorizationRequest.getOrderDate()), authorizationRequest.getOrderNo());
                        BankAuthorizationMessage bankAuthorizationMessage = gson.fromJson(message, BankAuthorizationMessage.class);
                        if (bankAuthorizationMessage == null) {
                            continue;
                        }
                        ResponseDto<QueryUserContentDto> query = queryUserService.query(bankAuthorizationMessage.getBankUserName(), bankAuthorizationMessage.getBankAccountNo());
                        if (query.isSuccess() && query.getContent().isAuthorization()) {
                            messageQueueClient.publishMessage(MessageTopic.Authorization, gson.toJson(bankAuthorizationMessage));
                            logger.info("[authorization Schedule] authorization is success, send message: {}", message);
                        }
                    } catch (JsonSyntaxException ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {

        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        String queryResponseData = this.selectMapper.selectQueryResponseData(API_TYPE.name().toLowerCase(), orderNo);

        if (!Strings.isNullOrEmpty(responseData)) {
            ResponseDto<AuthorizationContentDto> responseDto = (ResponseDto<AuthorizationContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess() && responseDto.getContent().isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryUserContentDto> queryResponseDto = (ResponseDto<QueryUserContentDto>) ApiType.QUERY_USER.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && queryResponseDto.getContent().isAuthorization();
        }

        return null;
    }
}
