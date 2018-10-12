package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPersonalInfoService;
import com.tuotiansudai.enums.riskestimation.Age;
import com.tuotiansudai.enums.riskestimation.Estimate;
import com.tuotiansudai.enums.riskestimation.Income;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.RiskEstimateMapper;
import com.tuotiansudai.repository.model.RiskEstimateModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by qduljs2011 on 2018/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MobileAppPersonalInfoTest {
    @Autowired
    private MobileAppPersonalInfoService mobileAppPersonalInfoService;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private RiskEstimateMapper riskEstimateMapper;

    @Before
    public void before() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setAppVersion("4.2");
        baseParam.setUserId("userId");
        baseParamDto.setBaseParam(baseParam);
        request.setAttribute("baseParam", baseParamDto);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
    @Test
    public void shouldGetUserInfo(){
        UserModel fakeUserModel = this.getUserModelTest();
        userMapper.create(fakeUserModel);
        RiskEstimateModel riskEstimateModel=new RiskEstimateModel();
        riskEstimateModel.setAge(Age._31_50);
        riskEstimateModel.setEstimate(Estimate.CONSERVATIVE);
        riskEstimateModel.setIncome(Income._15_30);
        riskEstimateModel.setLoginName(fakeUserModel.getLoginName());
        riskEstimateModel.setId(IdGenerator.generate());
        riskEstimateMapper.create(riskEstimateModel);

        PersonalInfoRequestDto personalInfoRequestDto=new PersonalInfoRequestDto();
        personalInfoRequestDto.setUserName(fakeUserModel.getLoginName());
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(fakeUserModel.getLoginName());
        personalInfoRequestDto.setBaseParam(baseParam);

        BaseResponseDto<PersonalInfoResponseDataDto> responseDto= mobileAppPersonalInfoService.getPersonalInfoData(personalInfoRequestDto);
        assertNotNull(responseDto);
        assertEquals(responseDto.getData().getRiskEstimate(),Estimate.CONSERVATIVE.getType());
    }

    public UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("hello");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
