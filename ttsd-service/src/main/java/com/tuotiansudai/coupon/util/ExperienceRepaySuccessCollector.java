package com.tuotiansudai.coupon.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceRepaySuccessCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }

    @Override
    public boolean contains(CouponModel couponModel, UserModel userModel) {
        if (userModel == null) {
            return false;
        }

        List<LoanModel> loanModels = loanMapper.findByProductType(LoanStatus.RAISING, Lists.newArrayList(ProductType.EXPERIENCE), ActivityType.NEWBIE);

        List<InvestModel> investModels = Lists.newArrayList();

        loanModels.stream().forEach(loanModel -> investModels.addAll(investMapper.findByLoanIdAndLoginName(loanModel.getId(), userModel.getLoginName())));

        List<InvestRepayModel> investRepayModels = Lists.newArrayList();

        investModels.forEach(investModel -> investRepayModels.addAll(investRepayMapper.findByInvestIdAndPeriodAsc(investModel.getId())));

        return CollectionUtils.isNotEmpty(investRepayModels) && investRepayModels.stream().allMatch(investRepayModel -> investRepayModel.getStatus() == RepayStatus.COMPLETE);
    }
}
