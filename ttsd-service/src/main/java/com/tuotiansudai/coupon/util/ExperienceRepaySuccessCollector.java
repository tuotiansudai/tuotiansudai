package com.tuotiansudai.coupon.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.repository.model.RepayStatus;
import org.apache.commons.collections.CollectionUtils;
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
        List<LoanModel> loanModels = loanMapper.findByProductType(ProductType.EXPERIENCE);
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
