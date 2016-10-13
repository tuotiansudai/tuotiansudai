package com.tuotiansudai.membership.repository.mapper;

import com.tuotiansudai.membership.repository.model.MembershipPriceModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MembershipPriceMapperTest {

    @Autowired
    private MembershipPriceMapper membershipPriceMapper;

    @Test
    public void shouldFindAll() throws Exception {
        List<MembershipPriceModel> all = membershipPriceMapper.findAll();

        assertThat(all.size(), is(3));
    }

    @Test
    public void shouldFind() throws Exception {
        MembershipPriceModel membershipPriceModel1 = membershipPriceMapper.find(5, 30);
        MembershipPriceModel membershipPriceModel2 = membershipPriceMapper.find(5, 180);
        MembershipPriceModel membershipPriceModel3 = membershipPriceMapper.find(5, 360);

        assertNotNull(membershipPriceModel1);
        assertNotNull(membershipPriceModel2);
        assertNotNull(membershipPriceModel3);
    }
}
