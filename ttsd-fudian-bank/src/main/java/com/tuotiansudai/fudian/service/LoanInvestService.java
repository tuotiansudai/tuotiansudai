package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankInvestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestStatus;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanInvestContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectRequestMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankInvestMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.BankClient;
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
public class LoanInvestService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanInvestService.class);

    private final static String BANK_INVEST_HISTORY_KEY_TEMPLATE = "BANK_INVEST_HISTORY_{0}";

    private final MessageQueueClient messageQueueClient;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final QueryTradeService queryTradeService;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final BankClient bankClient;

    private final SelectRequestMapper selectRequestMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanInvestService(MessageQueueClient messageQueueClient, RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, QueryTradeService queryTradeService, SignatureHelper signatureHelper, BankClient bankClient, InsertMapper insertMapper, UpdateMapper updateMapper, SelectRequestMapper selectRequestMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.messageQueueClient = messageQueueClient;
        this.redisTemplate = redisTemplate;
        this.signatureHelper = signatureHelper;
        this.bankClient = bankClient;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectRequestMapper = selectRequestMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
        this.redissonClient = redissonClient;
        this.queryTradeService = queryTradeService;
    }

    public LoanInvestRequestDto invest(Source source, BankInvestDto bankInvestDto) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(source,
                bankInvestDto.getLoginName(),
                bankInvestDto.getMobile(),
                bankInvestDto.getBankUserName(),
                bankInvestDto.getBankAccountNo(),
                AmountUtils.toYuan(bankInvestDto.getAmount()),
                bankInvestDto.getLoanTxNo(),
                ApiType.LOAN_INVEST);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan invest] sign error, data {}", bankInvestDto);
            return null;
        }

        insertMapper.insertLoanInvest(dto);

        String bankInvestHistoryKey = MessageFormat.format(BANK_INVEST_HISTORY_KEY_TEMPLATE, dto.getOrderDate());
        redisTemplate.opsForHash().put(bankInvestHistoryKey, dto.getOrderNo(),
                gson.toJson(new BankInvestMessage(bankInvestDto.getLoanId(),
                        bankInvestDto.getLoanName(),
                        bankInvestDto.getInvestId(),
                        bankInvestDto.getAmount(),
                        bankInvestDto.getLoginName(),
                        bankInvestDto.getMobile(),
                        bankInvestDto.getBankUserName(),
                        bankInvestDto.getBankAccountNo(),
                        dto.getOrderNo(),
                        dto.getOrderDate())));
        redisTemplate.expire(bankInvestHistoryKey, 30, TimeUnit.DAYS);

        return dto;
    }

    public ResponseDto fastInvest(Source source, String loginName, String mobile, String userName, String accountNo, String amount, String award, String loanTxNo) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(source, loginName, mobile, userName, accountNo, amount, loanTxNo, ApiType.LOAN_FAST_INVEST);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[loan fast invest] sign error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        insertMapper.insertLoanInvest(dto);

        String responseData = bankClient.send(dto.getRequestData(), ApiType.LOAN_FAST_INVEST);

        if (Strings.isNullOrEmpty(responseData)) {
            logger.error("[loan fast invest] send error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[loan fast invest] verify sign error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        ResponseDto responseDto = ApiType.LOAN_FAST_INVEST.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan fast invest] parse response error, userName: {}, accountNo: {}, amount: {}, award: {}, loanTxNo: {}",
                    userName, accountNo, amount, award, loanTxNo);
            return null;
        }

        this.updateMapper.updateLoanInvest(responseDto, LoanInvestStatus.BANK_RESPONSED);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[loan invest callback] data is {}", responseData);

        ResponseDto<LoanInvestContentDto> responseDto = (ResponseDto<LoanInvestContentDto>) ApiType.LOAN_INVEST.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan invest callback] parse callback data error, response data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        int count = updateMapper.updateLoanInvest(responseDto, LoanInvestStatus.BANK_RESPONSED);
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();

        if (responseDto.isSuccess() && count > 0) {
            String message = operations.get(MessageFormat.format(BANK_INVEST_HISTORY_KEY_TEMPLATE, responseDto.getContent().getOrderDate()),
                    responseDto.getContent().getOrderNo());
            if (Strings.isNullOrEmpty(message)) {
                logger.error("[loan invest callback] callback is success, but queue message is not found, response data is {}", responseData);
                return responseDto;
            }
            messageQueueClient.publishMessage(MessageTopic.InvestSuccess, message);
        }

        return responseDto;
    }

    @Scheduled(fixedDelay = 1000 * 10, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_INVEST_HISTORY");
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        if (lock.tryLock()) {
            try {
                List<LoanInvestRequestDto> loanInvestRequests = selectRequestMapper.selectNoInvestResponseInOneHour();

                for (LoanInvestRequestDto loanInvestRequest : loanInvestRequests) {
                    String bankInvestValue = hashOperations.get(MessageFormat.format(BANK_INVEST_HISTORY_KEY_TEMPLATE, loanInvestRequest.getOrderDate()), loanInvestRequest.getOrderNo());
                    BankInvestMessage bankInvestMessage = gson.fromJson(bankInvestValue, BankInvestMessage.class);

                    ResponseDto<QueryTradeContentDto> query = queryTradeService.query(bankInvestMessage.getLoginName(), bankInvestMessage.getMobile(), bankInvestMessage.getBankOrderNo(), bankInvestMessage.getBankOrderDate(), QueryTradeType.LOAN_INVEST);
                    if (query.isSuccess() && !"0".equals(query.getContent().getQueryState())) {
                        if (updateMapper.updateLoanInvest(query, LoanInvestStatus.MANUAL_QUERIED) > 0) {
                            logger.error("[Invest Status Schedule] invest is success, but bank response is not found, should send topic: {}", bankInvestValue);
                        }
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
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_INVEST.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanInvestContentDto> responseDto = (ResponseDto<LoanInvestContentDto>) ApiType.LOAN_INVEST.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
