package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanFullDto;
import com.tuotiansudai.fudian.dto.request.LoanFullRequestDto;
import com.tuotiansudai.fudian.dto.response.LoanFullContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
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
public class LoanFullService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanFullService.class);

    private static final String LOAN_FULL_DELAY_QUEUE = "BANK_LOAN_FULL_DELAY_QUEUE";

    private static final String LOAN_FULL_KEY = "BANK_LOAN_FULL_{0}";

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final RedissonClient redissonClient;

    private final RedisTemplate<String, String> redisTemplate;

    private final BankClient bankClient;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanFullService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, BankClient bankClient, SignatureHelper signatureHelper, RedissonClient redissonClient, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.redissonClient = redissonClient;
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    @SuppressWarnings(value = "unchecked")
    public BankBaseMessage full(BankLoanFullDto bankLoanFullDto) {
        if (bankLoanFullDto.getTime() > System.currentTimeMillis()) {
            redisTemplate.opsForList().leftPush(LOAN_FULL_DELAY_QUEUE, bankLoanFullDto.toString());
            return new BankBaseMessage(true, null);
        }

        LoanFullRequestDto dto = new LoanFullRequestDto(bankLoanFullDto.getLoginName(),
                bankLoanFullDto.getMobile(),
                bankLoanFullDto.getBankUserName(),
                bankLoanFullDto.getBankAccountNo(),
                bankLoanFullDto.getLoanTxNo(),
                bankLoanFullDto.getLoanOrderNo(),
                bankLoanFullDto.getLoanOrderDate(),
                bankLoanFullDto.getExpectRepayTime());
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan full] sign error, data: {}", bankLoanFullDto);
            return new BankBaseMessage(false, "数据签名失败");
        }

        insertMapper.insertLoanFull(dto);

        redisTemplate.<String, String>opsForHash().put(MessageFormat.format(LOAN_FULL_KEY, dto.getOrderDate()), dto.getOrderNo(),
                gson.toJson(new BankLoanFullMessage(bankLoanFullDto.getLoanId(), bankLoanFullDto.getLoanTxNo(), dto.getOrderNo(), dto.getOrderDate())));

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FULL);

        ResponseDto<LoanFullContentDto> responseDto = (ResponseDto<LoanFullContentDto>) this.callback(responseData);

        if (responseDto == null) {
            return new BankBaseMessage(false, "数据解析失败");
        }

        return new BankBaseMessage(responseDto.isSuccess() && responseDto.getContent().isSuccess(), responseDto.getRetMsg());
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[loan full] data is {}", responseData);

        ResponseDto<LoanFullContentDto> responseDto = (ResponseDto<LoanFullContentDto>) ApiType.LOAN_FULL.getParser().parse(responseData);
        if (responseDto == null) {
            logger.error("[loan full] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        int count = updateMapper.updateLoanFull(responseDto);

        LoanFullContentDto content = responseDto.getContent();
        if (count > 0 && responseDto.isSuccess() && content.isSuccess()) {
            BankLoanFullMessage bankLoanFullMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(MessageFormat.format(LOAN_FULL_KEY, content.getOrderDate()), content.getOrderNo()), BankLoanFullMessage.class);
            bankLoanFullMessage.setStatus(true);
            bankLoanFullMessage.setMessage(responseDto.getRetMsg());
            messageQueueClient.publishMessage(MessageTopic.LoanFullSuccess, bankLoanFullMessage);
        }

        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_FULL.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return false;
        }

        ResponseDto<LoanFullContentDto> responseDto = (ResponseDto<LoanFullContentDto>) ApiType.LOAN_FULL.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }

    @Scheduled(fixedDelay = 1000 * 10, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_FULL_DELAY");
        ListOperations<String, String> listOperations = redisTemplate.opsForList();
        if (lock.tryLock()) {
            try {
                Long size = listOperations.size(LOAN_FULL_DELAY_QUEUE);
                for (long index = 0; index < (size == null ? 0 : size); index++) {
                    BankLoanFullDto bankLoanFullDto = gson.fromJson(listOperations.index(LOAN_FULL_DELAY_QUEUE, -1), BankLoanFullDto.class);
                    if (bankLoanFullDto.getTime() < System.currentTimeMillis()) {
                        BankBaseMessage bankBaseMessage = this.full(bankLoanFullDto);
                        listOperations.rightPop(LOAN_FULL_DELAY_QUEUE);
                        if (bankBaseMessage.isStatus()) {
                            logger.info("[loan full] loan full success, loanId: {}", bankLoanFullDto.getLoanId());
                        } else {
                            logger.error("[loan full] loan full failure, loanId: {}, error: {}", bankLoanFullDto.getLoanId(), bankBaseMessage.getMessage());
                        }
                    } else {
                        listOperations.rightPopAndLeftPush(LOAN_FULL_DELAY_QUEUE, LOAN_FULL_DELAY_QUEUE);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
