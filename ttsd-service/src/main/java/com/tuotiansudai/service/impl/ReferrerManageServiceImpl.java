package com.tuotiansudai.service.impl;


import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.ReferrerManageMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.ReferrerManageService;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
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

    @Value(value = "${pay.user.reward}")
    private String userReward;

    @Value(value = "${pay.staff.reward}")
    private String staffReward;

    @Override
    public List<ReferrerManageView> findReferrerManage(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source, int currentPageNo, int pageSize) {
        return referrerManageMapper.findReferrerManage(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source, (currentPageNo - 1) * pageSize, pageSize);
    }

    @Override
    public int findReferrerManageCount(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source) {
        return referrerManageMapper.findReferrerManageCount(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source);
    }

    @Override
    public long findReferrerManageInvestAmountSum(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source) {
        return referrerManageMapper.findReferrerManageInvestAmountSum(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source);
    }

    @Override
    public long findReferrerManageRewardAmountSum(String referrerLoginName, String investLoginName, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source) {
        return referrerManageMapper.findReferrerManageRewardAmountSum(referrerLoginName, investLoginName, investStartTime, investEndTime, level, rewardStartTime, rewardEndTime, role, source);
    }

    @Override
    public BasePaginationDataDto findReferrerRelationList(String referrerLoginName, String loginName, Date referStartTime, Date referEndTime, int index, int pageSize) {
        String level = getUserRewardDisplayLevel(referrerLoginName);
        referEndTime = new DateTime(referEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<ReferrerRelationView> referRelationList = referrerManageMapper.findReferRelationList(referrerLoginName, loginName, referStartTime, referEndTime, level, (index - 1) * pageSize, pageSize);
        int count = referrerManageMapper.findReferRelationCount(referrerLoginName, loginName, referStartTime, referEndTime, level);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referRelationList);
        return baseDto;
    }

    @Override
    public BasePaginationDataDto findReferInvestList(String referrerLoginName, String loginName, Date investStartTime, Date investEndTime, int index, int pageSize) {
        String level = getUserRewardDisplayLevel(referrerLoginName);
        investEndTime = new DateTime(investEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        List<ReferrerManageView> referrerManageViewList = referrerManageMapper.findReferInvestList(referrerLoginName, loginName, investStartTime, investEndTime, level, (index - 1) * pageSize, pageSize);
        formatAmount(referrerManageViewList);
        int count = referrerManageMapper.findReferInvestCount(referrerLoginName, loginName, investStartTime, investEndTime, level);
        BasePaginationDataDto baseDto = new BasePaginationDataDto(index, pageSize, count, referrerManageViewList);
        return baseDto;
    }

    private void formatAmount(List<ReferrerManageView> referrerManageViewList) {
        for (ReferrerManageView view : referrerManageViewList) {
            view.setInvestAmountStr(AmountConverter.convertCentToString(view.getInvestAmount()));
            view.setRewardAmountStr(AmountConverter.convertCentToString(view.getRewardAmount()));
        }
    }

    @Override
    public String getUserRewardDisplayLevel(String loginName) {
        int level = 0;
        int merLevel = staffReward.split("\\|").length;
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


    @Override
    public String findReferInvestTotalAmount(String referrerLoginName, String loginName, Date startTime, Date endTime) {
        String level = getUserRewardDisplayLevel(referrerLoginName);
        endTime = new DateTime(endTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
        Long totalAmount = referrerManageMapper.findReferInvestTotalAmount(referrerLoginName, loginName, startTime, endTime, level);
        return totalAmount == null ? "0" : AmountConverter.convertCentToString(totalAmount);
    }
}
