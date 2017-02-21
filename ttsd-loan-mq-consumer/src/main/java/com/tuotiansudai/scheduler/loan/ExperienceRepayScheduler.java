package com.tuotiansudai.scheduler.loan;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.InvestRepayModel;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ExperienceRepayScheduler {

    private static Logger logger = LoggerFactory.getLogger(ExperienceRepayScheduler.class);

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

//    @Scheduled(cron = "0 0 16 * * ?", zone = "Asia/Shanghai")
    @Scheduled(cron = "0 0/5 * * * ?", zone = "Asia/Shanghai")
    public void evaluateExperienceRepay() {
        logger.info("[ExperienceRepayScheduler] start...");

        List<InvestRepayModel> investRepayModels = investRepayMapper.findByLoanId(1);
        investRepayModels.stream()
                .filter(investRepayModel -> new DateTime(investRepayModel.getRepayDate()).isBefore(new DateTime().plusDays(1).withTimeAtStartOfDay()))
                .collect(Collectors.toList())
                .forEach(investRepayModel -> {
                    String loginName = investMapper.findById(investRepayModel.getInvestId()).getLoginName();
                    InvestInfo investInfo = new InvestInfo();
                    investInfo.setLoginName(loginName);
                    mqWrapperClient.sendMessage(MessageQueue.InvestSuccess_ExperienceRepay, new InvestSuccessMessage(investInfo, null, null));
                    logger.info("[ExperienceRepayScheduler] {} experience invest repay", loginName);
                });

        logger.info("[ExperienceRepayScheduler] done");
    }
}
