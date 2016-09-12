package com.tuotiansudai.activity.service;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional(value = "activityTransactionManager")
public class MidAutumnActivityServiceTest {

    @InjectMocks
    private MidAutumnActivityService midAutumnActivityService;

    @Mock
    private AutumnService autumnService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private InvestMapper investMapper;

    @Ignore
    public void shouldGetMidAutumnHomeDataIsOk(){
        String loginName = "boss";
        Date activityAutumnStartTime = DateTime.now().withTimeAtStartOfDay().toDate();
        Date activityAutumnEndTime = DateUtils.addMilliseconds(DateTime.now().plusDays(1).toDate(), -1000);
        ReflectionTestUtils.setField(midAutumnActivityService, "activityMinAutumnStartTime", activityAutumnStartTime);
        ReflectionTestUtils.setField(midAutumnActivityService, "activityMinAutumnEndTime", activityAutumnEndTime);
        Map<String,List<String>> allFamily = Maps.newConcurrentMap();
        allFamily.put("团员1号家庭", Lists.newArrayList("midAutumnA", "midAutumnB"));
        allFamily.put("团员2号家庭", Lists.newArrayList("midAutumnC", "midAutumnD", "midAutumnF"));
        UserModel userModel = new UserModel();
        userModel.setMobile("15210001234");

        when(autumnService.getAllFamilyMap(any(Date.class), any(Date.class))).thenReturn(allFamily);
        when(investMapper.sumInvestAmount(anyLong(), anyString(), anyString(), any(Source.class), anyString(), any(Date.class), any(Date.class), any(InvestStatus.class), any(LoanStatus.class))).thenReturn(1l);
        when(userMapper.findByLoginName(anyString())).thenReturn(userModel);

        Map autumnMap = midAutumnActivityService.getMidAutumnHomeData(loginName);
        assertTrue(autumnMap.size() > 0);
        assertEquals(autumnMap.get("myFamilyNum"), 2);
        assertTrue(CollectionUtils.isNotEmpty((Collection) autumnMap.get("myFamily")));
        assertEquals(autumnMap.get("todayInvestAmount"), "2.00");
        assertEquals(autumnMap.get("totalInvestAmount"), "10.00");
    }

}
