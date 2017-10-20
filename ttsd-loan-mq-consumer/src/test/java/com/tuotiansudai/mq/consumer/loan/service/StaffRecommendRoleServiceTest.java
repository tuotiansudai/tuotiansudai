package com.tuotiansudai.mq.consumer.loan.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.UserRoleMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class StaffRecommendRoleServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private ReferrerRelationService referrerRelationService;

    @Autowired
    private StaffRecommendRoleService staffRecommendRoleService;

    @Test
    public void shouldGenerateRecommendRoleWhenUserIsNotStaffRecommendAndHasNoReferrer() {
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        referrerRelationService.generateRelation(null, user.getLoginName());

        staffRecommendRoleService.generateStaffRole(null, user.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenUserIsSDStaffAndHasNoReferrer() {
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.SD_STAFF));
        referrerRelationService.generateRelation(null, user.getLoginName());

        staffRecommendRoleService.generateStaffRole(null, user.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenReferrerIsZCStaff() {
        UserModel referrer = getFakeUser("fakeReferrer");
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(referrer.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        referrerRelationService.generateRelation(referrer.getLoginName(), user.getLoginName());

        staffRecommendRoleService.generateStaffRole(referrer.getLoginName(), user.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenReferrerIsSDStaff() {
        UserModel referrer = getFakeUser("fakeReferrer");
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(referrer.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        referrerRelationService.generateRelation(referrer.getLoginName(), user.getLoginName());

        staffRecommendRoleService.generateStaffRole(referrer.getLoginName(), user.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenUserIsSDStaff() {
        UserModel referrer = getFakeUser("fakeReferrer");
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(referrer.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        referrerRelationService.generateRelation(referrer.getLoginName(), user.getLoginName());

        staffRecommendRoleService.generateStaffRole(null, referrer.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.SD_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenUserIsZCStaff() {
        UserModel referrer = getFakeUser("fakeReferrer");
        UserModel user = getFakeUser("fakeUser");
        generateUserRole(referrer.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF));
        generateUserRole(user.getLoginName(), Lists.newArrayList(Role.USER));
        referrerRelationService.generateRelation(referrer.getLoginName(), user.getLoginName());

        staffRecommendRoleService.generateStaffRole(null, referrer.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.ZC_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer.getLoginName(), Role.SD_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenRootReferrerIsChangeToZCStaff() {
        UserModel referrer1 = getFakeUser("fakeReferrer1");
        UserModel referrer2 = getFakeUser("fakeReferrer2");
        UserModel referrer3 = getFakeUser("fakeReferrer3");
        UserModel referrer4 = getFakeUser("fakeReferrer4");
        UserModel referrer5 = getFakeUser("fakeReferrer5");
        UserModel user1 = getFakeUser("fakeUser1");
        UserModel user2 = getFakeUser("fakeUser2");
        UserModel user3 = getFakeUser("fakeUser3");
        UserModel user4 = getFakeUser("fakeUser4");
        UserModel user5 = getFakeUser("fakeUser5");

        generateUserRole(referrer1.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF));
        generateUserRole(referrer2.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF_RECOMMEND));
        generateUserRole(referrer3.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF_RECOMMEND));
        generateUserRole(referrer4.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF_RECOMMEND));
        generateUserRole(referrer5.getLoginName(), Lists.newArrayList(Role.USER, Role.ZC_STAFF_RECOMMEND));

        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));

        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());
        referrerRelationService.generateRelation(referrer5.getLoginName(), user1.getLoginName());
        referrerRelationService.generateRelation(referrer4.getLoginName(), referrer5.getLoginName());
        referrerRelationService.generateRelation(referrer3.getLoginName(), referrer4.getLoginName());
        referrerRelationService.generateRelation(referrer1.getLoginName(), referrer2.getLoginName());

        staffRecommendRoleService.generateStaffRole(referrer5.getLoginName(), user1.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.ZC_STAFF_RECOMMEND));
    }

    @Test
    public void shouldGenerateRecommendRoleWhenRootReferrerIsChangeToSDStaff() {
        UserModel referrer1 = getFakeUser("fakeReferrer1");
        UserModel referrer2 = getFakeUser("fakeReferrer2");
        UserModel referrer3 = getFakeUser("fakeReferrer3");
        UserModel referrer4 = getFakeUser("fakeReferrer4");
        UserModel referrer5 = getFakeUser("fakeReferrer5");
        UserModel user1 = getFakeUser("fakeUser1");
        UserModel user2 = getFakeUser("fakeUser2");
        UserModel user3 = getFakeUser("fakeUser3");
        UserModel user4 = getFakeUser("fakeUser4");
        UserModel user5 = getFakeUser("fakeUser5");

        generateUserRole(referrer1.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF));
        generateUserRole(referrer2.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF_RECOMMEND));
        generateUserRole(referrer3.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF_RECOMMEND));
        generateUserRole(referrer4.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF_RECOMMEND));
        generateUserRole(referrer5.getLoginName(), Lists.newArrayList(Role.USER, Role.SD_STAFF_RECOMMEND));

        generateUserRole(user1.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user2.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user3.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user4.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));
        generateUserRole(user5.getLoginName(), Lists.newArrayList(Role.USER, Role.NOT_STAFF_RECOMMEND));

        referrerRelationService.generateRelation(user4.getLoginName(), user5.getLoginName());
        referrerRelationService.generateRelation(user3.getLoginName(), user4.getLoginName());
        referrerRelationService.generateRelation(user2.getLoginName(), user3.getLoginName());
        referrerRelationService.generateRelation(user1.getLoginName(), user2.getLoginName());
        referrerRelationService.generateRelation(referrer5.getLoginName(), user1.getLoginName());
        referrerRelationService.generateRelation(referrer4.getLoginName(), referrer5.getLoginName());
        referrerRelationService.generateRelation(referrer3.getLoginName(), referrer4.getLoginName());
        referrerRelationService.generateRelation(referrer1.getLoginName(), referrer2.getLoginName());

        staffRecommendRoleService.generateStaffRole(referrer5.getLoginName(), user1.getLoginName());

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer1.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer2.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer3.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer4.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(referrer5.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user1.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user2.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user3.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user4.getLoginName(), Role.ZC_STAFF_RECOMMEND));

        assertNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.NOT_STAFF_RECOMMEND));
        assertNotNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.SD_STAFF_RECOMMEND));
        assertNull(userRoleMapper.findByLoginNameAndRole(user5.getLoginName(), Role.ZC_STAFF_RECOMMEND));
    }

    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword(loginName);
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
