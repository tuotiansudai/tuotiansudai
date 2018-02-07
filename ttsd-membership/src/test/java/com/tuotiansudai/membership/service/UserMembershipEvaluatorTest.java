package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class UserMembershipEvaluatorTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;

    @Autowired
    private UserMembershipEvaluator userMembershipEvaluator;

    @Test
    public void shouldEvaluateWhenLoginNameIsNotExist() throws Exception {
        assertNull(userMembershipEvaluator.evaluate("loginNameIsNotExist"));
    }

    @Test
    public void shouldEvaluateWhenUserMembershipIsOnlyOne() {
        UserModel fakeUser = this.getFakeUser("level0User");

        UserMembershipModel userMembershipModel = new UserMembershipModel(fakeUser.getLoginName(), 1, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel.setCreatedTime(new DateTime().minusDays(1).toDate());
        userMembershipMapper.create(userMembershipModel);

        assertThat(userMembershipEvaluator.evaluate(fakeUser.getLoginName()).getLevel(), is(0));
    }

    @Test
    public void shouldEvaluateWhenUserMembershipIsMoreThanOne() {
        UserModel fakeUser = this.getFakeUser("fakeUser");

        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 5, new DateTime().minusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel1.setCreatedTime(new DateTime().minusDays(11).toDate());
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 3, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel2.setCreatedTime(new DateTime().minusDays(11).toDate());
        UserMembershipModel userMembershipModel3 = new UserMembershipModel(fakeUser.getLoginName(), 4, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel3.setCreatedTime(new DateTime().minusDays(11).toDate());

        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);
        userMembershipMapper.create(userMembershipModel3);

        assertThat(userMembershipEvaluator.evaluate(fakeUser.getLoginName()).getLevel(), is(3));
    }

    @Test
    public void shouldEvaluateLevel0WhenNoMembershipNotFound() {
        UserModel fakeUser = this.getFakeUser("fakeUser");

        UserMembershipModel userMembershipModel1 = new UserMembershipModel(fakeUser.getLoginName(), 2, new DateTime().plusDays(20).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel1.setCreatedTime(new DateTime().plusDays(2).toDate());
        UserMembershipModel userMembershipModel2 = new UserMembershipModel(fakeUser.getLoginName(), 1, new DateTime().plusDays(10).toDate(), UserMembershipType.UPGRADE);
        userMembershipModel2.setCreatedTime(new DateTime().plusDays(1).toDate());

        userMembershipMapper.create(userMembershipModel1);
        userMembershipMapper.create(userMembershipModel2);

        assertThat(userMembershipEvaluator.evaluateSpecifiedDate(fakeUser.getLoginName(), new Date()).getLevel(), is(0));
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUsr@tuotiansudai.com");
        fakeUser.setMobile("11900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
