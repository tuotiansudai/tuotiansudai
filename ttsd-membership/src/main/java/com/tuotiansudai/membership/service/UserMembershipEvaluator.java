package com.tuotiansudai.membership.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.membership.dto.UserMembershipItemDto;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;

import java.util.Date;
import java.util.List;

public interface UserMembershipEvaluator {

    MembershipModel evaluate(String loginName);

    BasePaginationDataDto<UserMembershipItemDto> getUserMembershipItems(String loginName, String mobile,
                                                                        Date registerStartTime, Date registerEndTime,
                                                                        UserMembershipType userMembershipType,
                                                                        List<Integer> levels, int index, int pageSize);

    List<Integer> getAllLevels();
}
