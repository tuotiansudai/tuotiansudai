package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankInvestDto;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanInvestContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankLoanInvestMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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
public class LoanInvestService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanInvestService.class);

    private static final ApiType API_TYPE = ApiType.LOAN_INVEST;

    private final static String BANK_LOAN_INVEST_MESSAGE_KEY = "BANK_LOAN_INVEST_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final BankClient bankClient;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final QueryTradeService queryTradeService;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public LoanInvestService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, BankClient bankClient, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, QueryTradeService queryTradeService, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.bankClient = bankClient;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.queryTradeService = queryTradeService;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public LoanInvestRequestDto invest(Source source, BankInvestDto bankInvestDto) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(source, bankInvestDto);
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Invest] failed to sign, data: {}", bankInvestDto);
            return null;
        }

        insertMapper.insertLoanInvest(dto);

        BankLoanInvestMessage bankLoanInvestMessage = new BankLoanInvestMessage(bankInvestDto.getLoanId(),
                bankInvestDto.getLoanName(),
                bankInvestDto.getInvestId(),
                bankInvestDto.getAmount(),
                bankInvestDto.getLoginName(),
                bankInvestDto.getMobile(),
                bankInvestDto.getBankUserName(),
                bankInvestDto.getBankAccountNo(),
                dto.getOrderNo(),
                dto.getOrderDate());
        String bankInvestHistoryKey = MessageFormat.format(BANK_LOAN_INVEST_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.opsForHash().put(bankInvestHistoryKey, dto.getOrderNo(), gson.toJson(bankLoanInvestMessage));
        redisTemplate.expire(bankInvestHistoryKey, 7, TimeUnit.DAYS);

        return dto;
    }

    @SuppressWarnings(value = "unchecked")
    public BankReturnCallbackMessage fastInvest(Source source, BankInvestDto bankInvestDto) {
        LoanInvestRequestDto dto = new LoanInvestRequestDto(source, bankInvestDto);
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Loan Fast Invest] failed to sign, data {}", bankInvestDto);
            return new BankReturnCallbackMessage(false, "数据签名失败", null);
        }

        insertMapper.insertLoanInvest(dto);

        BankLoanInvestMessage bankLoanInvestMessage = new BankLoanInvestMessage(bankInvestDto.getLoanId(),
                bankInvestDto.getLoanName(),
                bankInvestDto.getInvestId(),
                bankInvestDto.getAmount(),
                bankInvestDto.getLoginName(),
                bankInvestDto.getMobile(),
                bankInvestDto.getBankUserName(),
                bankInvestDto.getBankAccountNo(),
                dto.getOrderNo(),
                dto.getOrderDate());

        String bankInvestHistoryKey = MessageFormat.format(BANK_LOAN_INVEST_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankInvestHistoryKey, dto.getOrderNo(), gson.toJson(bankLoanInvestMessage));
        redisTemplate.expire(bankInvestHistoryKey, 7, TimeUnit.DAYS);

        String responseData = bankClient.send(ApiType.LOAN_FAST_INVEST, dto.getRequestData());

        if (!signatureHelper.verifySign(responseData)) {
            logger.error("[Loan Fast Invest] failed to verify sign, request: {}, response: {}", dto.getRequestData(), responseData);
            return new BankReturnCallbackMessage(false, "数据验签失败", null);
        }

        ResponseDto<LoanInvestContentDto> responseDto = (ResponseDto<LoanInvestContentDto>) API_TYPE.getParser().parse(responseData);

        return new BankReturnCallbackMessage(responseDto.isSuccess(), responseDto.getRetMsg(), responseDto.getContent().getOrderNo());
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Loan Invest] callback data is {}", responseData);

        ResponseDto<LoanInvestContentDto> responseDto = (ResponseDto<LoanInvestContentDto>) API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Loan Invest] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Loan Invest] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        return responseDto;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_LOAN_INVEST_QUERY_LOCK");

        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> loanInvestRequests = selectMapper.selectResponseInOneHour(API_TYPE.name().toLowerCase());

                for (BaseRequestDto loanInvestRequest : loanInvestRequests) {
                    try {
                        String bankLoanInvestValue = hashOperations.get(MessageFormat.format(BANK_LOAN_INVEST_MESSAGE_KEY, loanInvestRequest.getOrderDate()), loanInvestRequest.getOrderNo());
                        BankLoanInvestMessage bankLoanInvestMessage = gson.fromJson(bankLoanInvestValue, BankLoanInvestMessage.class);
                        if (bankLoanInvestMessage == null) {
                            continue;
                        }
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(bankLoanInvestMessage.getBankOrderNo(), bankLoanInvestMessage.getBankOrderDate(), QueryTradeType.LOAN_INVEST);

                        if (query.isSuccess() && !"0".equals(query.getContent().getQueryState())) {
                            updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
                            bankLoanInvestMessage.setStatus(true);
                            messageQueueClient.publishMessage(MessageTopic.InvestSuccess, bankLoanInvestMessage);
                            logger.info("[Invest Status Schedule] invest is success, send message: {}", bankLoanInvestMessage);
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
            ResponseDto<LoanInvestContentDto> responseDto = (ResponseDto<LoanInvestContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryTradeContentDto> queryResponseDto = (ResponseDto<QueryTradeContentDto>) ApiType.QUERY_TRADE.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && !"0".equals(queryResponseDto.getContent().getQueryState());
        }

        return null;
    }
}
