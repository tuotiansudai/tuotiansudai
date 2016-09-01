package com.tuotiansudai.coupon.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
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
    public boolean contains(long couponId, String loginName) {
        List<LoanModel> loanModels = loanMapper.findByProductType(LoanStatus.RAISING,Lists.newArrayList(ProductType.EXPERIENCE),ActivityType.NEWBIE);
        if (CollectionUtils.isEmpty(loanModels)) {
            return false;
        }

        for (final LoanModel loanModel : loanModels) {
            List<InvestModel> investModels = investMapper.findByLoanIdAndLoginName(loanModel.getId(), loginName);
            boolean isRepayComplete = Iterators.all(investModels.iterator(), new Predicate<InvestModel>() {
                @Override
                public boolean apply(InvestModel input) {
                    return investRepayMapper.findByInvestIdAndPeriod(input.getId(), loanModel.getPeriods()).getStatus() == RepayStatus.COMPLETE;
                }
            });
            if (!isRepayComplete) {
                return false;
            }
        }
        return true;
    }
}
