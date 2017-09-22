package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.CreditLoanRechargeDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.paywrapper.repository.model.sync.response.ProjectTransferNopwdResponseModel;
import com.tuotiansudai.paywrapper.service.impl.CreditLoanRechargeServiceImpl;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.CreditLoanRechargeMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class CreditLoanRechargeServiceTest {


    @InjectMocks
    private CreditLoanRechargeServiceImpl creditLoanRechargeService;

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CreditLoanRechargeMapper creditLoanRechargeMapper;
    @Mock
    private PaySyncClient paySyncClient;
    @Mock
    private PayAsyncClient payAsyncClient;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldRechargeNoPassword() throws Exception {
        UserModel userModel = getFakeUserModel();

        AccountModel accountModel = new AccountModel("creditLoan", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(1000l);

        CreditLoanRechargeDto dto = new CreditLoanRechargeDto();
        dto.setMobile(userModel.getMobile());
        dto.setOperatorLoginName("admin");
        dto.setAmount("100");

        ProjectTransferNopwdResponseModel responseModel = new ProjectTransferNopwdResponseModel();
        Map<String, String> resp = new HashMap<>();
        resp.put("ret_code", "0000");
        responseModel.initializeModel(resp);

        when(userMapper.findByMobile(userModel.getMobile())).thenReturn(userModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(paySyncClient.send(any(),any(),any())).thenReturn(responseModel);

        BaseDto<PayDataDto> baseDto = creditLoanRechargeService.creditLoanRechargeNoPwd(dto);

        assertTrue(baseDto.getData().getStatus());
    }

    @Test
    public void shouldRecharge() throws Exception {
        UserModel userModel = getFakeUserModel();

        AccountModel accountModel = new AccountModel("creditLoan", "payUserId", "payAccountId", new Date());
        accountModel.setBalance(1000l);

        CreditLoanRechargeDto creditLoanRechargeDto = new CreditLoanRechargeDto();
        creditLoanRechargeDto.setMobile(userModel.getMobile());
        creditLoanRechargeDto.setOperatorLoginName("admin");
        creditLoanRechargeDto.setAmount("100");

        BaseDto<PayFormDataDto> dto = new BaseDto<>();
        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        dto.setData(payFormDataDto);

        when(userMapper.findByMobile(userModel.getMobile())).thenReturn(userModel);
        when(accountMapper.findByLoginName(anyString())).thenReturn(accountModel);
        when(payAsyncClient.generateFormData(any(Class.class), any())).thenReturn(dto);

        BaseDto<PayFormDataDto> baseDto = creditLoanRechargeService.creditLoanRecharge(creditLoanRechargeDto);
        assertTrue(baseDto.getData().getStatus());
    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("creditLoan");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("12332112332");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

}
