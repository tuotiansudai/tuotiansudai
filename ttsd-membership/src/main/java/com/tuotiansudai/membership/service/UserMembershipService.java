package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;

import java.util.Date;
import java.util.List;

public interface UserMembershipService {

    MembershipModel getMembershipByLevel(int level);

    int getProgressBarPercent(String loginName);

    int getExpireDayByLoginName(String loginName);

    UserMembershipModel findByLoginNameByMembershipId(String loginName, long membershipId);

    List<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                       Date registerStartTime, Date registerEndTime,
                                                       UserMembershipType userMembershipType,
                                                       List<Integer> levels,
                                                       int index,
                                                       int pageSize);

    List<Integer> getAllLevels();

    String getMembershipLevelByLoginNameAndInvestTime(String loginName,Date investTime);
}
