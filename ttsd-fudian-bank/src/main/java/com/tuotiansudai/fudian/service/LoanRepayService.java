package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanRepayRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanRepayContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
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
public class LoanRepayService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanRepayService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_REPAY;

    private final static String BANK_LOAN_REPAY_MESSAGE_KEY = "BANK_LOAN_REPAY_MESSAGE_{0}";

    private final static String BANK_LOAN_CALLBACK_DATA_KEY = "BANK_LOAN_CALLBACK_DATA_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final LoanCallbackService loanCallbackService;

    private final QueryTradeService queryTradeService;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public LoanRepayService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, LoanCallbackService loanCallbackService, QueryTradeService queryTradeService, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.loanCallbackService = loanCallbackService;
        this.queryTradeService = queryTradeService;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public LoanRepayRequestDto repay(Source source, BankLoanRepayDto bankLoanRepayDto) {
        LoanRepayRequestDto dto = new LoanRepayRequestDto(source, bankLoanRepayDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Repay] failed to sign, data: {}", bankLoanRepayDto);
            return null;
        }

        this.insertMapper.insertLoanRepay(dto);

        BankLoanRepayMessage bankLoanRepayMessage = new BankLoanRepayMessage(bankLoanRepayDto.getLoanId(),
                bankLoanRepayDto.getLoanRepayId(),
                bankLoanRepayDto.getCapital(),
                bankLoanRepayDto.getInterest(),
                bankLoanRepayDto.isNormalRepay(),
                bankLoanRepayDto.getLoginName(),
                bankLoanRepayDto.getMobile(),
                dto.getOrderNo(),
                dto.getOrderDate());
        String bankLoanRepayHistoryKey = MessageFormat.format(BANK_LOAN_REPAY_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.opsForHash().put(bankLoanRepayHistoryKey, dto.getOrderNo(), gson.toJson(bankLoanRepayMessage));
        redisTemplate.expire(bankLoanRepayHistoryKey, 7, TimeUnit.DAYS);

        redisTemplate.opsForValue().set(MessageFormat.format(BANK_LOAN_CALLBACK_DATA_KEY, dto.getOrderNo()), gson.toJson(bankLoanRepayDto), 3, TimeUnit.DAYS);

        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Loan Repay] callback data is {}", responseData);

        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Loan Repay] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Repay] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        return responseDto;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_REPAY_QUERY_LOCK");

        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> loanRepayRequests = selectMapper.selectResponseInOneHour(API_TYPE.name().toLowerCase());

                for (BaseRequestDto loanRepayRequest : loanRepayRequests) {
                    try {
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(loanRepayRequest.getOrderNo(), loanRepayRequest.getOrderDate(), QueryTradeType.LOAN_REPAY);

                        if (query.isSuccess() && "1".equals(query.getContent().getQueryState())) {
                            updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);

                            String message = hashOperations.get(MessageFormat.format(BANK_LOAN_REPAY_MESSAGE_KEY, loanRepayRequest.getOrderDate()), loanRepayRequest.getOrderNo());
                            BankLoanRepayMessage bankLoanRepayMessage = gson.fromJson(message, BankLoanRepayMessage.class);
                            if (bankLoanRepayMessage == null) {
                                continue;
                            }
                            bankLoanRepayMessage.setStatus(true);
                            bankLoanRepayMessage.setMessage(query.getRetMsg());
                            messageQueueClient.sendMessage(MessageQueue.LoanRepay_Success, bankLoanRepayMessage);
                            logger.info("[Repay Status Schedule] repay is success, send message: {}", message);

                            String loanCallbackData = redisTemplate.opsForValue().get(MessageFormat.format(BANK_LOAN_CALLBACK_DATA_KEY, loanRepayRequest.getOrderNo()));
                            loanCallbackService.pushLoanCallbackQueue(gson.fromJson(loanCallbackData, BankLoanRepayDto.class));
                            logger.info("[Repay Status Schedule] repay is success, trigger loan callback: {}", loanCallbackData);
                        }
                    } catch (Exception ex) {
                        logger.error(MessageFormat.format("[Repay Status Schedule] failed to query repay status, orderNo: {0}", loanRepayRequest.getOrderNo()), ex);
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
            ResponseDto<LoanRepayContentDto> responseDto = (ResponseDto<LoanRepayContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryTradeContentDto> queryResponseDto = (ResponseDto<QueryTradeContentDto>) ApiType.QUERY_TRADE.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && "1".equals(queryResponseDto.getContent().getQueryState());
        }

        return null;
    }
}
