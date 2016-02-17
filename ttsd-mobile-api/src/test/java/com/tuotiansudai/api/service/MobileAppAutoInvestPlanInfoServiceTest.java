package com.tuotiansudai.api.service;


import com.tuotiansudai.api.dto.AutoInvestPlanInfoResponseDataDto;
import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AutoInvestPlanModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.IdGenerator;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppAutoInvestPlanInfoServiceTest  {

    @Autowired
    private MobileAppAutoInvestPlanInfoService mobileAppAutoInvestPlanInfoService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Test
    public void shouldGetAutoInvestPlanInfoDataIsSuccess(){
        UserModel userModel = createUserModelTest();

        AutoInvestPlanModel autoInvestPlanModel = createUserAutoInvestPlan(userModel.getLoginName(), 769);

        AccountModel accountModel = new AccountModel(userModel.getLoginName(), "userName", "identityNumber", "payUserId", "payAccountId", new Date());
        accountModel.setAutoInvest(true);
        accountMapper.create(accountModel);
        accountMapper.update(accountModel);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(userModel.getLoginName());
        baseParamDto.setBaseParam(baseParam);

        BaseResponseDto<AutoInvestPlanInfoResponseDataDto> baseDto = mobileAppAutoInvestPlanInfoService.getAutoInvestPlanInfoData(baseParamDto);
        assertEquals(true,baseDto.getData().isAutoInvest());
        assertEquals("" + autoInvestPlanModel.getId(), baseDto.getData().getAutoInvestPlan().getAutoPlanId());
        assertEquals("1",baseDto.getData().getAutoInvestPlan().getAutoInvestPeriods().get(0).getPid());
        assertEquals("256",baseDto.getData().getAutoInvestPlan().getAutoInvestPeriods().get(8).getPid());
        assertEquals("512",baseDto.getData().getAutoInvestPlan().getAutoInvestPeriods().get(9).getPid());


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

    private AutoInvestPlanModel createUserAutoInvestPlan(String userId, int periods){
        AutoInvestPlanModel model = new AutoInvestPlanModel();
        model.setEnabled(true);
        model.setLoginName(userId);
        model.setRetentionAmount(10000);
        model.setAutoInvestPeriods(periods);
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setMaxInvestAmount(1000000);
        model.setMinInvestAmount(50000);
        autoInvestPlanMapper.create(model);
        return model;
    }

}
