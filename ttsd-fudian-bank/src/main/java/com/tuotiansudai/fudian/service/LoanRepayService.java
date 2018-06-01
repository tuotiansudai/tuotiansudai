package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.request.LoanRepayRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanRepayContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.*;
import com.tuotiansudai.fudian.message.BankLoanRepayMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.BankClient;
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
public class LoanRepayService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanRepayService.class);

    private final static String BANK_LOAN_REPAY_MESSAGE_KEY = "BANK_LOAN_REPAY_MESSAGE_{0}";

    private final static String BANK_LOAN_CALLBACK_DATA_KEY = "BANK_LOAN_CALLBACK_DATA_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final RedissonClient redissonClient;

    private final SignatureHelper signatureHelper;

    private final QueryTradeService queryTradeService;

    private final LoanCallbackService loanCallbackService;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final BankClient bankClient;

    private final SelectRequestMapper selectRequestMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanRepayService(MessageQueueClient messageQueueClient, RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, SignatureHelper signatureHelper, BankClient bankClient, QueryTradeService queryTradeService, LoanCallbackService loanCallbackService, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectRequestMapper selectRequestMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.messageQueueClient = messageQueueClient;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.queryTradeService = queryTradeService;
        this.loanCallbackService = loanCallbackService;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectRequestMapper = selectRequestMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public LoanRepayRequestDto repay(Source source, BankLoanRepayDto bankLoanRepayDto) {
        LoanRepayRequestDto dto = new LoanRepayRequestDto(source,
                bankLoanRepayDto.getLoginName(),
                bankLoanRepayDto.getMobile(),
                bankLoanRepayDto.getBankUserName(),
                bankLoanRepayDto.getBankAccountNo(),
                bankLoanRepayDto.getLoanTxNo(),
                AmountUtils.toYuan(bankLoanRepayDto.getCapital()),
                AmountUtils.toYuan(bankLoanRepayDto.getInterest()),
                ApiType.LOAN_REPAY);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Repay] sign error, data: {}", bankLoanRepayDto);
            return dto;
        }

        this.insertMapper.insertLoanRepay(dto);

        String bankLoanRepayHistoryKey = MessageFormat.format(BANK_LOAN_REPAY_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.opsForHash().put(bankLoanRepayHistoryKey, dto.getOrderNo(),
                gson.toJson(new BankLoanRepayMessage(bankLoanRepayDto.getLoanId(),
                        bankLoanRepayDto.getLoanRepayId(),
                        bankLoanRepayDto.getCapital(),
                        bankLoanRepayDto.getInterest(),
                        bankLoanRepayDto.isNormalRepay(),
                        bankLoanRepayDto.getLoginName(),
                        bankLoanRepayDto.getMobile(),
                        bankLoanRepayDto.getBankUserName(),
                        bankLoanRepayDto.getBankAccountNo(),
                        dto.getOrderNo(),
                        dto.getOrderDate())));
        redisTemplate.expire(bankLoanRepayHistoryKey, 3, TimeUnit.DAYS);

        redisTemplate.opsForValue().set(MessageFormat.format(BANK_LOAN_CALLBACK_DATA_KEY, dto.getOrderNo()), gson.toJson(bankLoanRepayDto), 3, TimeUnit.DAYS);

        return dto;
    }

    public ResponseDto fastRepay(Source source, String loginName, String mobile, String userName, String accountNo, String loanTxNo, String capital, String interest) {
        LoanRepayRequestDto dto = new LoanRepayRequestDto(source, loginName, mobile, userName, accountNo, loanTxNo, capital, interest, ApiType.LOAN_FAST_REPAY);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan fast repay] sign error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        insertMapper.insertLoanRepay(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FAST_REPAY);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan fast repay] send error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan fast repay] verify sign error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_FAST_REPAY.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan fast repay] parse response error, userName: {}, accountNo: {}, loanTxNo: {}, capital: {}, interest: {}",
                    userName, accountNo, loanTxNo, capital, interest);
            return null;
        }

        this.updateMapper.updateLoanRepay(responseDto);
        return responseDto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateLoanRepay(responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Loan Repay Callback] data is {}", responseData);


        ResponseDto<LoanRepayContentDto> responseDto = (ResponseDto<LoanRepayContentDto>) ApiType.LOAN_REPAY.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Loan Repay Callback] parse callback data error, response is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        int count = updateMapper.updateLoanRepay(responseDto);

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Repay Callback] loan repay is failure, response is {}", responseData);
            return responseDto;
        }

        LoanRepayContentDto content = responseDto.getContent();

        if (count == 0) {
            return responseDto;
        }

        String message = redisTemplate.<String, String>opsForHash().get(MessageFormat.format(BANK_LOAN_REPAY_MESSAGE_KEY, content.getOrderDate()), content.getOrderNo());
        BankLoanRepayMessage bankLoanRepayMessage = gson.fromJson(message, BankLoanRepayMessage.class);
        bankLoanRepayMessage.setStatus(true);
        bankLoanRepayMessage.setMessage(responseDto.getRetMsg());
        messageQueueClient.sendMessage(MessageQueue.LoanRepay_Success, bankLoanRepayMessage);
        loanCallbackService.pushLoanCallbackQueue(gson.fromJson(
                redisTemplate.opsForValue().get(MessageFormat.format(BANK_LOAN_CALLBACK_DATA_KEY, content.getOrderNo())),
                BankLoanRepayDto.class));

        logger.info("[Loan Repay Callback] loan repay is success, send message: {}", message);

        return responseDto;
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_REPAY_QUERY");
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        if (lock.tryLock()) {
            try {
                List<LoanRepayRequestDto> loanRepayRequests = selectRequestMapper.selectNoLoanRepayResponseInOneHour();

                for (LoanRepayRequestDto loanRepayRequest : loanRepayRequests) {
                    try {
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(loanRepayRequest.getOrderNo(), loanRepayRequest.getOrderDate(), QueryTradeType.LOAN_REPAY);

                        if (query.isSuccess() && "1".equals(query.getContent().getQueryState())) {
                            updateMapper.updateLoanRepayQuery(query);

                            String message = hashOperations.get(MessageFormat.format(BANK_LOAN_REPAY_MESSAGE_KEY, loanRepayRequest.getOrderDate()), loanRepayRequest.getOrderNo());
                            if (Strings.isNullOrEmpty(message)) {
                                logger.error("[Repay Status Schedule] loan repay status is success, but queue message is not found, response: {}", query.getReqData());
                                continue;
                            }
                            BankLoanRepayMessage bankLoanRepayMessage = gson.fromJson(message, BankLoanRepayMessage.class);
                            bankLoanRepayMessage.setStatus(true);
                            bankLoanRepayMessage.setMessage(query.getRetMsg());
                            messageQueueClient.sendMessage(MessageQueue.LoanRepay_Success, bankLoanRepayMessage);
                            logger.info("[Repay Status Schedule] repay is success, send message: {}", message);
                            String loanCallbackData = redisTemplate.opsForValue().get(MessageFormat.format(BANK_LOAN_CALLBACK_DATA_KEY, loanRepayRequest.getOrderNo()));
                            loanCallbackService.pushLoanCallbackQueue(gson.fromJson(loanCallbackData, BankLoanRepayDto.class));
                            logger.info("[Repay Status Schedule] repay is success, trigger loan callback: {}", loanCallbackData);
                        }
                    } catch (Exception ex) {
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
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_REPAY.name(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanRepayContentDto> responseDto = (ResponseDto<LoanRepayContentDto>) ApiType.LOAN_REPAY.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
