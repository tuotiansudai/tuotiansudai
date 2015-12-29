package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.*;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.mapper.TransferAsynMapper;
import com.tuotiansudai.paywrapper.repository.model.async.request.BaseAsyncRequestModel;
import com.tuotiansudai.paywrapper.repository.model.async.request.TransferAsynRequestModel;
import com.tuotiansudai.paywrapper.service.impl.SystemRechargeServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.SystemRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class SystemRechargeServiceTest {
    @Autowired
    private SystemRechargeService systemRechargeService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SystemRechargeMapper systemRechargeMapper;



    @Test
    public void shouldSystemRechargeIsSuccess(){
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        AccountModel accountModel = new AccountModel("loginName","loginName","111111111111111123","payUserId","payAccountId",new Date());
        accountModel.setBalance(1000l);
        accountMapper.create(accountModel);

        SystemRechargeDto dto = new SystemRechargeDto();
        dto.setLoginName("loginName");
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");
        BaseDto<PayFormDataDto> baseDto = systemRechargeService.systemRecharge(dto);

        List<SystemRechargeModel> systemRechargeModels = systemRechargeMapper.findByLoginName(userModel.getLoginName());

        assertEquals(1,systemRechargeModels.size());
        SystemRechargeModel systemRechargeModel1 = systemRechargeModels.get(0);
        assertEquals(AmountConverter.convertStringToCent(dto.getAmount()),systemRechargeModel1.getAmount());
        assertEquals(RechargeStatus.WAIT_PAY,systemRechargeModel1.getStatus());


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

    public SystemRechargeModel getFakeSystemRechargeModel(){
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
