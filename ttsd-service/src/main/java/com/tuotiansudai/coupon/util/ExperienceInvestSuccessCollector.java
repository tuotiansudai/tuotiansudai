package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceInvestSuccessCollector implements UserCollector {

    @Autowired
    private InvestMapper investMapper;

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

        return loanModels.stream().anyMatch(loanModel -> CollectionUtils.isNotEmpty(investMapper.findByLoanIdAndLoginName(loanModel.getId(), userModel.getLoginName())));
    }
}
