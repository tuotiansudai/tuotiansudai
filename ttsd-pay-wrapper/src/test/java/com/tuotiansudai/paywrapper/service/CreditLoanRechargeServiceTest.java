package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class CreditLoanRechargeServiceTest {
    @Autowired
    private CreditLoanRechargeService creditLoanRechargeService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CreditLoanRechargeMapper creditLoanRechargeMapper;

    @Test
    public void shouldPurchaseNoPassword() throws Exception {
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        AccountModel accountModel = new AccountModel("loginName","payUserId","payAccountId",new Date());
        accountModel.setBalance(1000l);
        accountMapper.create(accountModel);

        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile(userModel.getMobile());
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");
        BaseDto<PayDataDto> baseDto = creditLoanRechargeService.creditLoanRechargeNoPwd(dto);

        List<CreditLoanRechargeModel> creditLoanRechargeModels = creditLoanRechargeMapper.findByLoginName(userModel.getLoginName());

        assertEquals(1,creditLoanRechargeModels.size());
        CreditLoanRechargeModel creditLoanRechargeModel = creditLoanRechargeModels.get(0);
        assertEquals(AmountConverter.convertStringToCent(dto.getAmount()),creditLoanRechargeModel.getAmount());
        assertEquals(RechargeStatus.WAIT_PAY,creditLoanRechargeModel.getStatus());
    }

    @Test
    public void shouldPurchase() throws Exception {
        UserModel userModel = getFakeUserModel();
        userMapper.create(userModel);

        AccountModel accountModel = new AccountModel("loginName","payUserId","payAccountId",new Date());
        accountModel.setBalance(1000l);
        accountMapper.create(accountModel);

        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile(userModel.getMobile());
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");
        BaseDto<PayFormDataDto> baseDto = creditLoanRechargeService.creditLoanRecharge(dto);

        List<CreditLoanRechargeModel> creditLoanRechargeModels = creditLoanRechargeMapper.findByLoginName(userModel.getLoginName());

        assertEquals(1,creditLoanRechargeModels.size());
        CreditLoanRechargeModel creditLoanRechargeModel = creditLoanRechargeModels.get(0);
        assertEquals(AmountConverter.convertStringToCent(dto.getAmount()),creditLoanRechargeModel.getAmount());
        assertEquals(RechargeStatus.WAIT_PAY,creditLoanRechargeModel.getStatus());
    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("loginName");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000001");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

}
