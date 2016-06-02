package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
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
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserMembershipMapperTest {

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateUserMembership() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), new Date(), "system");

        userMembershipMapper.create(userMembershipModel);

        assertThat(userMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(userMembershipModel.getMembershipId(), is(2L));
        assertThat(userMembershipModel.getType(), is("system"));
    }

    @Test
    public void shouldUpdateUserMembership() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), new Date(), "system");
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel updateUserMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 5, new Date(), new Date(), "manual");
        userMembershipMapper.update(updateUserMembershipModel);

        assertThat(updateUserMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(updateUserMembershipModel.getMembershipId(), is(5L));
        assertThat(updateUserMembershipModel.getType(), is("manual"));
    }

    @Test
    public void shouldUserMembershipFindById() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), new Date(), "system");
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel membershipModel1 = userMembershipMapper.findById(userMembershipModel.getId());

        assertThat(membershipModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipModel1.getMembershipId(), is(2L));
        assertThat(membershipModel1.getType(), is("system"));

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
