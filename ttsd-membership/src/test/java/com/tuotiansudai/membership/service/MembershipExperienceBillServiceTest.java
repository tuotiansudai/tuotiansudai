package com.tuotiansudai.membership.service;

import com.tuotiansudai.membership.repository.mapper.MembershipExperienceBillMapper;
import com.tuotiansudai.membership.repository.model.MembershipExperienceBillModel;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class MembershipExperienceBillServiceTest {

    @Autowired
    private FakeUserHelper userMapper;

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    private MembershipExperienceBillService membershipExperienceBillService;

    @Test
    public void shouldMembershipExperienceBillListByLoginName() throws Exception {
        UserModel fakeUser = createFakeUser();

        MembershipExperienceBillModel membershipExperienceBillModelOne = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 5000, 10000, "出借了5000.增加5000成长值");
        MembershipExperienceBillModel membershipExperienceBillModelTwo = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 10000, 20000, "出借了10000.增加10000成长值");
        MembershipExperienceBillModel membershipExperienceBillModelThree = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 20000, 20000, "出借了5000.增加5000成长值");
        membershipExperienceBillMapper.create(membershipExperienceBillModelOne);
        membershipExperienceBillMapper.create(membershipExperienceBillModelTwo);
        membershipExperienceBillMapper.create(membershipExperienceBillModelThree);

        List<MembershipExperienceBillModel> membershipExperienceBillModelList = membershipExperienceBillService.findMembershipExperienceBillList(fakeUser.getLoginName(), null, null, 1, 10);

        long membershipExperienceBillCount = membershipExperienceBillService.findMembershipExperienceBillCount(fakeUser.getLoginName(), null, null);

        assertThat(membershipExperienceBillModelList.size(), is(3));
        assertThat(membershipExperienceBillCount, is(3L));

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
