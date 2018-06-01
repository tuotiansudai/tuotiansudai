package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackInvestItemRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCallbackRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanCallbackContentDto;
import com.tuotiansudai.fudian.dto.response.LoanCallbackInvestItemContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanCallbackMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.fudian.util.OrderIdGenerator;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LoanCallbackService {

    private static Logger logger = LoggerFactory.getLogger(LoanCallbackService.class);

    private static final String BANK_LOAN_CALLBACK_QUEUE = "BANK_LOAN_CALLBACK_QUEUE";

    private static final String BANK_LOAN_CALLBACK_MESSAGE_KEY = "BANK_LOAN_CALLBACK_MESSAGE_{0}";

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

    @SuppressWarnings(value = "unchecked")
    private void loanCallback(LoanCallbackRequestDto loanCallbackRequestDto) {
        signatureHelper.sign(loanCallbackRequestDto);

        if (Strings.isNullOrEmpty(loanCallbackRequestDto.getRequestData())) {
            logger.error("[Loan Callback] sign error, data: {}", gson.toJson(loanCallbackRequestDto));
            return;
        }

        insertMapper.insertLoanCallback(loanCallbackRequestDto);
        loanCallbackRequestDto.getInvestList().forEach(loanCallbackInvestItemRequest -> loanCallbackInvestItemRequest.setLoanCallbackId(loanCallbackRequestDto.getId()));
        insertMapper.insertLoanCallbackInvestItems(loanCallbackRequestDto.getInvestList());

        String responseData = bankClient.send(loanCallbackRequestDto.getRequestData(), ApiType.LOAN_CALLBACK);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[Loan Callback] send error, request data: {}", loanCallbackRequestDto.getRequestData());
            return;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Loan Callback] verify response sign error, request: {}, response: {}", loanCallbackRequestDto.getRequestData(), responseData);
            return;
        }

        ResponseDto<LoanCallbackContentDto> responseDto = (ResponseDto<LoanCallbackContentDto>) ApiType.LOAN_CALLBACK.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[Loan Callback] parse response error, request: {}, response: {}", loanCallbackRequestDto.getRequestData(), responseData);
            return;
        }

        this.updateMapper.updateLoanCallback(responseDto);
        this.updateMapper.updateLoanCallbackInvestItems(responseDto.getContent().getInvestList());

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Callback] response is not success, request: {}, response: {}", loanCallbackRequestDto.getRequestData(), responseData);
            return;
        }

        for (LoanCallbackInvestItemContentDto loanCallbackInvestItemContentDto : responseDto.getContent().getInvestList()) {
            HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
            if (loanCallbackInvestItemContentDto.isSuccess()) {
                String message = hashOperations.get(MessageFormat.format(BANK_LOAN_CALLBACK_MESSAGE_KEY, loanCallbackInvestItemContentDto.getOrderDate()), loanCallbackInvestItemContentDto.getOrderNo());
                logger.info("[Loan Callback] invest repay success, request: {}, response: {}。send message: {}", loanCallbackRequestDto.getRequestData(), responseData, message);
                messageQueueClient.sendMessage(MessageQueue.LoanCallback_Success, message);
            } else {
                logger.error("[Loan Callback] invest repay failure, request: {}, response: {}", loanCallbackRequestDto.getRequestData(), responseData);
            }
        }
    }

    @Scheduled(fixedDelay = 1000 * 2, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void sendSchedule() {
        RLock lock = redissonClient.getLock(BANK_LOAN_CALLBACK_QUEUE);
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        if (lock.tryLock()) {
            try {
                String value = listOperations.rightPop(BANK_LOAN_CALLBACK_QUEUE);
                if (Strings.isNullOrEmpty(value)) {
                    return;
                }
                logger.info("[Loan Callback Send Schedule] data: {}", value);

                this.loanCallback(gson.fromJson(value, LoanCallbackRequestDto.class));
            } finally {
                lock.unlock();
            }
        }
    }

    void pushLoanCallbackQueue(BankLoanRepayDto bankLoanRepayDto) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        List<LoanCallbackInvestItemRequestDto> loanCallbackInvestItemRequests = bankLoanRepayDto.getBankLoanRepayInvests()
                .stream()
                .map(bankLoanRepayInvest -> {
                    LoanCallbackInvestItemRequestDto loanCallbackInvestItemRequest = new LoanCallbackInvestItemRequestDto(AmountUtils.toYuan(bankLoanRepayInvest.getCapital()),
                            AmountUtils.toYuan(bankLoanRepayInvest.getInterest()),
                            AmountUtils.toYuan(bankLoanRepayInvest.getInterestFee()),
                            bankLoanRepayInvest.getBankUserName(),
                            bankLoanRepayInvest.getBankAccountNo(),
                            bankLoanRepayInvest.getInvestOrderNo(),
                            bankLoanRepayInvest.getInvestOrderDate(),
                            OrderIdGenerator.generate(redisTemplate));

                    hashOperations.put(MessageFormat.format(BANK_LOAN_CALLBACK_MESSAGE_KEY, loanCallbackInvestItemRequest.getOrderDate()),
                            loanCallbackInvestItemRequest.getOrderNo(),
                            gson.toJson(new BankLoanCallbackMessage(bankLoanRepayInvest.getInvestId(),
                                    bankLoanRepayInvest.getInvestRepayId(),
                                    bankLoanRepayInvest.getCapital(),
                                    bankLoanRepayInvest.getInterest(),
                                    bankLoanRepayInvest.getDefaultInterest(),
                                    bankLoanRepayInvest.getInterestFee(),
                                    loanCallbackInvestItemRequest.getOrderNo(),
                                    loanCallbackInvestItemRequest.getInvestOrderDate(),
                                    bankLoanRepayDto.isNormalRepay())));
                    redisTemplate.expire(MessageFormat.format(BANK_LOAN_CALLBACK_MESSAGE_KEY, loanCallbackInvestItemRequest.getOrderDate()), 3, TimeUnit.DAYS);
                    return loanCallbackInvestItemRequest;
                }).collect(Collectors.toList());

        LoanCallbackRequestDto loanCallbackRequestDto = new LoanCallbackRequestDto(bankLoanRepayDto.getLoginName(),
                bankLoanRepayDto.getMobile(),
                bankLoanRepayDto.getLoanTxNo(),
                loanCallbackInvestItemRequests);
        redisTemplate.opsForList().leftPushAll(BANK_LOAN_CALLBACK_QUEUE, gson.toJson(loanCallbackRequestDto));
    }
}
