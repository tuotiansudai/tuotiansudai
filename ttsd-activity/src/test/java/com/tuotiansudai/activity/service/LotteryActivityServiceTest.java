package com.tuotiansudai.activity.service;


import com.tuotiansudai.repository.mapper.ReferrerRelationMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LotteryActivityServiceTest {

    @Autowired
    private LotteryActivityService lotteryActivityService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;



    public void shouldGetDrawPrizeTimeIsOk(){
        String loginName = "testDrawPrize";
        String refferrerName = "testRefferrerName";
        Date activityAutumnStartTime = DateUtils.addMonths(DateTime.now().toDate(),-1);
        Date activityAutumnEndTime = DateUtils.addMonths(DateTime.now().toDate(),1);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnStartTime" ,activityAutumnStartTime);
        ReflectionTestUtils.setField(lotteryActivityService, "activityAutumnEndTime" ,activityAutumnEndTime);
        getFakeUser(loginName);
        getFakeUser(refferrerName);
    }


    private UserModel getFakeUser(String loginName) {
        UserModel fakeUser = new UserModel();
        fakeUser.setLoginName(loginName);
        fakeUser.setPassword("password");
        fakeUser.setEmail("fakeUsr@tuotiansudai.com");
        fakeUser.setMobile("11900000000");
        fakeUser.setRegisterTime(new Date());
        fakeUser.setStatus(UserStatus.ACTIVE);
        fakeUser.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(fakeUser);
        return fakeUser;
    }
}
