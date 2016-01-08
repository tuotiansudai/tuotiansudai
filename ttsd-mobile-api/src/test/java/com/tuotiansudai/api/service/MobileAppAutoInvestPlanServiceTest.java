package com.tuotiansudai.api.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppAutoInvestPlanServiceTest {

    @Autowired
    private MobileAppAutoInvestPlanService mobileAppAutoInvestPlanService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldCreateAutoInvestPlanIsSuccess(){
        AutoInvestPlanRequestDto autoInvestPlanRequestDto = getAutoInvestPlanRequestDto();
        autoInvestPlanRequestDto.setPid("");
        BaseResponseDto<AutoInvestPlanDataDto> baseDto = mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);
        assertEquals(true, baseDto.getData().isEnabled());
        assertNotNull(baseDto.getData().getPid());
        assertEquals("500.00", baseDto.getData().getMinInvestAmount());
        assertEquals("10000.00", baseDto.getData().getMaxInvestAmount());
        assertEquals("100.00", baseDto.getData().getRetentionAmount());
        assertEquals(false,baseDto.getData().getAutoInvestPeriods().get(1).isSelected());
        assertEquals(true,baseDto.getData().getAutoInvestPeriods().get(2).isSelected());
        assertEquals("4", baseDto.getData().getAutoInvestPeriods().get(2).getPid());
        assertEquals(true,baseDto.getData().getAutoInvestPeriods().get(6).isSelected());
        assertEquals("64",baseDto.getData().getAutoInvestPeriods().get(6).getPid());
        assertEquals("2048",baseDto.getData().getAutoInvestPeriods().get(11).getPid());
        assertEquals(false,baseDto.getData().getAutoInvestPeriods().get(12).isSelected());


    }

    @Test
    public void shouldUpdateAutoInvestPlanIsSuccess(){
        AutoInvestPlanRequestDto autoInvestPlanRequestDto = getAutoInvestPlanRequestDto();

        AutoInvestPlanModel autoInvestPlanModel = fakeUserAutoInvestPlan(autoInvestPlanRequestDto.getBaseParam().getUserId(), 769);

        autoInvestPlanMapper.create(autoInvestPlanModel);

        autoInvestPlanRequestDto.setPid("" + autoInvestPlanModel.getId());

        BaseResponseDto<AutoInvestPlanDataDto> baseDto = mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);
        assertEquals(false,baseDto.getData().getAutoInvestPeriods().get(1).isSelected());
        assertEquals(true,baseDto.getData().getAutoInvestPeriods().get(2).isSelected());
        assertEquals("4", baseDto.getData().getAutoInvestPeriods().get(2).getPid());
        assertEquals(true, baseDto.getData().getAutoInvestPeriods().get(6).isSelected());
        assertEquals("64", baseDto.getData().getAutoInvestPeriods().get(6).getPid());
        assertEquals("2048", baseDto.getData().getAutoInvestPeriods().get(11).getPid());
        assertEquals(true, baseDto.getData().getAutoInvestPeriods().get(11).isSelected());
        assertEquals(false, baseDto.getData().getAutoInvestPeriods().get(12).isSelected());

    }

    private AutoInvestPlanRequestDto getAutoInvestPlanRequestDto() {
        UserModel userModel = createUserModelTest();

        AutoInvestPlanModel autoInvestPlanModel = fakeUserAutoInvestPlan(userModel.getLoginName(), 769);

        AccountModel accountModel = new AccountModel(userModel.getLoginName(), "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setAutoInvest(true);
        accountMapper.create(accountModel);
        accountMapper.update(accountModel);

        AutoInvestPlanRequestDto autoInvestPlanRequestDto = new AutoInvestPlanRequestDto();

        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParamDto.setBaseParam(baseParam);
        autoInvestPlanRequestDto.setBaseParam(baseParam);

        autoInvestPlanRequestDto.setEnabled(autoInvestPlanModel.isEnabled());
        autoInvestPlanRequestDto.setRetentionAmount(AmountConverter.convertCentToString(autoInvestPlanModel.getRetentionAmount()));
        autoInvestPlanRequestDto.setMinInvestAmount(AmountConverter.convertCentToString(autoInvestPlanModel.getMinInvestAmount()));
        autoInvestPlanRequestDto.setMaxInvestAmount(AmountConverter.convertCentToString(autoInvestPlanModel.getMaxInvestAmount()));
        List<AutoInvestPeriodDto> autoInvestPeriodDtos = Lists.newArrayList();

        AutoInvestPeriodDto autoInvestPeriodDto1 = new AutoInvestPeriodDto("4","2月期",true);
        AutoInvestPeriodDto autoInvestPeriodDto2 = new AutoInvestPeriodDto("64","6月期",true);
        AutoInvestPeriodDto autoInvestPeriodDto3 = new AutoInvestPeriodDto("2048","11月期",true);
        autoInvestPeriodDtos.add(autoInvestPeriodDto1);
        autoInvestPeriodDtos.add(autoInvestPeriodDto2);
        autoInvestPeriodDtos.add(autoInvestPeriodDto3);

        autoInvestPlanRequestDto.setAutoInvestPeriods(autoInvestPeriodDtos);
        return autoInvestPlanRequestDto;
    }

    @Test
    public void shouldTurnOffAutoInvestPlan(){
        AutoInvestPlanRequestDto autoInvestPlanRequestDto = getAutoInvestPlanRequestDto();

        AutoInvestPlanModel autoInvestPlanModel = fakeUserAutoInvestPlan(autoInvestPlanRequestDto.getBaseParam().getUserId(), 769);

        autoInvestPlanMapper.create(autoInvestPlanModel);

        autoInvestPlanRequestDto.setPid("" + autoInvestPlanModel.getId());
        autoInvestPlanRequestDto.setEnabled(false);

        BaseResponseDto<AutoInvestPlanDataDto> baseDto = mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);
        assertEquals(false,baseDto.getData().isEnabled());

    }
    @Test
    public void shouldTurnOnAutoInvestPlan(){
        AutoInvestPlanRequestDto autoInvestPlanRequestDto = getAutoInvestPlanRequestDto();

        AutoInvestPlanModel autoInvestPlanModel = fakeUserAutoInvestPlan(autoInvestPlanRequestDto.getBaseParam().getUserId(), 769);
        autoInvestPlanModel.setEnabled(false);
        autoInvestPlanMapper.create(autoInvestPlanModel);

        autoInvestPlanRequestDto.setPid("" + autoInvestPlanModel.getId());
        autoInvestPlanRequestDto.setEnabled(true);

        BaseResponseDto<AutoInvestPlanDataDto> baseDto = mobileAppAutoInvestPlanService.buildAutoInvestPlan(autoInvestPlanRequestDto);
        assertEquals(true,baseDto.getData().isEnabled());

    }

    private UserModel createUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private AutoInvestPlanModel fakeUserAutoInvestPlan(String userId, int periods){
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(userId);
        model.setRetentionAmount(10000);
        model.setAutoInvestPeriods(periods);
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000000);
        model.setMinInvestAmount(50000);
        return model;
    }

}
