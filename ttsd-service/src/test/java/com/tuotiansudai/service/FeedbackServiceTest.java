package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class FeedbackServiceTest {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateFeedbackService() {
        UserModel fakeUser = getFakeUser("loginname");
        userMapper.create(fakeUser);

        FeedbackModel model = feedbackService.create(fakeUser.getLoginName(), "13811112222", Source.IOS, FeedbackType.opinion, "content");
        assertNotNull(model.getId());
        assertNotNull(model.getCreatedTime());

        BasePaginationDataDto<FeedbackModel> paginationDataDto = feedbackService.getFeedbackPagination(fakeUser.getLoginName(), null, null, null, null, null, 1, 3);

        long findCount = paginationDataDto.getCount();
        assertEquals(1, findCount);

        List<FeedbackModel> models = paginationDataDto.getRecords();
        assertEquals(1, models.size());
        assertEquals("content", models.get(0).getContent());
    }

    private UserModel getFakeUser(String loginName) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(loginName);
        userModelTest.setPassword("password");
        userModelTest.setEmail("testbefore@tuotiansudai.com");
        userModelTest.setMobile("11900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

}
