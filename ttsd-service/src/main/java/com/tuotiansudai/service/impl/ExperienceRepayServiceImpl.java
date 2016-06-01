package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.dto.ExperienceRepayNotifyDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceRepayService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
public class ExperienceRepayServiceImpl implements ExperienceRepayService {
    static Logger logger = Logger.getLogger(ExperienceRepayServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponActivationService couponActivationService;

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Override
    public void repay(Date compareDate, Date repayDate) {
        List<LoanModel> loanModels = loanMapper.findByProductType(ProductType.EXPERIENCE);
        if (CollectionUtils.isEmpty(loanModels) || loanModels.get(0).getStatus() != LoanStatus.RAISING) {
            return;
        }
        LoanModel loanModel = loanModels.get(0);
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        //调用时需要保证compareDate是new Date()
        DateTime compareDateTime = new DateTime(compareDate).withTimeAtStartOfDay();
        repayDate = new DateTime(repayDate).withMillisOfSecond(0).toDate();

        List<InvestRepayModel> successInvestRepayModels = Lists.newArrayList();
        for (InvestModel investModel : investModels) {
            if (null == investModel) {
                continue;
            }
            List<InvestRepayModel> investRepayModels;
            try {
                investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            } catch (Exception e) {
                logger.error(MessageFormat.format("[ExperienceRepayService][repay] findByInvestIdAndPeriodAse failed. " +
                        "investId:{0}\nExceptionMessage: {1}", investModel.getId(), e.toString()));
                continue;
            }

            if (CollectionUtils.isEmpty(investModels)) {
                continue;
            }
            for (InvestRepayModel investRepayModel : investRepayModels) {
                if (null == investRepayModel) {
                    continue;
                }
                if (investRepayModel.getStatus() != RepayStatus.REPAYING) {
                    continue;
                }
                try {
                    DateTime repayDateTime = new DateTime(investRepayModel.getRepayDate()).withTimeAtStartOfDay();
                    if (!repayDateTime.equals(compareDateTime)) {
                        continue;
                    }

                    investRepayModel.setActualFee(investRepayModel.getExpectedFee());
                    investRepayModel.setActualInterest(investRepayModel.getExpectedInterest());
                    investRepayModel.setActualRepayDate(repayDate);
                    investRepayModel.setStatus(RepayStatus.COMPLETE);

                    try {
                        investRepayMapper.update(investRepayModel);
                        logger.info("[ExperienceRepayService][repay] invest_repay table updated. investRepayModel: " + investRepayModel.toString());
                    } catch (Exception e) {
                        logger.error(MessageFormat.format("[ExperienceRepayService][repay] update invest_repay table error. " +
                                "should updated investRepayModel: {0}\nExceptionMessage: {1}", investRepayModel.toString(), e.toString()));
                        continue;
                    }
                    try {
                        couponActivationService.assignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.EXPERIENCE_REPAY_SUCCESS), null, null);
                        logger.info("sending experience coupon, loginName:" + investModel.getLoginName());
                    } catch (Exception e) {
                        logger.error(MessageFormat.format("[ExperienceRepayService][repay] invest_repay table has been updated, " +
                                        "but sending coupon failed. assignUserCoupon() loginName: {0}, updated investRepayModel: {1}\nExceptionMessage: {2}",
                                investModel.getLoginName(), investRepayModel.toString(), e.toString()));
                        continue;
                    }
                } catch (Exception e) {
                    logger.error(e.toString());
                    continue;
                }
                successInvestRepayModels.add(investRepayModel);
            }
        }

        this.sendSms(successInvestRepayModels);
    }

    private void sendSms(List<InvestRepayModel> successInvestRepayModels) {
        Map<Long, List<String>> maps = Maps.newHashMap();

        for (InvestRepayModel successInvestRepayModel : successInvestRepayModels) {
            String mobile = userMapper.findByLoginName(investMapper.findById(successInvestRepayModel.getInvestId()).getLoginName()).getMobile();
            if (maps.containsKey(successInvestRepayModel.getActualInterest())) {
                maps.get(successInvestRepayModel.getActualInterest()).add(mobile);
            } else {
                maps.put(successInvestRepayModel.getActualInterest(), Lists.newArrayList(mobile));
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