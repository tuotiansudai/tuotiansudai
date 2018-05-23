package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.*;
import com.tuotiansudai.fudian.message.BankInvestMessage;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.EventMessage;
import com.tuotiansudai.message.PushMessage;
import com.tuotiansudai.message.WeChatMessageNotify;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvestSuccessService {

    private static Logger logger = Logger.getLogger(InvestSuccessService.class);

    private final static int FIRST_INVEST_MAX_TIMES_EACH_MONTH = 3;

    private final InvestMapper investMapper;

    private final LoanMapper loanMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public InvestSuccessService(InvestMapper investMapper, LoanMapper loanMapper, MQWrapperClient mqWrapperClient) {
        this.investMapper = investMapper;
        this.loanMapper = loanMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processInvestSuccess(BankInvestMessage message) {
        long investId = message.getInvestId();

        LoanModel loanModel = loanMapper.findById(message.getLoanId());
        InvestModel investModel = investMapper.findById(investId);

        if (investModel == null || investModel.getStatus() != InvestStatus.WAIT_PAY) {
            logger.warn(MessageFormat.format("[MQ] invest not found or status is incorrect, message: {}", new Gson().toJson(message)));
            return;
        }

        investModel.setBankOrderNo(message.getBankOrderNo());
        investModel.setBankOrderDate(message.getBankOrderDate());
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setTradingTime(new Date());

        this.processInvestAchievement(investModel, loanModel);

        investMapper.update(investModel);
        loanMapper.update(loanModel);

        mqWrapperClient.sendMessage(MessageQueue.AmountTransfer,
                new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, investModel.getLoginName(), investModel.getId(), investModel.getAmount(), UserBillBusinessType.INVEST_SUCCESS));

        //投资成功后发送消息
        this.publishInvestSuccessMessage(investModel);
    }

    private void processInvestAchievement(InvestModel investModel, LoanModel loanModel) {
        if (loanModel.getActivityType() == ActivityType.NEWBIE) {
            return;
        }

        List<InvestModel> successInvestModels = investMapper.findSuccessInvestsByLoanId(investModel.getLoanId());

        if (isFirstInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.FIRST_INVEST);
            loanModel.setFirstInvestAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the FIRST_INVEST of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }

        if (isMaxAmountAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.MAX_AMOUNT);
            loanModel.setMaxAmountAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the MAX_AMOUNT of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }

        if (isLastInvestAchievement(investModel, successInvestModels)) {
            investModel.getAchievements().add(InvestAchievement.LAST_INVEST);
            loanModel.setLastInvestAchievementId(investModel.getId());
            logger.info(MessageFormat.format("{0} get the LAST_INVEST of loan({1})", investModel.getLoginName(), String.valueOf(investModel.getLoanId())));
        }
    }

    private boolean isFirstInvestAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        boolean isFirstInvestAchievementNotExist = successInvestModels.stream().noneMatch(successInvestModel -> successInvestModel.getAchievements().contains(InvestAchievement.FIRST_INVEST));

        int firstInvestAchievementTimes = investMapper.countAchievementTimesByLoginName(investModel.getLoginName(),
                InvestAchievement.FIRST_INVEST,
                new DateTime().withTimeAtStartOfDay().dayOfMonth().withMinimumValue().toDate(),
                new Date());

        return isFirstInvestAchievementNotExist && firstInvestAchievementTimes < FIRST_INVEST_MAX_TIMES_EACH_MONTH;
    }

    private boolean isMaxAmountAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        Optional<InvestModel> maxAmountAchievementInvestOptional = successInvestModels.stream().filter(successInvestModel -> successInvestModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT)).findFirst();

        if (maxAmountAchievementInvestOptional.isPresent()) {
            InvestModel previousMaxAmountInvest = maxAmountAchievementInvestOptional.get();

            long currentInvestorInvestAmount = 0;
            long maxAmountInvestorInvestAmount = 0;

            for (InvestModel successInvestModel : successInvestModels) {
                if (successInvestModel.getLoginName().equalsIgnoreCase(investModel.getLoginName())) {
                    currentInvestorInvestAmount += successInvestModel.getAmount();
                }
                if (successInvestModel.getLoginName().equalsIgnoreCase(previousMaxAmountInvest.getLoginName())) {
                    maxAmountInvestorInvestAmount += successInvestModel.getAmount();
                }
            }

            if (currentInvestorInvestAmount <= maxAmountInvestorInvestAmount) {
                return false;
            }

            previousMaxAmountInvest.getAchievements().remove(InvestAchievement.MAX_AMOUNT);
            investMapper.update(previousMaxAmountInvest);
        }

        return true;
    }

    private boolean isLastInvestAchievement(InvestModel investModel, List<InvestModel> successInvestModels) {
        boolean isLastInvestAchievementNotExist = successInvestModels.stream().noneMatch(successInvestModel -> successInvestModel.getAchievements().contains(InvestAchievement.LAST_INVEST));

        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());

        long successInvestAmount = successInvestModels.stream().mapToLong(InvestModel::getAmount).sum();

        return isLastInvestAchievementNotExist && loanModel.getLoanAmount() == successInvestAmount;
    }

    private void publishInvestSuccessMessage(InvestModel investModel) {
        try {
            //Title:恭喜您成功投资{0}元
            //Content:尊敬的用户，您已成功投资房产/车辆抵押借款{0}元，独乐不如众乐，马上【邀请好友投资】还能额外拿1%现金奖励哦！
            String title = MessageFormat.format(MessageEventType.INVEST_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(investModel.getAmount()));
            String content = MessageFormat.format(MessageEventType.INVEST_SUCCESS.getTitleTemplate(), AmountConverter.convertCentToString(investModel.getAmount()));
            mqWrapperClient.sendMessage(MessageQueue.EventMessage, new EventMessage(MessageEventType.INVEST_SUCCESS,
                    Lists.newArrayList(investModel.getLoginName()),
                    title,
                    content,
                    investModel.getId()
            ));
            mqWrapperClient.sendMessage(MessageQueue.PushMessage, new PushMessage(Lists.newArrayList(investModel.getLoginName()),
                    PushSource.ALL,
                    PushType.INVEST_SUCCESS,
                    title,
                    AppUrl.MESSAGE_CENTER_LIST));

            mqWrapperClient.sendMessage(MessageQueue.WeChatMessageNotify, new WeChatMessageNotify(investModel.getLoginName(),
                    WeChatMessageType.INVEST_SUCCESS,
                    investModel.getId()));
        } catch (Exception e) {
            logger.error("invest success wechat message notify send fail", e);
        }
    }
}
