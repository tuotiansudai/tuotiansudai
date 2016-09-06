package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ProductType;
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
    public boolean contains(long couponId, String loginName) {
        List<LoanModel> loanModels = loanMapper.findByProductType(LoanStatus.RAISING,Lists.newArrayList(ProductType.EXPERIENCE), ActivityType.NEWBIE);
        for (LoanModel loanModel : loanModels) {
            if (CollectionUtils.isNotEmpty(investMapper.findByLoanIdAndLoginName(loanModel.getId(), loginName))) {
                return true;
            }
        }
        return false;
    }
}
