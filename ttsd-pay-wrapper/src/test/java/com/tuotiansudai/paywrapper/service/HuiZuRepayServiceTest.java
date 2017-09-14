package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuRepayDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuRepayMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class HuiZuRepayServiceTest {
    private static final String loginName = "testHuiZuRepay";

    @Autowired
    private HuiZuRepayService huiZuRepayService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    @Before
    public void setUp() {
        this.createFakeUser(loginName, 300);
    }

    @Test
    public void shouldPasswordRepayIsSuccess() {
        HuiZuRepayDto huiZuRepayDto = createFakeHuiZuRepayDto("1.00");
        BaseDto<PayFormDataDto> baseDto = huiZuRepayService.passwordRepay(huiZuRepayDto);
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        assertEquals(true, redisWrapperClient.exists(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId())));
        assertEquals(huiZuRepayDto.getLoginName(), redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId()), "loginName"));
        assertEquals(huiZuRepayDto.getAmount(), redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId()), "amount"));
        assertEquals(String.valueOf(huiZuRepayDto.getPeriod()), redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId()), "period"));
        assertEquals(SyncRequestStatus.SENT.name(), redisWrapperClient.hget(String.format("REPAY_PLAN_ID:%s", huiZuRepayDto.getRepayPlanId()), "status"));

        assertEquals(true, baseDto.isSuccess());
        assertEquals(huiZuRepayDto.getAmount(), baseDto.getData().getFields().get("amount"));
        assertEquals(huiZuRepayDto.getRepayPlanId(), baseDto.getData().getFields().get("order_id"));
        assertEquals(accountModel.getPayUserId(), baseDto.getData().getFields().get("partic_user_id"));

    }

    @Test
    public void shouldPasswordRepayReturnBalanceInsufficient() {
        HuiZuRepayDto huiZuRepayDto = createFakeHuiZuRepayDto("3.10");
        BaseDto<PayFormDataDto> baseDto = huiZuRepayService.passwordRepay(huiZuRepayDto);
        assertEquals("余额不足，请充值", baseDto.getData().getMessage());

    }

    @Test
    public void shouldPasswordRepayReturnRepayIdPaySuccess() {
        HuiZuRepayDto huiZuRepayDto = createFakeHuiZuRepayDto("1.10");
        BaseDto<PayFormDataDto> baseDto = huiZuRepayService.passwordRepay(huiZuRepayDto);

        redisWrapperClient.hmset(String.format("REPAY_PLAN_ID:%s", String.valueOf(huiZuRepayDto.getRepayPlanId())),
                Maps.newHashMap(ImmutableMap.builder()
                        .put("loginName", huiZuRepayDto.getLoginName())
                        .put("amount", String.valueOf(huiZuRepayDto.getAmount()))
                        .put("period", String.valueOf(huiZuRepayDto.getPeriod()))
                        .put("status", SyncRequestStatus.SUCCESS.name())
                        .build()),
                30 * 30);
        assertEquals(String.format("用户:%s-第%s期-已经还款成功",
                huiZuRepayDto.getLoginName(),
                String.valueOf(huiZuRepayDto.getPeriod())), baseDto.getData().getMessage());
    }

    private HuiZuRepayDto createFakeHuiZuRepayDto(String amount) {
        HuiZuRepayDto huiZuRepayDto = new HuiZuRepayDto();
        huiZuRepayDto.setLoginName(loginName);
        huiZuRepayDto.setPeriod(2);
        huiZuRepayDto.setAmount(amount);
        huiZuRepayDto.setRepayPlanId("001");
        return huiZuRepayDto;
    }

    private void createFakeUser(String loginName, long balance) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setPassword("password");
        fakeUserModel.setMobile(loginName);
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountModel.setBalance(balance);
        accountModel.setFreeze(0l);
        accountMapper.create(accountModel);
    }
}
