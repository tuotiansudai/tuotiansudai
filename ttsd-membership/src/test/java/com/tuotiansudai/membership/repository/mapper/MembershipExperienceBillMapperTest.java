package com.tuotiansudai.membership.repository.mapper;

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
public class MembershipExperienceBillMapperTest {

    @Autowired
    private MembershipExperienceBillMapper membershipExperienceBillMapper;

    @Autowired
    private FakeUserHelper userMapper;

    @Test
    public void shouldCreateMembershipExperienceBill() throws Exception {

        UserModel fakeUser = createFakeUser();
        MembershipExperienceBillModel membershipExperienceBillModel = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 5000, 10000, "出借了5000.增加5000成长值");

        membershipExperienceBillMapper.create(membershipExperienceBillModel);

        assertThat(membershipExperienceBillModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipExperienceBillModel.getExperience(), is(5000L));
        assertThat(membershipExperienceBillModel.getTotalExperience(), is(10000L));
        assertThat(membershipExperienceBillModel.getDescription(), is("出借了5000.增加5000成长值"));
    }

    @Test
    public void shouldUpdateMembershipExperienceBill() throws Exception {

        UserModel fakeUser = createFakeUser();
        MembershipExperienceBillModel membershipExperienceBillModel = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 5000, 10000, "出借了5000.增加5000成长值");
        membershipExperienceBillMapper.create(membershipExperienceBillModel);

        MembershipExperienceBillModel updateMembershipExperienceBillModel = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 50000, 80000, "出借了50000.增加50000成长值");
        membershipExperienceBillMapper.update(updateMembershipExperienceBillModel);

        assertThat(updateMembershipExperienceBillModel.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(updateMembershipExperienceBillModel.getExperience(), is(50000L));
        assertThat(updateMembershipExperienceBillModel.getTotalExperience(), is(80000L));
        assertThat(updateMembershipExperienceBillModel.getDescription(), is("出借了50000.增加50000成长值"));
    }

    @Test
    public void shouldMembershipExperienceBillFindById() throws Exception {

        UserModel fakeUser = createFakeUser();
        MembershipExperienceBillModel membershipExperienceBillModel = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 5000, 10000, "出借了5000.增加5000成长值,累计成长值10000");
        membershipExperienceBillMapper.create(membershipExperienceBillModel);

        MembershipExperienceBillModel membershipExperienceBillModel1 = membershipExperienceBillMapper.findById(membershipExperienceBillModel.getId());

        assertThat(membershipExperienceBillModel1.getLoginName(), is(fakeUser.getLoginName()));
        assertThat(membershipExperienceBillModel1.getExperience(), is(5000L));
        assertThat(membershipExperienceBillModel1.getTotalExperience(), is(10000L));
        assertThat(membershipExperienceBillModel1.getDescription(), is("出借了5000.增加5000成长值,累计成长值10000"));

    }

    @Test
    public void shouldMembershipExperienceBillListByLoginName() throws Exception{
        UserModel fakeUser = createFakeUser();
        MembershipExperienceBillModel membershipExperienceBillModel1 = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 5000, 10000, "出借了5000.增加5000成长值");
        MembershipExperienceBillModel membershipExperienceBillModel2 = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 10000, 20000, "出借了10000.增加10000成长值");
        MembershipExperienceBillModel membershipExperienceBillModel3 = new MembershipExperienceBillModel(fakeUser.getLoginName(), null, 20000, 20000, "出借了5000.增加5000成长值");
        membershipExperienceBillMapper.create(membershipExperienceBillModel1);
        membershipExperienceBillMapper.create(membershipExperienceBillModel2);
        membershipExperienceBillMapper.create(membershipExperienceBillModel3);

        List<MembershipExperienceBillModel> membershipExperienceBillModelList = membershipExperienceBillMapper.findMembershipExperienceBillByLoginName(fakeUser.getLoginName(), null, null, 0, 10);

        long membershipExperienceBillCount = membershipExperienceBillMapper.findMembershipExperienceBillCountByLoginName(fakeUser.getLoginName(), null, null);

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
