package com.tuotiansudai.activity.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.model.ActivityModel;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
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
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ActivityMapperTest {
    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    FakeUserHelper userMapper;

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

    private ActivityModel createActivityModel(UserModel userModel, String activityTitle, Date activatedTime, Date expiredTime, List<Source> source, ActivityStatus activityStatus, boolean longTerm) {
        ActivityModel activityModel = new ActivityModel();
        activityModel.setTitle(activityTitle);
        activityModel.setSeq(10l);
        activityModel.setWebActivityUrl("testWebActivityUrl");
        activityModel.setAppActivityUrl("testAppActivityUrl");
        activityModel.setDescription("testDescription");
        activityModel.setWebPictureUrl("testWebPictureUrl");
        activityModel.setAppPictureUrl("testAppPictureUrl");
        activityModel.setAppVerticalPictureUrl("testWebPictureUrl");
        activityModel.setActivatedTime(activatedTime);
        activityModel.setExpiredTime(expiredTime);
        activityModel.setSource(source);
        activityModel.setStatus(activityStatus);
        activityModel.setCreatedBy(userModel.getLoginName());
        activityModel.setCreatedTime(DateTime.parse("2016-04-30T01:20").toDate());
        activityModel.setUpdatedBy(userModel.getLoginName());
        activityModel.setUpdatedTime(DateTime.parse("2016-05-30T01:20").toDate());
        activityModel.setActivatedBy(userModel.getLoginName());
        activityModel.setShareTitle("title");
        activityModel.setShareUrl("shareUrl");
        activityModel.setShareContent("content");
        activityModel.setLongTerm(longTerm);

        activityMapper.create(activityModel);

        return activityModel;
    }

    private List<ActivityModel> prepareData(String loginName, String updateLoginName) {
        UserModel userModel = createUserModel(loginName);
        createUserModel(updateLoginName);
        List<ActivityModel> activityModels = new ArrayList<>();
        ActivityModel activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-06-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate(), Lists.newArrayList(Source.WEB), ActivityStatus.TO_APPROVE, false);
        activityModels.add(activityModel);
        activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-06-02T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate(), Lists.newArrayList(Source.WEB), ActivityStatus.TO_APPROVE, false);
        activityModels.add(activityModel);
        activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-06-03T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate(), Lists.newArrayList(Source.ANDROID), ActivityStatus.OPERATING, false);
        activityModels.add(activityModel);
        activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-07-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.WEB), ActivityStatus.APPROVED, false);
        activityModels.add(activityModel);
        activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-07-01T01:20").toDate(), DateTime.parse("2000-06-01T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.WEB), ActivityStatus.APPROVED, false);
        activityModels.add(activityModel);
        activityModel = createActivityModel(userModel, "testTitle", DateTime.parse("2016-07-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate(), Lists.newArrayList(Source.WEB), ActivityStatus.APPROVED, false);
        activityModels.add(activityModel);

        return activityModels;
    }

    @Test
    public void testCreateAndFindById() throws Exception {
        List<ActivityModel> activityModels = prepareData("testActivityUser1", "testUpdateActivityUser1");

        ActivityModel activityModel = activityMapper.findById(activityModels.get(0).getId());
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
        List<ActivityModel> activityModels = prepareData("testActivityUser2", "testUpdateActivityUser2");

        ActivityModel updatedActivityModel = new ActivityModel();
        updatedActivityModel.setId(activityModels.get(0).getId());
        updatedActivityModel.setTitle("Title");
        updatedActivityModel.setWebActivityUrl("WebActivityUrl");
        updatedActivityModel.setAppActivityUrl("AppActivityUrl");
        updatedActivityModel.setDescription("Description");
        updatedActivityModel.setWebPictureUrl("WebPictureUrl");
        updatedActivityModel.setAppPictureUrl("AppPictureUrl");
        updatedActivityModel.setAppVerticalPictureUrl("AppPictureUrl");
        updatedActivityModel.setActivatedTime(DateTime.parse("2015-07-30T01:20").toDate());
        updatedActivityModel.setExpiredTime(DateTime.parse("2015-07-30T01:20").toDate());
        updatedActivityModel.setSource(Lists.newArrayList(Source.IOS));
        updatedActivityModel.setStatus(ActivityStatus.REJECTION);
        updatedActivityModel.setCreatedBy("updatedUser");
        updatedActivityModel.setCreatedTime(DateTime.parse("2015-04-30T01:20").toDate());
        updatedActivityModel.setUpdatedBy("updatedUser");
        updatedActivityModel.setUpdatedTime(DateTime.parse("2015-05-30T01:20").toDate());
        updatedActivityModel.setActivatedBy("updatedUser");
        updatedActivityModel.setShareContent("content");
        updatedActivityModel.setShareTitle("title");
        updatedActivityModel.setShareUrl("url");
        updatedActivityModel.setSeq(10l);
        activityMapper.update(updatedActivityModel);

        ActivityModel activityModel = activityMapper.findById(activityModels.get(0).getId());

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
    public void testFindActiveActivities() throws Exception {
        prepareData("testActivityUser3", "testUpdateActivityUser3");
        List<ActivityModel> activityModels = activityMapper.findActiveActivities(Source.WEB, DateTime.parse("1999-1-1").toDate(), 0, 10);
        assertEquals(3, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertTrue(activityModel.getSource().contains(Source.WEB));
        }
        activityModels = activityMapper.findActiveActivities(Source.ANDROID, DateTime.parse("1999-1-1").toDate(), 0, 10);
        assertEquals(2, activityModels.size());
        for (ActivityModel activityModel : activityModels) {
            assertTrue(activityModel.getSource().contains(Source.ANDROID));
        }
        activityModels = activityMapper.findActiveActivities(Source.WEB, DateTime.parse("1999-1-1").toDate(), 0, 1);
        assertEquals(1, activityModels.size());
        activityModels = activityMapper.findActiveActivities(Source.WEB, DateTime.parse("2999-1-1").toDate(), 0, 1);
        assertEquals(1, activityModels.size());
    }

    @Test
    public void shouldFindLongTermActivities() throws Exception {
        UserModel activityUser = createUserModel("activityUser");
        ActivityModel longTermActivity = createActivityModel(activityUser, "longTermActivity", null, null, Lists.newArrayList(Source.WEB), ActivityStatus.APPROVED, true);

        List<ActivityModel> allActivities = activityMapper.findAllActivities(new Date(), new Date(), null, null);

        assertTrue(allActivities.stream().anyMatch(activityModel -> activityModel.isLongTerm() && activityModel.getTitle().equals(longTermActivity.getTitle())));

    }
}
