package com.tuotiansudai.membership.repository.mapper;

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
    public void shouldMembershipFindById() throws Exception {
        assertThat(membershipMapper.findById(1).getLevel(), is(0));
        assertThat(membershipMapper.findById(2).getLevel(), is(1));
        assertThat(membershipMapper.findById(3).getLevel(), is(2));
        assertThat(membershipMapper.findById(4).getLevel(), is(3));
        assertThat(membershipMapper.findById(5).getLevel(), is(4));
        assertThat(membershipMapper.findById(6).getLevel(), is(5));
    }
}
