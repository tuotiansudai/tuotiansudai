package com.tuotiansudai.console.service;


import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRewardStatus;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConsoleReferrerManageService {

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

    public List<ReferrerManageView> findReferrerManage(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source, ReferrerRewardStatus referrerRewardStatus, int currentPageNo, int pageSize) {
        return referrerManageMapper.findReferrerManage(referrerMobile, investMobile, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source, referrerRewardStatus, (currentPageNo - 1) * pageSize, pageSize);
    }

    public int findReferrerManageCount(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source, ReferrerRewardStatus referrerRewardStatus) {
        return referrerManageMapper.findReferrerManageCount(referrerMobile, investMobile, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source, referrerRewardStatus);
    }

    public long findReferrerManageInvestAmountSum(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source) {
        return referrerManageMapper.findReferrerManageInvestAmountSum(referrerMobile, investMobile, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source);
    }

    public long findReferrerManageRewardAmountSum(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source) {
        return referrerManageMapper.findReferrerManageRewardAmountSum(referrerMobile, investMobile, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source);
    }
}
