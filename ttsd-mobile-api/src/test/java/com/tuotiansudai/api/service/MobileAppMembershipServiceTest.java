package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.MembershipRequestDto;
import com.tuotiansudai.api.dto.v1_0.MembershipResponseDataDto;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppMembershipServiceTest extends ServiceTestBase {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    private MobileAppMembershipService mobileAppMembershipService;

    @Test
    public void shouldMembershipExperienceBill() {
        UserModel userModel = getFakeUser("test");
        userMapper.create(userModel);
        MembershipExperienceBillModel membership1 = fillMembershipExperienceBill(userModel.getLoginName(), 100, "会员成长+++");
        MembershipExperienceBillModel membership2 = fillMembershipExperienceBill(userModel.getLoginName(), 1000, "会员成长送不停");
        membershipExperienceBillMapper.create(membership1);
        membershipExperienceBillMapper.create(membership2);

        MembershipRequestDto requestDto = new MembershipRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        requestDto.setBaseParam(baseParam);
        BaseResponseDto<MembershipResponseDataDto> dataDto = mobileAppMembershipService.getMembershipExperienceBill(requestDto);

        assertThat("0000", is(dataDto.getCode()));
        assertThat(2, is(dataDto.getData().getMembershipExperienceBill().size()));
        assertThat(2l, is(dataDto.getData().getTotalCount()));
    }

    private MembershipExperienceBillModel fillMembershipExperienceBill(String loginName, long experience, String desc) {
        MembershipExperienceBillModel model = new MembershipExperienceBillModel();
        model.setLoginName(loginName);
        model.setExperience(experience);
        model.setDescription(desc);
        return model;
    }

}
