package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanFullDto;
import com.tuotiansudai.fudian.dto.request.LoanFullRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanFullContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBaseMessage;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.BankClient;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class LoanFullService implements NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanFullService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_FULL;

    private static final String BANK_LOAN_FULL_DELAY_QUEUE = "BANK_LOAN_FULL_DELAY_QUEUE";

    private static final String BANK_LOAN_FULL_MESSAGE_KEY = "BANK_LOAN_FULL_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final BankClient bankClient;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public LoanFullService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, BankClient bankClient, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, QueryTradeService queryTradeService, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.bankClient = bankClient;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage full(BankLoanFullDto bankLoanFullDto) {
        if (bankLoanFullDto.getTriggerTime() > System.currentTimeMillis()) {
            redisTemplate.opsForList().leftPush(BANK_LOAN_FULL_DELAY_QUEUE, bankLoanFullDto.toString());
            return new BankBaseMessage(true, null);
        }

        LoanFullRequestDto dto = new LoanFullRequestDto(bankLoanFullDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Full] failed to sign, data: {}", bankLoanFullDto);
            return new BankBaseMessage(false, "签名失败");
        }

        insertMapper.insertLoanFull(dto);

        String bankLoanFullMessageKey = MessageFormat.format(BANK_LOAN_FULL_MESSAGE_KEY, dto.getOrderDate());
        BankLoanFullMessage bankLoanFullMessage = new BankLoanFullMessage(bankLoanFullDto.getLoanId(), bankLoanFullDto.getLoanTxNo(), dto.getOrderNo(), dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankLoanFullMessageKey, dto.getOrderNo(), gson.toJson(bankLoanFullMessage));

        String responseData = bankClient.send(API_TYPE, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.warn("[Loan Full] failed to verify sign, data: {}, response: {}", bankLoanFullDto, responseData);
            return new BankBaseMessage(false, "验签失败");
        }

        ResponseDto<LoanFullContentDto> responseDto = this.notifyCallback(responseData);

        if (responseDto == null) {
            return new BankBaseMessage(false, "解析银行数据失败");
        }

        return new BankBaseMessage(responseDto.isSuccess() && responseDto.getContent().isSuccess(), responseDto.getRetMsg());
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto<LoanFullContentDto> notifyCallback(String responseData) {
        logger.info("[Loan Full] callback data is {}", responseData);

        ResponseDto responseDto = ApiType.LOAN_FULL.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Loan Full] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Full] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        int count = updateMapper.updateNotifyResponseData(ApiType.LOAN_FULL.name().toLowerCase(), responseDto);

        LoanFullContentDto content = ((ResponseDto<LoanFullContentDto>) responseDto).getContent();
        if (count > 0 && responseDto.isSuccess() && content.isSuccess()) {
            BankLoanFullMessage bankLoanFullMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(MessageFormat.format(BANK_LOAN_FULL_MESSAGE_KEY, content.getOrderDate()), content.getOrderNo()), BankLoanFullMessage.class);
            bankLoanFullMessage.setStatus(true);
            bankLoanFullMessage.setMessage(responseDto.getRetMsg());
            messageQueueClient.publishMessage(MessageTopic.LoanFullSuccess, bankLoanFullMessage);
            logger.info("[Loan Full] loan full is success, send message: {}", bankLoanFullMessage);
        }

        return responseDto;
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void delaySchedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_FULL_DELAY_QUEUE_LOCK");

        if (lock.tryLock()) {
            try {
                ListOperations<String, String> listOperations = redisTemplate.opsForList();
                Long size = listOperations.size(BANK_LOAN_FULL_DELAY_QUEUE);
                for (long index = 0; index < (size == null ? 0 : size); index++) {
                    BankLoanFullDto bankLoanFullDto = gson.fromJson(listOperations.index(BANK_LOAN_FULL_DELAY_QUEUE, -1), BankLoanFullDto.class);
                    if (bankLoanFullDto.getTriggerTime() < System.currentTimeMillis()) {
                        this.full(bankLoanFullDto);
                        listOperations.rightPop(BANK_LOAN_FULL_DELAY_QUEUE);
                    } else {
                        listOperations.rightPopAndLeftPush(BANK_LOAN_FULL_DELAY_QUEUE, BANK_LOAN_FULL_DELAY_QUEUE);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
