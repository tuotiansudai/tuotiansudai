package com.tuotiansudai.repository.mapper;


import com.tuotiansudai.repository.model.ReferrerManageView;
import com.tuotiansudai.repository.model.ReferrerRelationView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ReferrerManageMapperTest {

    @Autowired
    private ReferrerManageMapper referrerManageMapper;

//    @Test
    public void shouldGetSomeReferRealtion() throws Exception {

        Calendar start =  Calendar.getInstance();
        start.add(Calendar.DATE, -300);

        List<ReferrerRelationView> referRelationList = referrerManageMapper.findReferRelationList("admin", "zhoubx", start.getTime(), new Date(), null, 0, 10);

        assertNotNull(referRelationList.get(0));

        int referRelationCount = referrerManageMapper.findReferRelationCount("admin", null, start.getTime(), new Date(), null);

        assert(referRelationCount>0);
    }

//    @Test
    public void shouldGetSomeReferInvest() throws Exception {

        Calendar start =  Calendar.getInstance();
        start.add(Calendar.DATE, -300);
        List<ReferrerManageView> referManageList = referrerManageMapper.findReferInvestList("admin", null, start.getTime(), new Date(), null, 0, 10);

        assertNotNull(referManageList.get(0));

        int referInvestCount = referrerManageMapper.findReferInvestCount("admin", "zhoubx", null, new Date(), null);

        assert(referInvestCount>0);
    }



}
