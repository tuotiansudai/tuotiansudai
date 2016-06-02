package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipMapperTest {

    @Autowired
    private MembershipMapper membershipMapper;

    @Test
    public void shouldCreateMembership() throws Exception {
        MembershipModel membershipModel = new MembershipModel("V0", 0, 0.1);
        membershipMapper.create(membershipModel);

        assertThat(membershipModel.getLevel(), is("V0"));
        assertThat(membershipModel.getExperience(), is(0L));
        assertThat(membershipModel.getFee(), is(0.1));

    }

    @Test
    public void shouldUpdateMembership() throws Exception {
        MembershipModel membershipModel = new MembershipModel("V0", 0, 0.1);
        membershipMapper.create(membershipModel);

        MembershipModel updateMembershipModel = new MembershipModel("V5", 5000000, 0.07);
        membershipMapper.update(updateMembershipModel);

        assertThat(updateMembershipModel.getLevel(), is("V5"));
        assertThat(updateMembershipModel.getExperience(), is(5000000L));
        assertThat(updateMembershipModel.getFee(), is(0.07));
    }

    @Test
    public void shouldMembershipFindById() throws Exception {
        MembershipModel membershipModel = new MembershipModel("V0", 0, 0.1);
        membershipMapper.create(membershipModel);

        MembershipModel membershipModel1 = membershipMapper.findById(membershipModel.getId());

        assertThat(membershipModel1.getLevel(), is("V0"));
        assertThat(membershipModel1.getExperience(), is(0L));
        assertThat(membershipModel1.getFee(), is(0.1));
    }

}
