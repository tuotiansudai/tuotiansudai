package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class WithdrawMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private WithdrawMapper withdrawMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldCreateWithdraw() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setId(idGenerator.generate());
        bankCardModel.setLoginName(fakeUserModel.getLoginName());
        bankCardModel.setBankCode("code");
        bankCardModel.setCardNumber("number");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardMapper.create(bankCardModel);

        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(idGenerator.generate());
        withdrawModel.setBankCardId(bankCardModel.getId());
        withdrawModel.setLoginName(fakeUserModel.getLoginName());
        withdrawModel.setAmount(1L);
        withdrawModel.setFee(1L);
        withdrawModel.setStatus(WithdrawStatus.WAIT_PAY);
        withdrawModel.setSource(Source.WEB);
        withdrawModel.setCreatedTime(new Date());

        withdrawMapper.create(withdrawModel);

        assertNotNull(withdrawMapper.findById(withdrawModel.getId()));
    }

    @Test
    public void shouldUpdateWithdraw() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setId(idGenerator.generate());
        bankCardModel.setLoginName(fakeUserModel.getLoginName());
        bankCardModel.setBankCode("code");
        bankCardModel.setCardNumber("number");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardMapper.create(bankCardModel);

        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(idGenerator.generate());
        withdrawModel.setBankCardId(bankCardModel.getId());
        withdrawModel.setLoginName(fakeUserModel.getLoginName());
        withdrawModel.setAmount(1L);
        withdrawModel.setFee(1L);
        withdrawModel.setSource(Source.WEB);
        withdrawModel.setStatus(WithdrawStatus.WAIT_PAY);
        withdrawModel.setCreatedTime(new Date());
        withdrawMapper.create(withdrawModel);

        withdrawModel.setNotifyMessage("recheck message");
        withdrawMapper.update(withdrawModel);

        assertNotNull(withdrawMapper.findById(withdrawModel.getId()).getNotifyMessage());
    }

    public UserModel getFakeUserModel() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }
}
