package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.service.ReferrerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReferrerManageServiceImpl implements ReferrerManageService {

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

    @Override
    public List<ReferrerManageView> findReferrerManage(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, int currentPageNo, int pageSize) {
        return referrerManageMapper.findReferrerManage(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, (currentPageNo - 1) * pageSize, pageSize);
    }

    @Override
    public int findReferrerManageCount(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role) {
        return referrerManageMapper.findReferrerManageCount(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role);
    }

    @Override
    public BasePaginationDataDto findReferrerRelationList(String referrerLoginName, String loginName, Date referStartTime, Date referEndTime, int index, int pageSize) {
        List<ReferrerRelationView> referRelationList = referrerManageMapper.findReferRelationList(referrerLoginName, loginName, referStartTime, referEndTime, (index - 1) * pageSize, index * pageSize);
        int count = referrerManageMapper.findReferRelationCount(referrerLoginName, loginName, referStartTime, referEndTime);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referRelationList);
        return baseDto;
    }

    @Override
    public BasePaginationDataDto findReferInvestList(String referrerLoginName, String loginName, Date investStartTime, Date investEndTime, int index, int pageSize) {
        List<ReferrerManageView> referrerManageViewList = referrerManageMapper.findReferInvestList(referrerLoginName, loginName, investStartTime, investEndTime, (index - 1) * pageSize, index * pageSize);
        int count = referrerManageMapper.findReferInvestCount(referrerLoginName, loginName, investStartTime, investEndTime);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referrerManageViewList);
        return baseDto;
    }
}
