package com.tuotiansudai.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.UserBillDto;
import com.tuotiansudai.repository.model.UserBillBusinessType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Administrator on 2015/9/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class UserBillServiceTest {

    @Autowired
    private UserBillService userBillService;

    @Test
    public void findUserBillsTest() {
        List<UserBillBusinessType> billBusinessTypes = Lists.newArrayList();
        billBusinessTypes.add(UserBillBusinessType.ACTIVITY_REWARD);
        billBusinessTypes.add(UserBillBusinessType.ADVANCE_REPAY);
        int count = userBillService.findUserBillsCount(billBusinessTypes,new Date(),new Date());
        List<UserBillDto> userBillDtos = userBillService.findUserBills(billBusinessTypes, 1, new Date(), new Date(), 10);
        assertTrue(userBillDtos.size() == count);
    }

}
