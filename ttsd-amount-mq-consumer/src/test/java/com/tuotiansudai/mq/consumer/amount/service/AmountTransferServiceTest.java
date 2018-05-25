package com.tuotiansudai.mq.consumer.amount.service;


import com.tuotiansudai.enums.TransferType;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.exception.AmountTransferException;
import com.tuotiansudai.message.AmountTransferMessage;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class AmountTransferServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AmountTransferService amountTransferService;

    @Test
    public void shouldConsumerAmountTransferMessage() throws AmountTransferException {

        String loginName = "zbx_asdf";

        mockUser(loginName);
        mockAccount(loginName);

        long orderId = 123456789;

        // test transfer_in_balance
        long inAmount = 400000;
        UserBillBusinessType businessType = UserBillBusinessType.RECHARGE_SUCCESS;

        AmountTransferMessage inAtm = new AmountTransferMessage(TransferType.TRANSFER_IN_BALANCE, loginName, orderId,
                inAmount, businessType);

        amountTransferService.amountTransferProcess(inAtm);
        verifyBalance(loginName, inAmount);

        // test transfer_out_balance
        long outAmount = 100000;
        AmountTransferMessage outAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_BALANCE, loginName, orderId,
                outAmount, businessType);

        amountTransferService.amountTransferProcess(outAtm);
        verifyBalance(loginName, inAmount - outAmount);

        // test freeze
        long freezeAmount = 150000;
        AmountTransferMessage freezeAtm = new AmountTransferMessage(TransferType.FREEZE, loginName, orderId,
                freezeAmount, businessType);
        amountTransferService.amountTransferProcess(freezeAtm);

        verifyBalance(loginName, inAmount - outAmount - freezeAmount);
        verifyFreeze(loginName, freezeAmount);

        // test unfreeze
        long unfreezeAmount = 100000;

        AmountTransferMessage unfreezeAtm = new AmountTransferMessage(TransferType.UNFREEZE, loginName, orderId,
                unfreezeAmount, businessType);
        amountTransferService.amountTransferProcess(unfreezeAtm);
        verifyBalance(loginName, inAmount - outAmount - freezeAmount + unfreezeAmount);
        verifyFreeze(loginName, freezeAmount - unfreezeAmount);

        // test transfer_out_freeze
        long toFreezeAmount = 40000;

        AmountTransferMessage toFreezeAtm = new AmountTransferMessage(TransferType.TRANSFER_OUT_FREEZE, loginName, orderId,
                toFreezeAmount, businessType);
        amountTransferService.amountTransferProcess(toFreezeAtm);
        verifyFreeze(loginName, freezeAmount - unfreezeAmount - toFreezeAmount);
    }

    private void verifyBalance(String loginName, long amount) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        assert (accountModel.getBalance() == amount);
    }

    private void verifyFreeze(String loginName, long amount) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        assert (accountModel.getFreeze() == amount);
    }

    private UserModel mockUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("139" + RandomStringUtils.randomNumeric(8));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private void mockAccount(String loginName) {
        AccountModel accountModel = new AccountModel(loginName, "payUserId", "payAccountId", new Date());
        accountMapper.create(accountModel);
    }

}
