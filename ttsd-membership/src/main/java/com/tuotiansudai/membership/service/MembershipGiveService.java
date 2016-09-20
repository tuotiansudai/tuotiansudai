package com.tuotiansudai.membership.service;

import com.tuotiansudai.dto.BaseDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.membership.dto.MembershipGiveDto;
import com.tuotiansudai.membership.dto.MembershipGiveReceiveDto;

import java.io.InputStream;
import java.util.List;

public interface MembershipGiveService {
    void createAndEditMembershipGive(MembershipGiveDto membershipGiveDto, long importUsersId);

    MembershipGiveDto getMembershipGiveDtoById(long membershipGiveId);

    List<MembershipGiveDto> getMembershipGiveDtos(int index, int pageSize);

    long getMembershipGiveCount();

    BaseDto<BaseDataDto> approveMembershipGive(long id, String validLoginName);

    BaseDto<BaseDataDto> cancelMembershipGive(long id, String validLoginName);

    BaseDto<BaseDataDto> importGiveUsers(long importUsersId, InputStream inputStream);

    List<MembershipGiveReceiveDto> getMembershipGiveReceiveDtosByMobile(long membershipGiveId, String mobile, int index,
                                                                        int pageSize);

    long getCountMembershipGiveReceiveDtosByMobile(long membershipGiveId, String mobile);
}
