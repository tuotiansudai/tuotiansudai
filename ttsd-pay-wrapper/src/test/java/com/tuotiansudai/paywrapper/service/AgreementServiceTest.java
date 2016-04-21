package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.dto.AgreementBusinessType;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PayAsyncClient;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
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
import static org.junit.Assert.assertFalse;
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
    private BankCardMapper bankCardMapper;

    @Autowired
    private AgreementService agreementService;

    private MockWebServer mockPayServer;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private PayAsyncClient payAsyncClient;

    @Autowired
    private IdGenerator idGenerator;

    private BankCardModel createBankCardByUserId(long id, String userId) {
        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setId(id);
        bankCardModel.setLoginName(userId);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setCardNumber("6225880130182016");
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardMapper.create(bankCardModel);
        return bankCardModel;
    }

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
                .put("user_id", "payUserId")
                .put("user_bind_agreement_list", agreementType.name())
                .build());
    }

    @Before
    public void setUp() throws Exception {
        this.mockPayServer = new MockWebServer();
        this.mockPayServer.start();

        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.injectInto(payAsyncClient);
        MockPayGateWrapper.setUrl(this.mockPayServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockPayServer.shutdown();
    }

    @Test
    public void shouldAgreementFastPay() {
        String userId = "testAutoInvest";
        createUserByUserId(userId);
        createAccountByUserId(userId);
        long id = idGenerator.generate();
        createBankCardByUserId(id, userId);
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setLoginName(userId);
        agreementDto.setFastPay(true);
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getFields().get("user_bind_agreement_list"), is(AgreementType.ZKJP0700.name()));

        this.generateMockResponse(10);
        agreementService.agreementCallback(getFakeCallbackParamsMap(AgreementType.ZKJP0700), "", null);

        AccountModel accountModel = accountMapper.findByLoginName(userId);
        assertFalse(accountModel.isAutoInvest());
        assertFalse(accountModel.isAutoRepay());
        assertFalse(accountModel.isNoPasswordInvest());

        BankCardModel bankCardModel = bankCardMapper.findById(id);
        assertTrue(bankCardModel.isFastPayOn());
    }

    @Test
    public void shouldAgreementAutoInvest() {
        String userId = "testAutoInvest";
        createUserByUserId(userId);
        createAccountByUserId(userId);
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setLoginName(userId);
        agreementDto.setAutoInvest(true);
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getFields().get("user_bind_agreement_list"), is(AgreementType.ZTBB0G00.name()));

        this.generateMockResponse(10);
        agreementService.agreementCallback(getFakeCallbackParamsMap(AgreementType.ZTBB0G00), "", null);

        AccountModel accountModel = accountMapper.findByLoginName(userId);
        assertTrue(accountModel.isAutoInvest());
        assertFalse(accountModel.isAutoRepay());
        assertFalse(accountModel.isNoPasswordInvest());
    }

    @Test
    public void shouldAgreementNoPasswordInvest() {
        String userId = "testAutoInvest";
        createUserByUserId(userId);
        createAccountByUserId(userId);
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setLoginName(userId);
        agreementDto.setAutoInvest(true);
        agreementDto.setNoPasswordInvest(true);
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getFields().get("user_bind_agreement_list"), is(AgreementType.ZTBB0G00.name()));

        this.generateMockResponse(10);
        agreementService.agreementCallback(getFakeCallbackParamsMap(AgreementType.ZTBB0G00), "", AgreementBusinessType.NO_PASSWORD_INVEST);

        AccountModel accountModel = accountMapper.findByLoginName(userId);
        assertTrue(accountModel.isAutoInvest());
        assertFalse(accountModel.isAutoRepay());
        assertTrue(accountModel.isNoPasswordInvest());
    }

    @Test
    public void shouldAgreementAutoRepay() {
        String userId = "testAutoInvest";
        createUserByUserId(userId);
        createAccountByUserId(userId);
        AgreementDto agreementDto = new AgreementDto();
        agreementDto.setLoginName(userId);
        agreementDto.setAutoRepay(true);
        BaseDto<PayFormDataDto> baseDto = agreementService.agreement(agreementDto);
        assertTrue(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getFields().get("user_bind_agreement_list"), is(AgreementType.ZHKB0H01.name()));

        this.generateMockResponse(10);
        agreementService.agreementCallback(getFakeCallbackParamsMap(AgreementType.ZHKB0H01), "", null);

        AccountModel accountModel = accountMapper.findByLoginName(userId);
        assertFalse(accountModel.isAutoInvest());
        assertTrue(accountModel.isAutoRepay());
        assertFalse(accountModel.isNoPasswordInvest());
    }

}
