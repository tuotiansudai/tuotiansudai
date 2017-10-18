package com.tuotiansudai.point.repository.mapper;

import com.tuotiansudai.point.repository.model.UserAddressModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class UserAddressMapperTest {

    @Autowired
    private UserAddressMapper userAddressMapper;


    @Test
    public void shouldCreateUserAddressModel() throws Exception {
        String fakeLoginName = "userAddressUser";

        UserAddressModel userAddressModel = new UserAddressModel(fakeLoginName, "张山", "13999999999", "北京市北京市", fakeLoginName);

        userAddressMapper.create(userAddressModel);

        List<UserAddressModel> userAddressModelList = userAddressMapper.findByLoginName(fakeLoginName);
        assertEquals(1, userAddressModelList.size());
        UserAddressModel userAddressModel1 = userAddressModelList.get(0);
        assertThat(userAddressModel1.getContact(), is("张山"));
    }

    @Test
    public void testUpdate() throws Exception {
        String fakeLoginName1 = "user1";
        String fakeLoginName2 = "user2";

        UserAddressModel userAddressModel1 = new UserAddressModel(fakeLoginName1, "realName1", "mobile1", "address1", fakeLoginName1);
        userAddressMapper.create(userAddressModel1);
        UserAddressModel userAddressModel2 = new UserAddressModel(fakeLoginName2, "realName2", "mobile2", "address2", fakeLoginName2);
        userAddressMapper.create(userAddressModel2);

        userAddressModel1.setMobile("MOBILE1");
        userAddressModel1.setContact("REAL_NAME1");
        userAddressModel1.setAddress("ADDRESS1");
        userAddressMapper.update(userAddressModel1);

        UserAddressModel updatedUserAddressModel1 = userAddressMapper.findByLoginName(fakeLoginName1).get(0);
        assertEquals(userAddressModel1.getMobile(), updatedUserAddressModel1.getMobile());
        assertEquals(userAddressModel1.getContact(), updatedUserAddressModel1.getContact());
        assertEquals(userAddressModel1.getAddress(), updatedUserAddressModel1.getAddress());

        UserAddressModel updatedUserAddressModel2 = userAddressMapper.findByLoginName(fakeLoginName2).get(0);
        assertEquals(userAddressModel2.getMobile(), updatedUserAddressModel2.getMobile());
        assertEquals(userAddressModel2.getContact(), updatedUserAddressModel2.getContact());
        assertEquals(userAddressModel2.getAddress(), updatedUserAddressModel2.getAddress());
    }
}
