package com.tuotiansudai.api.service;


import com.tuotiansudai.activity.service.MoneyTreePrizeService;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppMembershipService;
import com.tuotiansudai.api.service.v1_0.MobileAppMoneyTreeService;
import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class MobileAppMoneyTreeServiceTest extends ServiceTestBase{

    @Autowired
    private MoneyTreePrizeService moneyTreePrizeService;

    @Autowired
    private MobileAppMoneyTreeService mobileAppMoneyTreeService;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldGenerateLeftCount(){
        UserModel userModel = getFakeUser("testMontyTree");
        userModel.setMobile("13888888888");
        userMapper.create(userModel);

        MembershipRequestDto requestDto = new MembershipRequestDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("test");
        requestDto.setBaseParam(baseParam);
        BaseResponseDto<MoneyTreeLeftCountResponseDataDto> dataDto = mobileAppMoneyTreeService.generateLeftCount(userModel.getLoginName());

        assertThat("0000",is(dataDto.getCode()));
        assertThat(1,is(dataDto.getData().getLeftCount()));
    }


}
