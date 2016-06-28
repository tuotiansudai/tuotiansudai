package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ActivityMapperTest {
    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    UserMapper userMapper;

    private UserModel createUserModel(String loginName) {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setPassword("123abc");
        userModel.setEmail("12345@mail.com");
        userModel.setMobile(String.valueOf(RandomStringUtils.randomNumeric(11)));
        userModel.setRegisterTime(new Date());
        userModel.setStatus(UserStatus.ACTIVE);
        userModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));

        userMapper.create(userModel);

        return userModel;
    }

    private ActivityModel createActivityModel(long id, UserModel userModel, Date activatedTime, List<Source> source, ActivityStatus activityStatus) {
        ActivityModel activityModel = new ActivityModel();
        activityModel.setId(id);
        activityModel.setTitle("testTitle");
        activityModel.setWebActivityUrl("testWebActivityUrl");
        activityModel.setAppActivityUrl("testAppActivityUrl");
        activityModel.setDescription("testDescription");
        activityModel.setWebPictureUrl("testWebPictureUrl");
        activityModel.setAppPictureUrl("testAppPictureUrl");
        activityModel.setActivatedTime(activatedTime);
        activityModel.setExpiredTime(DateTime.parse("2016-07-30T01:20").toDate());
        activityModel.setSource(source);
        activityModel.setStatus(activityStatus);
        activityModel.setCreatedBy(userModel.getLoginName());
        activityModel.setCreatedTime(DateTime.parse("2016-04-30T01:20").toDate());
        activityModel.setUpdatedBy(userModel.getLoginName());
        activityModel.setUpdatedTime(DateTime.parse("2016-05-30T01:20").toDate());
        activityModel.setActivatedBy(userModel.getLoginName());

        activityMapper.create(activityModel);

        return activityModel;
    }

    private List<ActivityModel> prepareData() {
        UserModel userModel = createUserModel("testUser");
        createUserModel("updatedUser");
        List<ActivityModel> activityModels = new ArrayList<>();
        ActivityModel activityModel = createActivityModel(1L, userModel, DateTime.parse("2016-06-01T01:20").toDate(), Lists.newArrayList(Source.WEB), ActivityStatus.TO_APPROVE);
        activityModels.add(activityModel);
        activityModel = createActivityModel(2L, userModel, DateTime.parse("2016-06-02T01:20").toDate(), Lists.newArrayList(Source.WEB), ActivityStatus.TO_APPROVE);
        activityModels.add(activityModel);
        activityModel = createActivityModel(3L, userModel, DateTime.parse("2016-06-03T01:20").toDate(), Lists.newArrayList(Source.ANDROID), ActivityStatus.OPERATING);
        activityModels.add(activityModel);

        return activityModels;
    }

    @Test
    public void testCreateAndFindById() throws Exception {
        List<ActivityModel> activityModels = prepareData();

        ActivityModel activityModel = activityMapper.findById(1L);
        ActivityModel originalActivityModel = activityModels.get(0);

        assertEquals(originalActivityModel.getId(), activityModel.getId());
        assertEquals(originalActivityModel.getTitle(), activityModel.getTitle());
        assertEquals(originalActivityModel.getWebActivityUrl(), activityModel.getWebActivityUrl());
        assertEquals(originalActivityModel.getAppActivityUrl(), activityModel.getAppActivityUrl());
        assertEquals(originalActivityModel.getDescription(), activityModel.getDescription());
        assertEquals(originalActivityModel.getWebPictureUrl(), activityModel.getWebPictureUrl());
        assertEquals(originalActivityModel.getAppPictureUrl(), activityModel.getAppPictureUrl());
        assertEquals(originalActivityModel.getActivatedTime(), activityModel.getActivatedTime());
        assertEquals(originalActivityModel.getExpiredTime(), activityModel.getExpiredTime());
        assertEquals(originalActivityModel.getSource(), activityModel.getSource());
        assertEquals(originalActivityModel.getStatus(), activityModel.getStatus());
        assertEquals(originalActivityModel.getCreatedBy(), activityModel.getCreatedBy());
        assertEquals(originalActivityModel.getCreatedTime(), activityModel.getCreatedTime());
        assertEquals(originalActivityModel.getUpdatedBy(), activityModel.getUpdatedBy());
        assertEquals(originalActivityModel.getUpdatedTime(), activityModel.getUpdatedTime());
        assertEquals(originalActivityModel.getActivatedBy(), activityModel.getActivatedBy());
    }

    @Test
    public void testUpdate() throws Exception {
        prepareData();

        ActivityModel updatedActivityModel = new ActivityModel();
        updatedActivityModel.setId(1L);
        updatedActivityModel.setTitle("Title");
        updatedActivityModel.setWebActivityUrl("WebActivityUrl");
        updatedActivityModel.setAppActivityUrl("AppActivityUrl");
        updatedActivityModel.setDescription("Description");
        updatedActivityModel.setWebPictureUrl("WebPictureUrl");
        updatedActivityModel.setAppPictureUrl("AppPictureUrl");
        updatedActivityModel.setActivatedTime(DateTime.parse("2015-07-30T01:20").toDate());
        updatedActivityModel.setExpiredTime(DateTime.parse("2015-07-30T01:20").toDate());
        updatedActivityModel.setSource(Lists.newArrayList(Source.IOS));
        updatedActivityModel.setStatus(ActivityStatus.REJECTION);
        updatedActivityModel.setCreatedBy("updatedUser");
        updatedActivityModel.setCreatedTime(DateTime.parse("2015-04-30T01:20").toDate());
        updatedActivityModel.setUpdatedBy("updatedUser");
        updatedActivityModel.setUpdatedTime(DateTime.parse("2015-05-30T01:20").toDate());
        updatedActivityModel.setActivatedBy("updatedUser");

        activityMapper.update(updatedActivityModel);

        ActivityModel activityModel = activityMapper.findById(1l);

        assertEquals(updatedActivityModel.getId(), activityModel.getId());
        assertEquals(updatedActivityModel.getTitle(), activityModel.getTitle());
        assertEquals(updatedActivityModel.getWebActivityUrl(), activityModel.getWebActivityUrl());
        assertEquals(updatedActivityModel.getAppActivityUrl(), activityModel.getAppActivityUrl());
        assertEquals(updatedActivityModel.getDescription(), activityModel.getDescription());
        assertEquals(updatedActivityModel.getWebPictureUrl(), activityModel.getWebPictureUrl());
        assertEquals(updatedActivityModel.getAppPictureUrl(), activityModel.getAppPictureUrl());
        assertEquals(updatedActivityModel.getActivatedTime(), activityModel.getActivatedTime());
        assertEquals(updatedActivityModel.getExpiredTime(), activityModel.getExpiredTime());
        assertEquals(updatedActivityModel.getSource(), activityModel.getSource());
        assertEquals(updatedActivityModel.getStatus(), activityModel.getStatus());
        assertEquals(updatedActivityModel.getCreatedBy(), activityModel.getCreatedBy());
        assertEquals(updatedActivityModel.getCreatedTime(), activityModel.getCreatedTime());
        assertEquals(updatedActivityModel.getUpdatedBy(), activityModel.getUpdatedBy());
        assertEquals(updatedActivityModel.getUpdatedTime(), activityModel.getUpdatedTime());
        assertEquals(updatedActivityModel.getActivatedBy(), activityModel.getActivatedBy());
    }

    @Test
    public void testFindActivities() throws Exception {
        List<ActivityModel> originalActivityModels = prepareData();

        List<ActivityModel> activityModels = activityMapper.findActivities(null, null, null, null);
        assertEquals(3, activityModels.size());

        activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, Source.ANDROID);
        assertEquals(1, activityModels.size());
        ActivityModel originalActivityModel = originalActivityModels.get(2);
        ActivityModel searchActivityModel = activityModels.get(0);
        assertEquals(originalActivityModel.getId(), searchActivityModel.getId());
        assertEquals(originalActivityModel.getTitle(), searchActivityModel.getTitle());
        assertEquals(originalActivityModel.getWebActivityUrl(), searchActivityModel.getWebActivityUrl());
        assertEquals(originalActivityModel.getAppActivityUrl(), searchActivityModel.getAppActivityUrl());
        assertEquals(originalActivityModel.getDescription(), searchActivityModel.getDescription());
        assertEquals(originalActivityModel.getWebPictureUrl(), searchActivityModel.getWebPictureUrl());
        assertEquals(originalActivityModel.getAppPictureUrl(), searchActivityModel.getAppPictureUrl());
        assertEquals(originalActivityModel.getActivatedTime(), searchActivityModel.getActivatedTime());
        assertEquals(originalActivityModel.getExpiredTime(), searchActivityModel.getExpiredTime());
        assertEquals(originalActivityModel.getSource(), searchActivityModel.getSource());
        assertEquals(originalActivityModel.getStatus(), searchActivityModel.getStatus());
        assertEquals(originalActivityModel.getCreatedBy(), searchActivityModel.getCreatedBy());
        assertEquals(originalActivityModel.getCreatedTime(), searchActivityModel.getCreatedTime());
        assertEquals(originalActivityModel.getUpdatedBy(), searchActivityModel.getUpdatedBy());
        assertEquals(originalActivityModel.getUpdatedTime(), searchActivityModel.getUpdatedTime());
        assertEquals(originalActivityModel.getActivatedBy(), searchActivityModel.getActivatedBy());

        activityModels = activityMapper.findActivities(DateTime.parse("2016-06-02T02:20").toDate(), null, null, null);
        assertEquals(1, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertTrue(activityModel.getActivatedTime().after(DateTime.parse("2016-06-02T02:20").toDate()));
        }

        activityModels = activityMapper.findActivities(null, DateTime.parse("2016-06-02T02:20").toDate(), null, null);
        assertEquals(2, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertTrue(activityModel.getActivatedTime().before(DateTime.parse("2016-06-02T02:20").toDate()));
        }

        activityModels = activityMapper.findActivities(null, null, ActivityStatus.OPERATING, null);
        assertEquals(1, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertEquals(ActivityStatus.OPERATING, activityModel.getStatus());
        }

        activityModels = activityMapper.findActivities(null, null, null, Source.ANDROID);
        assertEquals(1, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertEquals(Source.ANDROID, activityModel.getSource().get(0));
        }

        activityModels = activityMapper.findActivities(DateTime.parse("2016-06-01T00:20").toDate(), DateTime.parse("2016-06-01T02:20").toDate(), null, null);
        assertEquals(1, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertTrue(activityModel.getActivatedTime().after(DateTime.parse("2016-06-01T00:20").toDate()));
            assertTrue(activityModel.getActivatedTime().before(DateTime.parse("2016-06-01T02:20").toDate()));
        }

        assertEquals(0, activityMapper.findActivities(null, null, ActivityStatus.TO_APPROVE, Source.ANDROID).size());
        assertEquals(0, activityMapper.findActivities(null, null, null, Source.IOS).size());
        assertEquals(0, activityMapper.findActivities(null, null, ActivityStatus.REJECTION, null).size());
        assertEquals(0, activityMapper.findActivities(DateTime.parse("2017-05-02T02:20").toDate(), null, ActivityStatus.TO_APPROVE, Source.WEB).size());
        assertEquals(0, activityMapper.findActivities(null, DateTime.parse("2014-05-02T02:20").toDate(), ActivityStatus.TO_APPROVE, Source.WEB).size());
    }
}
