package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
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
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.UPGRADE);

        userMembershipMapper.create(userMembershipModel);

        assertThat(userMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(userMembershipModel.getMembershipId(), is(2L));
        assertThat(userMembershipModel.getType(), is(UserMembershipType.UPGRADE));
    }

    @Test
    public void shouldUpdateUserMembership() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.UPGRADE);
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel updateUserMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 5, new Date(), UserMembershipType.GIVEN);
        userMembershipMapper.update(updateUserMembershipModel);

        assertThat(updateUserMembershipModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(updateUserMembershipModel.getMembershipId(), is(5L));
        assertThat(updateUserMembershipModel.getType(), is(UserMembershipType.GIVEN));
    }

    @Test
    public void shouldUserMembershipFindById() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 2, new Date(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel);

        UserMembershipModel membershipModel1 = userMembershipMapper.findById(userMembershipModel.getId());

        assertThat(membershipModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipModel1.getMembershipId(), is(2L));
        assertThat(membershipModel1.getType(), is(UserMembershipType.GIVEN));

    }

    @Test
    public void shouldFindActiveByLoginName() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 2, new DateTime().plusDays(2).toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(-2).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        UserMembershipModel membershipModel1 = userMembershipMapper.findActiveByLoginName(fakeUser.getLoginName());

        assertThat(membershipModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipModel1.getMembershipId(), is(2L));
        assertThat(membershipModel1.getType(), is(UserMembershipType.GIVEN));
    }

    @Test
    public void shouldFindRateByLoginName() throws Exception {

        UserModel fakeUser = createFakeUser();
        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 1, new DateTime().plusDays(-2).toDate(), UserMembershipType.GIVEN);
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(2).toDate(), UserMembershipType.GIVEN);
        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        double rate = userMembershipMapper.findRateByLoginName(fakeUser.getLoginName());

        assertThat(rate, is(0.09));
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
