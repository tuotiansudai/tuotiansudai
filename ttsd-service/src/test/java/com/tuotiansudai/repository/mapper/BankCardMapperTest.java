package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class BankCardMapperTest {

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private UserMapper userMapper;


    @Test
    public void shouldCreateBankCard() throws Exception {
        UserModel fakeUser = createFakeUser();
        BankCardModel bankCardModel = new BankCardModel();

        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName(fakeUser.getLoginName());
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");

        bankCardMapper.create(bankCardModel);

        assertNotNull(bankCardModel.getId());
    }

    @Test
    public void shouldUpdateBankCard() throws Exception {
        UserModel fakeUser = createFakeUser();
        BankCardModel bankCardModel = new BankCardModel();

        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName(fakeUser.getLoginName());
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");

        bankCardMapper.create(bankCardModel);

        bankCardModel.setCardNumber("99999");
        bankCardModel.setBankCode("ABC");
        bankCardMapper.update(bankCardModel);

        BankCardModel bankCardModel1 = bankCardMapper.findById(bankCardModel.getId());
        assertEquals("99999", bankCardModel1.getCardNumber());
        assertEquals("ABC", bankCardModel1.getBankCode());


    }
    @Test
    public void findByLoginNameAndIsFastPayOnIsOk(){
        UserModel fakeUser = createFakeUser();
        BankCardModel bankCardModel = new BankCardModel();

        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName(fakeUser.getLoginName());
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");

        bankCardMapper.create(bankCardModel);

        BankCardModel bankCardModel1 = bankCardMapper.findByLoginNameAndIsFastPayOn("loginName");
        assertNotNull(bankCardModel1);
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("loginName");
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("13900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
