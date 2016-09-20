package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPurchaseModel;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang.RandomStringUtils;
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
public class MembershipPurchaseMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MembershipPurchaseMapper membershipPurchaseMapper;

    @Test
    public void shouldCreate() throws Exception {
        UserModel fakeUser = createFakeUser("fakeUser");
        MembershipPurchaseModel purchaseModel = new MembershipPurchaseModel(Long.parseLong(RandomStringUtils.randomNumeric(10)),
                fakeUser.getLoginName(),
                fakeUser.getMobile(),
                "username",
                5,
                30,
                1000,
                Source.WEB);

        membershipPurchaseMapper.create(purchaseModel);

        assertNotNull(membershipPurchaseMapper.findById(purchaseModel.getId()));
    }

    private UserModel createFakeUser(String loginName) {
        UserModel model = new UserModel();
        model.setLoginName(loginName);
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile(RandomStringUtils.randomNumeric(11));
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
