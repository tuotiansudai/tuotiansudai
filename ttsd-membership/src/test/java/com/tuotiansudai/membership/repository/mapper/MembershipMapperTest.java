package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipMapperTest {

    @Autowired
    private MembershipMapper membershipMapper;

    @Test
    public void shouldMembershipFindById() throws Exception {
        assertThat(membershipMapper.findById(1).getLevel(), is(0));
        assertThat(membershipMapper.findById(2).getLevel(), is(1));
        assertThat(membershipMapper.findById(3).getLevel(), is(2));
        assertThat(membershipMapper.findById(4).getLevel(), is(3));
        assertThat(membershipMapper.findById(5).getLevel(), is(4));
        assertThat(membershipMapper.findById(6).getLevel(), is(5));
    }

    @Test
    public void testFindByLevel() throws Exception {
        assertThat(membershipMapper.findByLevel(0).getId(), is(1L));
        assertThat(membershipMapper.findByLevel(1).getId(), is(2L));
        assertThat(membershipMapper.findByLevel(2).getId(), is(3L));
        assertThat(membershipMapper.findByLevel(3).getId(), is(4L));
        assertThat(membershipMapper.findByLevel(4).getId(), is(5L));
        assertThat(membershipMapper.findByLevel(5).getId(), is(6L));
    }

    @Test
    public void testFindAllLevels() throws Exception {
        List<Integer> levels = membershipMapper.findAllLevels();
        assertThat(levels.size(), is(6));
        assertThat(levels.get(0), is(0));
        assertThat(levels.get(1), is(1));
        assertThat(levels.get(2), is(2));
        assertThat(levels.get(3), is(3));
        assertThat(levels.get(4), is(4));
        assertThat(levels.get(5), is(5));
    }

    @Test
    public void shouldMembershipByLevel(){
        MembershipModel membershipModel = createMembership();
        MembershipModel membershipModel1 = membershipMapper.findByLevel(membershipModel.getLevel());

        assertThat(membershipModel1.getFee(), is(0.1));
        assertThat(membershipModel1.getExperience(), is(5000L));
        assertThat(membershipModel1.getLevel(), is(1));
    }

    private MembershipModel createMembership() {
        MembershipModel membershipModel = new MembershipModel();
        membershipModel.setId(100001);
        membershipModel.setExperience(50000);
        membershipModel.setFee(0.1);
        membershipModel.setLevel(1);
        return membershipModel;
    }
}
