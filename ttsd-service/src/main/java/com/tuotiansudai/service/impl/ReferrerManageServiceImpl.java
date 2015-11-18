package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.service.ReferrerManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReferrerManageServiceImpl implements ReferrerManageService {

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Value(value = "${referrer.user.reward}")
    private String userReward;

    @Value(value = "${referrer.merchandiser.reward}")
    private String merReward;

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
        String level = getUserRewardDisplayLevel(referrerLoginName);

        List<ReferrerRelationView> referRelationList = referrerManageMapper.findReferRelationList(referrerLoginName, loginName, referStartTime, referEndTime, level, (index - 1) * pageSize, pageSize);
        int count = referrerManageMapper.findReferRelationCount(referrerLoginName, loginName, referStartTime, referEndTime, level);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referRelationList);
        return baseDto;
    }

    @Override
    public BasePaginationDataDto findReferInvestList(String referrerLoginName, String loginName, Date investStartTime, Date investEndTime, int index, int pageSize) {
        String level = getUserRewardDisplayLevel(referrerLoginName);

        List<ReferrerManageView> referrerManageViewList = referrerManageMapper.findReferInvestList(referrerLoginName, loginName, investStartTime, investEndTime, level, (index - 1) * pageSize, pageSize);
        int count = referrerManageMapper.findReferInvestCount(referrerLoginName, loginName, investStartTime, investEndTime, level);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referrerManageViewList);
        return baseDto;
    }
    @Override
    public String getUserRewardDisplayLevel(String loginName) {
        int level = 0;
        int merLevel = merReward.split("\\|").length;
        int userLevel = userReward.split("\\|").length;

        List<UserRoleModel> userRoleModelList = userRoleMapper.findByLoginName(loginName);
        for (UserRoleModel model : userRoleModelList) {
            if (model.getRole().equals(Role.STAFF)) {
                level = merLevel > level ? merLevel : level;
            } else if (model.getRole().equals(Role.USER)) {
                level = userLevel > level ? userLevel : level;
            }
        }
        return level == 0 ? null : String.valueOf(level);
    }

}
