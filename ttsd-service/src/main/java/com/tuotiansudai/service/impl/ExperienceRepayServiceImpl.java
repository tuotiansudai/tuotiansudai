package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ExperienceRepayService;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Component
public class ExperienceRepayServiceImpl implements ExperienceRepayService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponActivationService couponActivationService;

    @Override
    public void repay(Date compareDate, Date repayDate) {
        List<LoanModel> loanModels = loanMapper.findByProductType(ProductType.EXPERIENCE);
        if (CollectionUtils.isEmpty(loanModels) || loanModels.get(0).getStatus() != LoanStatus.RAISING) {
            return;
        }
        LoanModel loanModel = loanModels.get(0);
        List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loanModel.getId());

        //调用时需要保证compareDate是new Date()
        DateTime todayDateTime = new DateTime(compareDate).withTimeAtStartOfDay();
        repayDate = new DateTime(repayDate).withMillisOfSecond(0).toDate();

        for (InvestModel investModel : investModels) {
            List<InvestRepayModel> investRepayModels = investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId());
            if (CollectionUtils.isEmpty(investModels)) {
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
                investRepayMapper.update(investRepayModel);
                couponActivationService.assignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.EXPERIENCE_REPAY_SUCCESS), null, null);
            }
        }
    }
}
