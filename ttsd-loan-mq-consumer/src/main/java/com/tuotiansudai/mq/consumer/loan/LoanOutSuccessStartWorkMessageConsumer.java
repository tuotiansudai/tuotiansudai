package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.SystemBillDetailTemplate;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@Component
public class LoanOutSuccessStartWorkMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessStartWorkMessageConsumer.class);

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    public static final String START_WORK_CASH_KEY = "START_WORK_CASH_KEY:{0}:{1}";

    private final static List<String> FRIDAY_TIME = Lists.newArrayList("2018-03-02", "2018-03-09", "2018-03-16", "2018-03-23", "2018-03-30");

    private final int lifeSecond = 180 * 24 * 60 * 60;

    private final double ratio = 0.005D;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_StartWorkActivity;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_StartWorkActivity receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_StartWorkActivity receive message is empty");
            return;
        }
        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_StartWorkActivity loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_StartWorkActivity json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        List<InvestModel> list = investMapper.findSuccessInvestsByLoanId(loanOutInfo.getLoanId());
        LoanModel loanModel = loanMapper.findById(loanOutInfo.getLoanId());
        Map<String, Long> map = list.stream().filter(i->FRIDAY_TIME.contains(new DateTime(i.getTradingTime()).toString("yyyy-MM-dd"))).collect(groupingBy(InvestModel::getLoginName, summingLong(InvestModel::getAmount)));

        for(Map.Entry<String, Long>  entry: map.entrySet()){
            String key = MessageFormat.format(START_WORK_CASH_KEY, entry.getKey(), String.valueOf(loanModel.getId()));
            if (!redisWrapperClient.exists(key)){
                long sendCash = (long) (entry.getValue() * loanModel.getProductType().getDuration() / 360 * ratio);
                try {
                    sendCashPrize(entry.getKey(), loanModel.getId(), sendCash);
                } catch (Exception e) {
                    logger.error("[LoanOutSuccess_StartWorkActivity] user:{}, loanId:{}, sendCash:{} is send fail.",
                            entry.getKey(), loanModel.getId(), sendCash);
                }
            }
        }
    }

    public void sendCashPrize(String loginName, long loanId, long sendCash){
        logger.info("send start work activity prize begin, loginName:{}, loanId:{}, sendCash:{}", loginName, loanId, sendCash);
        String key = MessageFormat.format(START_WORK_CASH_KEY, loginName, String.valueOf(loanId));
        TransferCashDto transferCashDto = new TransferCashDto(loginName, String.valueOf(IdGenerator.generate()), String.valueOf(sendCash),
                UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.CASH_START_WORK_DETAIL_TEMPLATE);
        try {
            BaseDto<PayDataDto> response = payWrapperClient.transferCash(transferCashDto);
            if (response.getData().getStatus()) {
                logger.info("send start work activity prize success, loginName:{}, loanId:{}, cash:{}", loginName, loanId, sendCash);
                redisWrapperClient.setex(key, lifeSecond, "success");
                return;
            }
        } catch (Exception e) {
            logger.error("send start work activity prize fail, loginName:{}, loanId:{}, cash{}", loginName, loanId, sendCash);
        }
        redisWrapperClient.setex(key,  lifeSecond, "fail");
        smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("【惊喜不重样加息不打烊活动放款】用户:{0}, 标的:{1}, 获得现金:{2}, 发送现金失败, 业务处理异常", loginName, String.valueOf(loanId), String.valueOf(sendCash))));
        logger.info("send cash snowball activity prize end, loginName:{}, loanId:{}, sendCash:{}", loginName, loanId, sendCash);
    }

}
