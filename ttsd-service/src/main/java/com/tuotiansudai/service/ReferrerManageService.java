package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.model.*;

import java.util.Date;
import java.util.List;

public interface ReferrerManageService {

    List<ReferrerManageView> findReferrerManage(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source, ReferrerRewardStatus referrerRewardStatus, int currentPageNo, int pageSize);

    int findReferrerManageCount(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source,ReferrerRewardStatus referrerRewardStatus);

    BasePaginationDataDto<ReferrerRelationView> findReferrerRelationList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    BasePaginationDataDto<ReferrerManageView> findReferInvestList(String referrerLoginName, String loginName, Date startTime, Date endTime, int index, int pageSize);

    String getUserRewardDisplayLevel(String loginName);

    String findReferInvestTotalAmount(String referrerLoginName, String loginName, Date startTime, Date endTime);

    long findReferrerManageInvestAmountSum(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source);

    long findReferrerManageRewardAmountSum(String referrerMobile, String investMobile, Date investStartTime, Date investEndTime, Integer level, Date rewardStartTime, Date rewardEndTime, Role role, Source source);

}
