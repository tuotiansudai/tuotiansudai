package com.tuotiansudai.service.impl;

import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.NewbieExperienceService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class NewbieExperienceServiceImpl implements NewbieExperienceService {
    @Autowired
    LoanMapper loanMapper;

    @Autowired
    InvestMapper investMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    @Autowired
    CouponActivationService couponActivationService;

    public void sendCouplesDaily(Date compareDate, Date repayDate) {
        List<LoanModel> loanModels = loanMapper.findByProductType(ProductType.EXPERIENCE);
        if (null == loanModels || loanModels.size() == 0) {
            return;
        }
        LoanModel loanModel = loanModels.get(0);
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        //调用时需要保证compareDate是new Date()
        DateTime todayDateTime = new DateTime(compareDate).withTimeAtStartOfDay();
        repayDate = new DateTime(repayDate).withMillisOfSecond(0).toDate();

        for (InvestModel investModel : investModels) {
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            if (null == investRepayModels || investRepayModels.size() == 0) {
                continue;
            }
            for (InvestRepayModel investRepayModel : investRepayModels) {
                if (investRepayModel.getStatus() == RepayStatus.COMPLETE) {
                    continue;
                }

                DateTime repayDateTime = new DateTime(investRepayModel.getRepayDate()).withTimeAtStartOfDay();

                if (!repayDateTime.equals(todayDateTime)) {
                    continue;
                }
                investRepayModel.setActualFee(investRepayModel.getExpectedFee());
                investRepayModel.setActualInterest(investRepayModel.getExpectedInterest());
                investRepayModel.setActualRepayDate(repayDate);
                investRepayModel.setStatus(RepayStatus.COMPLETE);
                List<UserGroup> userGroups = new ArrayList<>();
                userGroups.add(UserGroup.EXPERIENCE_INVESTOR);
                couponActivationService.assignUserCoupon(investModel.getLoginName(), userGroups, null, null);
                investRepayMapper.update(investRepayModel);
            }
        }
    }
}
