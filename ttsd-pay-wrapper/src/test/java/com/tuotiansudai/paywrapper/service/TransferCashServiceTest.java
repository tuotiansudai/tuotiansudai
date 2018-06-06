package com.tuotiansudai.paywrapper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.tuotiansudai.activity.repository.dto.InviteHelpActivityPayCashDto;
import com.tuotiansudai.activity.repository.model.WeChatHelpModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.TransferCashDto;
import com.tuotiansudai.enums.SystemBillBusinessType;
import com.tuotiansudai.enums.SystemBillDetailTemplate;
import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.message.SystemBillMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.paywrapper.client.MockPayGateWrapper;
import com.tuotiansudai.paywrapper.client.PaySyncClient;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.WeChatUserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.repository.model.WeChatUserModel;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class TransferCashServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    private MockWebServer mockServer;

    @Autowired
    private PaySyncClient paySyncClient;

    @Autowired
    private TransferCashService transferCashService;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private WeChatUserMapper weChatUserMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private void createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId, "", "", new Date());
        accountModel.setAutoInvest(true);
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        accountMapper.create(accountModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private MockWebServer mockUmPayService() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n" +
                "<html>\n" +
                "  <head>\n" +
                "\t<META NAME=\"MobilePayPlatform\" CONTENT=\"ret_code=0000&ret_msg=成功&sign=HvmQ2sW1pHii6FtvjfhXU/q2kvkb8hWYbCes2WV1jL/XCA38va1Il6eIlirj3/PrIPOexKpjhnYNQymUWRb2Jdwd6DfQ249dxNffENlgMSr3B4S5r/nZ9F+qeh27SyekDUfWfha84vElgIzLyBE/Rl/ISdLGth/9u9GWA5Wd2nM=\">\n" +
                "  </head>\n" +
                "  <body>\n" +
                "  </body>\n" +
                "</html>");
        mockResponse.setResponseCode(200);
        mockWebServer.enqueue(mockResponse);

        return mockWebServer;
    }

    @Before
    public void setup() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mockServer = mockUmPayService();
        MockPayGateWrapper.injectInto(paySyncClient);
        MockPayGateWrapper.setUrl(this.mockServer.getUrl("/").toString());
    }

    @After
    public void clean() throws Exception {
        this.mockServer.shutdown();
    }

    @Test
    public void shouldTransferCash() throws Exception {
        this.createUserByUserId("testTransferCash");
        this.createAccountByUserId("testTransferCash");
        long orderId = IdGenerator.generate();
        String amount = "1";
        TransferCashDto transferCashDto = new TransferCashDto("testTransferCash", String.valueOf(orderId), amount, UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.LOTTERY_CASH, SystemBillDetailTemplate.LOTTERY_CASH_DETAIL_TEMPLATE);
        BaseDto<PayDataDto> baseDto = transferCashService.transferCash(transferCashDto);
        assertTrue(baseDto.isSuccess());

        verifyAmountTransferMessage(orderId);

        verifySystemBillMessage();
    }

    private void verifySystemBillMessage() throws IOException {
        String messageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.SystemBill.getQueueName()));
        SystemBillMessage message = JsonConverter.readValue(messageBody, SystemBillMessage.class);
        assertThat(message.getAmount(), CoreMatchers.is(1L));
    }

    private void verifyAmountTransferMessage(long orderId) throws IOException {
        String feeMessageBody = redisWrapperClient.lpop(String.format("MQ:LOCAL:%s", MessageQueue.AmountTransfer.getQueueName()));
        AmountTransferMessage feeMessage = JsonConverter.readValue(feeMessageBody, AmountTransferMessage.class);
        assertThat(feeMessage.getLoginName(), CoreMatchers.is("testTransferCash"));
        assertThat(feeMessage.getAmount(), CoreMatchers.is(1L));
        assertThat(feeMessage.getOrderId(), is(orderId));
        assertThat(feeMessage.getBusinessType(), CoreMatchers.is(UserBillBusinessType.INVEST_CASH_BACK));
        assertThat(feeMessage.getTransferType(), CoreMatchers.is(TransferType.TRANSFER_IN_BALANCE));
    }

    @Test
    public void transferCashInviteHelpActivityFail(){
        long orderId = IdGenerator.generate();
        InviteHelpActivityPayCashDto dto = new InviteHelpActivityPayCashDto("openId", null, String.valueOf(orderId), "1", UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);
        BaseDto<PayDataDto> baseDto = transferCashService.transferCashInviteHelpActivity(dto);

        assertFalse(baseDto.getData().getStatus());
        assertThat(baseDto.getData().getCode(), is(String.valueOf(HttpStatus.BAD_REQUEST)));
        assertThat(baseDto.getData().getMessage(), is("用户未注册"));

        WeChatUserModel weChatUserModel = new WeChatUserModel("loginName", "openid");
        weChatUserMapper.create(weChatUserModel);
        weChatUserModel.setBound(true);
        weChatUserMapper.update(weChatUserModel);
        BaseDto<PayDataDto> baseDto1 = transferCashService.transferCashInviteHelpActivity(dto);

        assertFalse(baseDto1.getData().getStatus());
        assertThat(baseDto1.getData().getCode(), is(String.valueOf(HttpStatus.BAD_REQUEST)));
        assertThat(baseDto1.getData().getMessage(), is("用户未实名认证"));
    }

    @Test
    public void transferCashInviteHelpActivitySuccess(){
        WeChatUserModel weChatUserModel = new WeChatUserModel("loginName", "openid");
        weChatUserMapper.create(weChatUserModel);
        weChatUserModel.setBound(true);
        weChatUserMapper.update(weChatUserModel);
        this.createAccountByUserId("loginName");
        long orderId = IdGenerator.generate();
        InviteHelpActivityPayCashDto dto = new InviteHelpActivityPayCashDto("openId", null, String.valueOf(orderId), "1", UserBillBusinessType.INVEST_CASH_BACK, SystemBillBusinessType.INVEST_CASH_BACK, SystemBillDetailTemplate.INVITE_HELP_SEND_CASH_REWARD_DETAIL_TEMPLATE);

        BaseDto<PayDataDto> baseDto = transferCashService.transferCashInviteHelpActivity(dto);

        assertTrue(baseDto.getData().getStatus());
    }

}
