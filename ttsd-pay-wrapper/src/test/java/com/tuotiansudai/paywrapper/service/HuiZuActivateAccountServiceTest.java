package com.tuotiansudai.paywrapper.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.HuiZuActivateAccountDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.paywrapper.repository.mapper.HuiZuActivateAccountNotifyMapper;
import com.tuotiansudai.paywrapper.repository.model.sync.request.SyncRequestStatus;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.RedisWrapperClient;
import org.junit.After;
import org.junit.Before;
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
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class HuiZuActivateAccountServiceTest {
    private static final String loginName = "testHuiZuActivateAccount";
    private static final String mobile = "13900000000";

    @Autowired
    private HuiZuActivateAccountService huiZuActivateAccountService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private HuiZuActivateAccountNotifyMapper huiZuActivateAccountNotifyMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();


    @Before
    public void setUp() {
        this.createFakeUser(loginName,300);
    }

    @After
    public void tearDown() {
        redisWrapperClient.del(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", mobile));
    }

    @Test
    public void shouldPasswordActivateAccountIsSuccess() {
        HuiZuActivateAccountDto huiZuActivateAccountDto = createFakeHuiZuActivateAccountDto();
        BaseDto<PayFormDataDto> baseDto = huiZuActivateAccountService.password(huiZuActivateAccountDto);
        AccountModel accountModel = accountMapper.findByLoginName(loginName);

        assertEquals(true, redisWrapperClient.exists(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", huiZuActivateAccountDto.getMobile())));
        assertEquals(huiZuActivateAccountDto.getMobile(), redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", huiZuActivateAccountDto.getMobile()), "mobile"));
        assertEquals(1, Long.parseLong(redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", huiZuActivateAccountDto.getMobile()), "amount")));
        assertEquals(SyncRequestStatus.SENT.name(), redisWrapperClient.hget(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", huiZuActivateAccountDto.getMobile()), "status"));

        assertEquals(true, baseDto.isSuccess());
        assertEquals(1, Integer.parseInt(baseDto.getData().getFields().get("amount")));
        assertEquals(huiZuActivateAccountDto.getMobile(), baseDto.getData().getFields().get("order_id").split("X")[0]);
        assertEquals(accountModel.getPayUserId(), baseDto.getData().getFields().get("partic_user_id"));
        redisWrapperClient.del(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", mobile));

    }

    @Test
    public void shouldPasswordActivateAccountIsActivated() {
        HuiZuActivateAccountDto huiZuActivateAccountDto = createFakeHuiZuActivateAccountDto();
        redisWrapperClient.hmset(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", String.valueOf(huiZuActivateAccountDto.getMobile())),
                Maps.newHashMap(ImmutableMap.builder()
                        .put("mobile", huiZuActivateAccountDto.getMobile())
                        .put("amount", String.valueOf(1))
                        .put("status", SyncRequestStatus.SENT.name())
                        .build()),
                30 * 30);

        BaseDto<PayFormDataDto> baseDto = huiZuActivateAccountService.password(huiZuActivateAccountDto);

        assertEquals(true, baseDto.isSuccess());
        assertEquals(String.format("用户%s:已经激活过账户",huiZuActivateAccountDto.getMobile()), baseDto.getData().getMessage());
    }


    @Test
    public void shouldActivateAccountModifyIsSuccess() throws AmountTransferException {
        HuiZuActivateAccountDto huiZuActivateAccountDto = createFakeHuiZuActivateAccountDto();

        redisWrapperClient.hmset(String.format("ACTIVATE_ACCOUNT_MOBILE:%s", String.valueOf(huiZuActivateAccountDto.getMobile())),
                Maps.newHashMap(ImmutableMap.builder()
                        .put("mobile", huiZuActivateAccountDto.getMobile())
                        .put("amount", String.valueOf(1))
                        .put("status", SyncRequestStatus.SENT.name())
                        .build()),
                30 * 30);
        huiZuActivateAccountService.postActivateAccount(Long.parseLong(huiZuActivateAccountDto.getMobile()));
        UserModel userModel = userMapper.findByMobile(huiZuActivateAccountDto.getMobile());
        AccountModel accountModel = accountMapper.findByLoginName(userModel.getLoginName());
        List<UserBillModel> userBillModels = userBillMapper.findByLoginName(userModel.getLoginName());

        assertEquals(299, accountModel.getBalance());
        assertEquals(1, userBillModels.get(0).getAmount());
        assertEquals(UserBillBusinessType.CREDIT_LOAN_ACTIVATE_ACCOUNT, userBillModels.get(0).getBusinessType());
        assertEquals(huiZuActivateAccountDto.getMobile(), String.valueOf(userBillModels.get(0).getOrderId()));
        assertEquals(UserBillOperationType.TO_BALANCE, userBillModels.get(0).getOperationType());
    }

    private HuiZuActivateAccountDto createFakeHuiZuActivateAccountDto() {
        HuiZuActivateAccountDto huiZuActivateAccountDto = new HuiZuActivateAccountDto();
        huiZuActivateAccountDto.setMobile(mobile);
        return huiZuActivateAccountDto;
    }

    private void createFakeUser(String loginName, long balance) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setPassword("password");
        fakeUserModel.setMobile(mobile);
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
