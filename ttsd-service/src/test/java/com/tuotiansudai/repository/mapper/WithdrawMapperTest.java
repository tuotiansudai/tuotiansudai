package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.repository.model.WithdrawStatus;
import com.tuotiansudai.utils.IdGenerator;
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
    private WithdrawMapper withdrawMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void shouldCreateWithdraw() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(idGenerator.generate());
        withdrawModel.setLoginName(fakeUserModel.getLoginName());
        withdrawModel.setAmount(1L);
        withdrawModel.setFee(1L);
        withdrawModel.setStatus(WithdrawStatus.WAIT_VERIFY);
        withdrawModel.setCreatedTime(new Date());

        withdrawMapper.create(withdrawModel);

        assertNotNull(withdrawMapper.findById(withdrawModel.getId()));
    }

    @Test
    public void shouldUpdateWithdraw() throws Exception {
        UserModel fakeUserModel = this.getFakeUserModel();
        userMapper.create(fakeUserModel);

        WithdrawModel withdrawModel = new WithdrawModel();
        withdrawModel.setId(idGenerator.generate());
        withdrawModel.setLoginName(fakeUserModel.getLoginName());
        withdrawModel.setAmount(1L);
        withdrawModel.setFee(1L);
        withdrawModel.setStatus(WithdrawStatus.WAIT_VERIFY);
        withdrawModel.setCreatedTime(new Date());
        withdrawMapper.create(withdrawModel);

        withdrawModel.setRecheckMessage("recheck message");
        withdrawMapper.update(withdrawModel);

        assertNotNull(withdrawMapper.findById(withdrawModel.getId()).getRecheckMessage());
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
