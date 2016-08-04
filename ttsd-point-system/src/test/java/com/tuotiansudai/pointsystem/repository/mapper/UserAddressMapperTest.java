package com.tuotiansudai.pointsystem.repository.mapper;

import com.tuotiansudai.pointsystem.repository.model.GoodsType;
import com.tuotiansudai.pointsystem.repository.model.ProductModel;
import com.tuotiansudai.pointsystem.repository.model.UserAddressModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserAddressMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAddressMapper userAddressMapper;


    @Test
    public void shouldCreateUserAddressModel() throws Exception {
        UserModel fakeUserModel = this.createFakeUserModel();

        UserAddressModel userAddressModel = new UserAddressModel(10001,fakeUserModel.getLoginName(), "张山", "13999999999", "北京市北京市", fakeUserModel.getLoginName(), new Date());

        userAddressMapper.create(userAddressModel);

        UserAddressModel userAddressModel1 = userAddressMapper.findByLoginName(fakeUserModel.getLoginName());

        assertThat(userAddressModel1.getRealName(), is("张山"));


    }

    private UserModel createFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("userAddressUser");
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }

}
