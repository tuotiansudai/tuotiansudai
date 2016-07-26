package com.tuotiansudai.coupon.util;

import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestAchievementCollector implements InvestAchievementUserCollector {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public List<String> collect(long couponId) {
        return Lists.newArrayList();
    }


    @Override
    public boolean contains(long loanId, String loginName, UserGroup userGroup) {
        LoanModel loanModel = loanMapper.findById(loanId);
        if(loanModel == null){
            return false;
        }
        long investId = 0;
        if(UserGroup.FIRST_INVEST_ACHIEVEMENT.equals(userGroup)){
            investId = loanModel.getFirstInvestAchievementId();
        }
        if(UserGroup.MAX_AMOUNT_ACHIEVEMENT.equals(userGroup)){
            investId = loanModel.getFirstInvestAchievementId();
        }
        if(UserGroup.LAST_INVEST_ACHIEVEMENT.equals(userGroup)){
            investId = loanModel.getFirstInvestAchievementId();
        }

        if(investMapper.findById(investId).getLoginName().equals(loginName)){
            return true;
        }
        return false;
    }
}
