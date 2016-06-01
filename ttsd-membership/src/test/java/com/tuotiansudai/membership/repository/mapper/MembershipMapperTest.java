package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipMapperTest {

    @Autowired
    private MembershipMapper membershipMapper;

    @Test
    public void shouldCreateMembership() throws Exception {
        MembershipModel membershipModel = new MembershipModel(1, "V0", 0, 0.1);
        membershipMapper.create(membershipModel);

        MembershipModel membershipModel1 = membershipMapper.findById(1);

        assertNotNull(membershipModel1);
    }

    @Test
    public void shouldUpdateMembership() throws Exception {
        MembershipModel membershipModel = new MembershipModel(1, "V0", 0, 0.1);
        membershipMapper.create(membershipModel);

        MembershipModel updateMembershipModel = new MembershipModel(1, "V1", 5000, 0.1);

        membershipMapper.update(updateMembershipModel);

        MembershipModel afterMembershipModel = membershipMapper.findById(1);

        assertThat(afterMembershipModel.getLevel(), is("V1"));
        assertThat(afterMembershipModel.getExperience(), is(5000L));

    }


}
