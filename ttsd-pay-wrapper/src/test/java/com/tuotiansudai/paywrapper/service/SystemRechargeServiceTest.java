package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.SystemRechargeDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.SystemRechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class SystemRechargeServiceTest {
    @Autowired
    private SystemRechargeService systemRechargeService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FakeUserHelper userMapper;
    @Autowired
    private SystemRechargeMapper systemRechargeMapper;


    @Test
    public void shouldSystemRechargeIsSuccess() {
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        AccountModel accountModel = new AccountModel("loginName", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(1000l);
        accountMapper.create(accountModel);

        SystemRechargeDto dto = new SystemRechargeDto();
        dto.setMobile(userModel.getMobile());
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");
        BaseDto<PayFormDataDto> baseDto = systemRechargeService.systemRecharge(dto);

        List<SystemRechargeModel> systemRechargeModels = systemRechargeMapper.findByLoginName(userModel.getLoginName());

        assertEquals(1, systemRechargeModels.size());
        SystemRechargeModel systemRechargeModel1 = systemRechargeModels.get(0);
        assertEquals(AmountConverter.convertStringToCent(dto.getAmount()), systemRechargeModel1.getAmount());
        assertEquals(RechargeStatus.WAIT_PAY, systemRechargeModel1.getStatus());


    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

    public SystemRechargeModel getFakeSystemRechargeModel() {
        SystemRechargeModel systemRechargeModel = new SystemRechargeModel();
        systemRechargeModel.setLoginName("loginName");
        systemRechargeModel.setTime(new Date());
        systemRechargeModel.setAmount(10000);
        systemRechargeModel.setSuccessTime(new Date());
        systemRechargeModel.setStatus(RechargeStatus.SUCCESS);
        systemRechargeModel.setRemark("remark");
        return systemRechargeModel;
    }


}
