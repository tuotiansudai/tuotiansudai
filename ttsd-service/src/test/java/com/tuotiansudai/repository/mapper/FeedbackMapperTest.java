package com.tuotiansudai.repository.mapper;

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
public class FeedbackMapperTest {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateFeedback() throws Exception {
        UserModel fakeUser = createFakeUser();
        FeedbackModel feedbackModel = new FeedbackModel();
        feedbackModel.setLoginName(fakeUser.getLoginName());
        feedbackModel.setContent("content");
        feedbackModel.setSource(Source.IOS);
        feedbackModel.setType(FeedbackType.opinion);
        feedbackModel.setStatus(ProcessStatus.NOT_DONE);
        feedbackModel.setCreatedTime(new Date());

        feedbackMapper.create(feedbackModel);
        assertNotNull(feedbackModel.getId());

        long findCount = feedbackMapper.findAllCount(fakeUser.getLoginName(), null, null, null, null, null);
        assertEquals(1, findCount);

        List<FeedbackModel> models = feedbackMapper.findAll(fakeUser.getLoginName(), null, null, null, null, null, 0, 3);
        assertEquals(1, models.size());
        assertEquals("content", models.get(0).getContent());

        List<FeedbackModel> modelsEmpty = feedbackMapper.findAll(fakeUser.getLoginName(), null, null, null, null, null, 3, 3);
        assertEquals(0, modelsEmpty.size());
    }

    private UserModel createFakeUser() {
        UserModel model = new UserModel();
        model.setLoginName("loginName");
        model.setPassword("password");
        model.setEmail("loginName@abc.com");
        model.setMobile("13900000000");
        model.setRegisterTime(new Date());
        model.setStatus(UserStatus.ACTIVE);
        model.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(model);
        return model;
    }
}
