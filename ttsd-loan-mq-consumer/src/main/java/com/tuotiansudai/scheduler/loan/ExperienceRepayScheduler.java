package com.tuotiansudai.scheduler.loan;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.sms.ExperienceRepayNotifyDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
public class ExperienceRepayScheduler {

    static Logger logger = LoggerFactory.getLogger(ExperienceRepayScheduler.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Scheduled(cron = "0 0 16 * * ?", zone = "Asia/Shanghai")
    public void repay() {
        Date repayDate = new Date();
        logger.info(MessageFormat.format("[Experience Repay] starting at {0}", new Date().toString()));

        List<LoanModel> loanModels = loanMapper.findByProductType(LoanStatus.RAISING, Lists.newArrayList(ProductType.EXPERIENCE), ActivityType.NEWBIE);

        List<InvestRepayModel> repaySuccessInvestRepayModels = Lists.newArrayList();

        for (LoanModel loanModel : loanModels) {
            if (loanModel.getStatus() != LoanStatus.RAISING) {
                logger.error(MessageFormat.format("[Experience Repay] experience loan({0}) status({1}) is not RAISING", String.valueOf(loanModel.getId()), loanModel.getStatus()));
                continue;
            }

            List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

            for (InvestModel investModel : investModels) {
                List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
                for (InvestRepayModel investRepayModel : investRepayModels) {
                    DateTime investRepayDate = new DateTime(investRepayModel.getRepayDate());

                    if (investRepayModel.getStatus() == RepayStatus.REPAYING && new DateTime(repayDate).withTimeAtStartOfDay().isEqual(investRepayDate.withTimeAtStartOfDay())) {
                        try {
                            investRepayModel.setActualFee(investRepayModel.getExpectedFee());
                            investRepayModel.setActualInterest(investRepayModel.getExpectedInterest());
                            investRepayModel.setRepayAmount(investRepayModel.getExpectedInterest() - investRepayModel.getExpectedFee());
                            investRepayModel.setActualRepayDate(repayDate);
                            investRepayModel.setStatus(RepayStatus.COMPLETE);
                            investRepayMapper.update(investRepayModel);
                            logger.info(MessageFormat.format("[Experience Repay] invest({0}) repay is success", String.valueOf(investModel.getId())));

                            couponAssignmentService.asyncAssignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.EXPERIENCE_REPAY_SUCCESS));
                            logger.info(MessageFormat.format("[Experience Repay] assign invest({0}) user coupon is success", String.valueOf(investModel.getId())));

                            repaySuccessInvestRepayModels.add(investRepayModel);
                        } catch (Exception e) {
                            logger.error(MessageFormat.format("[Experience Repay] invest repay failed", String.valueOf(investModel.getId())));
                        }
                    }
                }
            }
        }

        this.sendSms(repaySuccessInvestRepayModels);

        logger.info(MessageFormat.format("[Experience Repay] done at {0}", new Date().toString()));
    }

    private void sendSms(List<InvestRepayModel> successInvestRepayModels) {
        Map<Long, List<String>> maps = Maps.newHashMap();

        for (InvestRepayModel successInvestRepayModel : successInvestRepayModels) {
            String mobile = userMapper.findByLoginName(investMapper.findById(successInvestRepayModel.getInvestId()).getLoginName()).getMobile();
            if (maps.containsKey(successInvestRepayModel.getActualInterest())) {
                maps.get(successInvestRepayModel.getRepayAmount()).add(mobile);
            } else {
                maps.put(successInvestRepayModel.getRepayAmount(), Lists.newArrayList(mobile));
            }
        }

        for (Long repayAmount : maps.keySet()) {
            ExperienceRepayNotifyDto experienceRepayNotifyDto = new ExperienceRepayNotifyDto();
            experienceRepayNotifyDto.setRepayAmount(AmountConverter.convertCentToString(repayAmount));
            experienceRepayNotifyDto.setMobiles(maps.get(repayAmount));
            smsWrapperClient.sendExperienceRepayNotify(experienceRepayNotifyDto);
        }
    }
}