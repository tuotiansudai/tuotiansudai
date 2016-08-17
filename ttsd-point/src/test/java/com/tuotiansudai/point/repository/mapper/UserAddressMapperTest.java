package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserAddressModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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
import static org.junit.Assert.assertEquals;
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
        UserModel fakeUserModel = createFakeUserModel("userAddressUser");

        UserAddressModel userAddressModel = new UserAddressModel(fakeUserModel.getLoginName(), "张山", "13999999999", "北京市北京市", fakeUserModel.getLoginName());

        userAddressMapper.create(userAddressModel);

        List<UserAddressModel> userAddressModelList = userAddressMapper.findByLoginName(fakeUserModel.getLoginName());
        assertEquals(1, userAddressModelList.size());
        UserAddressModel userAddressModel1 = userAddressModelList.get(0);
        assertThat(userAddressModel1.getRealName(), is("张山"));
    }

    @Test
    public void testUpdate() throws Exception {
        UserModel user1 = createFakeUserModel("user1");
        UserModel user2 = createFakeUserModel("user2");

        UserAddressModel userAddressModel1 = new UserAddressModel(user1.getLoginName(), "realName1", "mobile1", "address1", user1.getLoginName());
        userAddressMapper.create(userAddressModel1);
        UserAddressModel userAddressModel2 = new UserAddressModel(user2.getLoginName(), "realName2", "mobile2", "address2", user2.getLoginName());
        userAddressMapper.create(userAddressModel2);

        userAddressModel1.setMobile("MOBILE1");
        userAddressModel1.setRealName("REAL_NAME1");
        userAddressModel1.setAddress("ADDRESS1");
        userAddressMapper.update(userAddressModel1);

        UserAddressModel updatedUserAddressModel1 = userAddressMapper.findByLoginName(user1.getLoginName()).get(0);
        assertEquals(userAddressModel1.getMobile(), updatedUserAddressModel1.getMobile());
        assertEquals(userAddressModel1.getRealName(), updatedUserAddressModel1.getRealName());
        assertEquals(userAddressModel1.getAddress(), updatedUserAddressModel1.getAddress());

        UserAddressModel updatedUserAddressModel2 = userAddressMapper.findByLoginName(user2.getLoginName()).get(0);
        assertEquals(userAddressModel2.getMobile(), updatedUserAddressModel2.getMobile());
        assertEquals(userAddressModel2.getRealName(), updatedUserAddressModel2.getRealName());
        assertEquals(userAddressModel2.getAddress(), updatedUserAddressModel2.getAddress());
    }

    private UserModel createFakeUserModel(String loginName) {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName(loginName);
        fakeUserModel.setPassword("123abc");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile(loginName);
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUserModel);
        return fakeUserModel;
    }
}
