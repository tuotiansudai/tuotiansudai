package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.AgreementType;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class AgreementServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AgreementService agreementService;

    private MockWebServer mockPayServer;

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId,userId,"120101198810012010","payUserId","payAccountId",new Date());
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        accountMapper.create(accountModel);
        return accountModel;
    }

    private void generateMockResponse(int times) {
        for (int index = 0; index < times; index++){
            MockResponse mockResponse = new MockResponse();
            mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                    "<html>\n" +
                    "  <head>\n" +
                    "    <META NAME=\"MobilePayPlatform\" CONTENT=\"mer_id=7099088&ret_code=0000&ret_msg=成功&sign_type=RSA&version=1.0&sign=rqxyL+LrtzdGba4k4rFd1cs232Kcc4aQaUHTQlfZ0y9ayowzpxMwnbrbKyVHPGRxVz/UzLdo6uhNjPmGHND8F/yT0TDXkF1K8KW5AEjCzOwq39dWhEpLon62a1K4fchubLrpdeAx45X1YqpqL0s6uug/jb4SeWAYPi0ktnlHFVE=\">\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "  </body>\n" +
                    "</html>");
            mockResponse.setResponseCode(200);
            this.mockPayServer.enqueue(mockResponse);
        }
    }

    private Map<String, String> getFakeCallbackParamsMap(AgreementType agreementType) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("service", "ptp_mer_bind_agreement")
                .put("sign_type", "RSA")
                .put("sign", "sign")
                .put("mer_id", "mer_id")
                .put("version", "1.0")
                .put("trade_no", "trade_no")
                .put("mer_date", new SimpleDateFormat("yyyyMMdd").format(new Date()))
                .put("ret_code", "0000")
                .put("ret_msg", "")
                .put("user_id", "")
                .put("user_bind_agreement_lis", agreementType.name())
                .build());
    }

    @Test
    public void shouldAgreementFastPay() {

    }

    @Test
    public void shouldAgreementAutoInvest() {
        String userId = "testAutoInvest";
        UserModel userModel = createUserByUserId(userId);
        AccountModel accountModel = createAccountByUserId(userId);
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setLoginName(userId);
        agreementDto.setAutoInvest(true);
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getFields().get("user_bind_agreement_list"), is(AgreementType.ZTBB0G00.name()));

        this.generateMockResponse(10);
        agreementService.agreementCallback(getFakeCallbackParamsMap(), "", null);


    }

    @Test
    public void shouldAgreementNoPasswordInvest() {

    }

    @Test
    public void shouldAgreementAutoRepay() {

    }

}
