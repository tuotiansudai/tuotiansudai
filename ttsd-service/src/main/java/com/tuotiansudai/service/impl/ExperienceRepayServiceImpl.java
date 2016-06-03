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
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
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
    public void repay(Date repayDate) {
        logger.error(MessageFormat.format("[Experience Repay] starting at {0}", repayDate.toString()));

        List<LoanModel> loanModels = loanMapper.findByProductType(ProductType.EXPERIENCE);

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
                    DateTime investRepayDate = new DateTime(investRepayModel.getRepayDate()).withTimeAtStartOfDay();

                    if (investRepayModel.getStatus() == RepayStatus.REPAYING && new DateTime(repayDate).isEqual(investRepayDate)) {
                        try {
                            investRepayModel.setActualFee(investRepayModel.getExpectedFee());
                            investRepayModel.setActualInterest(investRepayModel.getExpectedInterest());
                            investRepayModel.setActualRepayDate(repayDate);
                            investRepayModel.setStatus(RepayStatus.COMPLETE);
                            investRepayMapper.update(investRepayModel);
                            logger.debug(MessageFormat.format("[Experience Repay] invest({0}) repay is success", String.valueOf(investModel.getId())));

                            couponActivationService.assignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.EXPERIENCE_REPAY_SUCCESS), null, null);
                            logger.debug(MessageFormat.format("[Experience Repay] assign invest({0}) user coupon is success", String.valueOf(investModel.getId())));

                            repaySuccessInvestRepayModels.add(investRepayModel);
                        } catch (Exception e) {
                            logger.error(MessageFormat.format("[Experience Repay] invest repay failed", String.valueOf(investModel.getId())));
                        }
                    }
                }
            }
        }

        this.sendSms(repaySuccessInvestRepayModels);
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