package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.exception.ReferrerRelationException;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang3.RandomStringUtils;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ReferrerRelationServiceTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationService referrerRelationService;

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Test
    public void shouldGenerateReferrerRelations() throws ReferrerRelationException {
        UserModel referrer = getFakeUser("referrer");
        UserModel user = getFakeUser("user");
        generateUserRole(referrer.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(referrer.getLoginName(), user.getLoginName());

        List<ReferrerRelationModel> referrerRelationModels = referrerRelationMapper.findByLoginName(user.getLoginName());

        assertThat(referrerRelationModels.size(), is(1));
        assertThat(referrerRelationModels.get(0).getReferrerLoginName(), is(referrer.getLoginName()));
        assertThat(referrerRelationModels.get(0).getLevel(), is(1));
    }

    @Test
    public void shouldNotGenerateReferrerRelationsWhenUserReferrerLevelGreatThanTwo() throws ReferrerRelationException {
        UserModel user1 = getFakeUser("user1");
        UserModel user2 = getFakeUser("user2");
        UserModel user3 = getFakeUser("user3");
        UserModel user4 = getFakeUser("user4");
        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user4.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user2.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user3.getLoginName()));
    }

    @Test
    public void shouldNotGenerateReferrerRelationsWhenStaffReferrerLevelGreatThanFour() throws ReferrerRelationException {
        UserModel user1 = getFakeUser("user1");
        UserModel user2 = getFakeUser("user2");
        UserModel user3 = getFakeUser("user3");
        UserModel user4 = getFakeUser("user4");
        UserModel user5 = getFakeUser("user5");
        UserModel user6 = getFakeUser("user6");
        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.STAFF));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user6.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(user5.getLoginName(), user6.getLoginName());
        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user6.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user2.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user3.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user4.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user5.getLoginName()));
    }

    @Test
    public void shouldGenerateReferrerRelationsWhenRemoveReferrer() throws ReferrerRelationException {
        UserModel user1 = getFakeUser("user1");
        UserModel user2 = getFakeUser("user2");
        UserModel user3 = getFakeUser("user3");
        UserModel user4 = getFakeUser("user4");
        UserModel user5 = getFakeUser("user5");
        UserModel user6 = getFakeUser("user6");
        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.STAFF));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user6.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(user5.getLoginName(), user6.getLoginName());
        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        referrerRelationService.generateRelation(null, user5.getLoginName());

        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user5.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user2.getLoginName(), user5.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user3.getLoginName(), user5.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user4.getLoginName(), user5.getLoginName()));

        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user5.getLoginName(), user6.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user4.getLoginName(), user6.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user3.getLoginName(), user6.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user2.getLoginName(), user6.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user6.getLoginName()));
    }

    @Test
    public void shouldGenerateReferrerRelationsWhenReplaceReferrer() throws ReferrerRelationException {
        UserModel user1 = getFakeUser("user1");
        UserModel user2 = getFakeUser("user2");
        UserModel user3 = getFakeUser("user3");
        UserModel user4 = getFakeUser("user4");
        UserModel user5 = getFakeUser("user5");
        UserModel user6 = getFakeUser("user6");
        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.STAFF));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user6.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(user5.getLoginName(), user6.getLoginName());
        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        referrerRelationService.generateRelation(user3.getLoginName(), user5.getLoginName());

        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user4.getLoginName(), user5.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user4.getLoginName(), user6.getLoginName()));

        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user3.getLoginName(), user5.getLoginName()).getLevel(), is(1));
        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user2.getLoginName(), user5.getLoginName()).getLevel(), is(2));
        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user5.getLoginName()).getLevel(), is(3));
        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user5.getLoginName(), user6.getLoginName()).getLevel(), is(1));
        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user3.getLoginName(), user6.getLoginName()).getLevel(), is(2));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user2.getLoginName(), user6.getLoginName()));
        assertThat(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user6.getLoginName()).getLevel(), is(4));
    }

    @Test
    public void shouldGenerateReferrerRelationsWhenChangeStaffToUser() throws ReferrerRelationException {
        UserModel user1 = getFakeUser("user1");
        UserModel user2 = getFakeUser("user2");
        UserModel user3 = getFakeUser("user3");
        UserModel user4 = getFakeUser("user4");
        UserModel user5 = getFakeUser("user5");
        UserModel user6 = getFakeUser("user6");
        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.STAFF));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user6.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(user5.getLoginName(), user6.getLoginName());
        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());

        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER));

        referrerRelationService.generateRelation(null, user1.getLoginName());

        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user2.getLoginName()));
        assertNotNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user3.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user4.getLoginName()));
        assertNull(referrerRelationMapper.findByReferrerAndLoginName(user1.getLoginName(), user5.getLoginName()));
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("email");
        fakeUser.setMobile(RandomStringUtils.randomNumeric(11));
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }

    private void generateUserRole(String loginName, List<Role> roles) {
        userRoleMapper.deleteByLoginName(loginName);
        List<UserRoleModel> userRoles = Lists.newArrayList();
        for (Role role : roles) {
            userRoles.add(new UserRoleModel(loginName, role));
        }
        userRoleMapper.create(userRoles);
    }
}
