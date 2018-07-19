package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.BankLoanRepayInvestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackInvestItemRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCallbackContentDto;
import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.fudian.util.BankOrderNoGenerator;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LoanCallbackService {

    private static Logger logger = LoggerFactory.getLogger(LoanCallbackService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_CALLBACK;

    private static final String BANK_LOAN_CALLBACK_QUEUE = "BANK_LOAN_CALLBACK_QUEUE";

    private final RedisTemplate<String, String> redisTemplate;

    private final BankClient bankClient;

    private final SignatureHelper signatureHelper;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;


    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanCallbackService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, MessageQueueClient messageQueueClient, BankClient bankClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.bankClient = bankClient;
        this.redissonClient = redissonClient;
        this.signatureHelper = signatureHelper;
        this.messageQueueClient = messageQueueClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.redisTemplate = redisTemplate;
    }

    void pushLoanCallbackQueue(BankLoanRepayDto bankLoanRepayDto) {
        redisTemplate.opsForList().leftPushAll(BANK_LOAN_CALLBACK_QUEUE, gson.toJson(bankLoanRepayDto));
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void sendSchedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_CALLBACK_QUEUE_LOCK");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        if (lock.tryLock()) {
            try {
                String value = listOperations.rightPop(BANK_LOAN_CALLBACK_QUEUE);
                if (Strings.isNullOrEmpty(value)) {
                    return;
                }
                logger.info("[Loan Callback Send Schedule] loan callback data: {}", value);
                this.loanCallback(gson.fromJson(value, BankLoanRepayDto.class));
            } finally {
                lock.unlock();
            }
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void loanCallback(BankLoanRepayDto bankLoanRepayDto) {
        Map<String, BankLoanCallbackMessage> message_map = Maps.newHashMap();
        List<BankLoanRepayInvestDto> bankLoanRepayInvests = bankLoanRepayDto.getBankLoanRepayInvests();
        List<LoanCallbackInvestItemRequestDto> loanCallbackInvestItemRequests = bankLoanRepayInvests
                .stream()
                .map(bankLoanRepayInvest -> {
                    LoanCallbackInvestItemRequestDto loanCallbackInvestItemRequest = new LoanCallbackInvestItemRequestDto(BankOrderNoGenerator.generate(redisTemplate), bankLoanRepayInvest);
                    message_map.put(loanCallbackInvestItemRequest.getOrderNo(),
                            new BankLoanCallbackMessage(bankLoanRepayInvest.getInvestId(),
                                    bankLoanRepayInvest.getInvestRepayId(),
                                    bankLoanRepayInvest.getCapital(),
                                    bankLoanRepayInvest.getInterest(),
                                    bankLoanRepayInvest.getDefaultInterest(),
                                    bankLoanRepayInvest.getInterestFee(),
                                    loanCallbackInvestItemRequest.getOrderNo(),
                                    loanCallbackInvestItemRequest.getOrderDate(),
                                    bankLoanRepayDto.isNormalRepay()));
                    return loanCallbackInvestItemRequest;
                }).collect(Collectors.toList());

        LoanCallbackRequestDto loanCallbackRequestDto = new LoanCallbackRequestDto(bankLoanRepayDto.getLoginName(), bankLoanRepayDto.getMobile(), bankLoanRepayDto.getLoanTxNo(), loanCallbackInvestItemRequests);

        signatureHelper.sign(API_TYPE, loanCallbackRequestDto);

        if (Strings.isNullOrEmpty(loanCallbackRequestDto.getRequestData())) {
            logger.error("[Loan Callback] failed to sign, data: {}", gson.toJson(loanCallbackRequestDto));
            return;
        }

        insertMapper.insertLoanCallback(loanCallbackRequestDto);
        loanCallbackRequestDto.getInvestList().forEach(loanCallbackInvestItemRequest -> loanCallbackInvestItemRequest.setLoanCallbackId(loanCallbackRequestDto.getId()));
        insertMapper.insertLoanCallbackInvestItems(loanCallbackRequestDto.getInvestList());

        String responseData = bankClient.send(API_TYPE, loanCallbackRequestDto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Loan Callback] failed to verify sign, data: {}, response: {}", gson.toJson(loanCallbackRequestDto), responseData);
            return;
        }

        ResponseDto<LoanCallbackContentDto> responseDto = (ResponseDto<LoanCallbackContentDto>) API_TYPE.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[Loan Callback] failed to parse response, request: {}, response: {}", gson.toJson(loanCallbackRequestDto), responseData);
            return;
        }

        this.updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        this.updateMapper.updateLoanCallbackInvestItems(responseDto.getContent().getInvestList());

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Callback] response is not success, request: {}, response: {}", gson.toJson(loanCallbackRequestDto), responseData);
            return;
        }

        for (LoanCallbackInvestItemContentDto loanCallbackInvestItemContentDto : responseDto.getContent().getInvestList()) {
            if (loanCallbackInvestItemContentDto.isSuccess()) {
                logger.info("[Loan Callback] invest repay success, send message: {}", gson.toJson(message_map.get(loanCallbackInvestItemContentDto.getOrderNo())));
                messageQueueClient.sendMessage(MessageQueue.LoanCallback_Success, message_map.get(loanCallbackInvestItemContentDto.getOrderNo()));
            } else {
                logger.error("[Loan Callback] invest repay failure, request: {}, response: {}", gson.toJson(loanCallbackRequestDto), responseData);
            }
        }
    }
}
