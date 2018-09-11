package com.tuotiansudai.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.ActivityDto;
import com.tuotiansudai.activity.repository.mapper.ActivityMapper;
import com.tuotiansudai.activity.repository.model.ActivityModel;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})@Transactional
public class ActivityServiceTest {
    @Autowired
    InvestMapper investMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    ActivityService activityService;

    @Autowired
    private MidSummerService midSummerService;

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

    private LoanModel createLoanModel(String loginName, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(loginName);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(loginName);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);

        return loanModel;
    }

    private void createInvests(String loginName, long loanId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -98);
        for (int i = 10000000; i < 10099000; i += 1000) {
            cal.add(Calendar.SECOND, 1);
            InvestModel model = new InvestModel(IdGenerator.generate(), loanId, null, loginName, 1, 0.1, false, new Date(), Source.WEB, null);
            model.setStatus(InvestStatus.SUCCESS);
            investMapper.create(model);
        }
    }

    private ActivityModel createActivityModel(long id, UserModel userModel, String title, Date activatedTime, Date expiredTime) {
        ActivityModel activityModel = new ActivityModel();
        activityModel.setId(id);
        activityModel.setTitle(title);
        activityModel.setSeq(10l);
        activityModel.setWebActivityUrl("testWebActivityUrl");
        activityModel.setAppActivityUrl("testAppActivityUrl");
        activityModel.setDescription("testDescription");
        activityModel.setWebPictureUrl("testWebPictureUrl");
        activityModel.setAppPictureUrl("testAppPictureUrl");
        activityModel.setAppVerticalPictureUrl("testAppPictureUrl");
        activityModel.setActivatedTime(activatedTime);
        activityModel.setExpiredTime(expiredTime);
        activityModel.setSource(Lists.newArrayList(Source.WEB));
        activityModel.setStatus(ActivityStatus.APPROVED);
        activityModel.setCreatedBy(userModel.getLoginName());
        activityModel.setCreatedTime(DateTime.parse("2016-04-30T01:20").toDate());
        activityModel.setUpdatedBy(userModel.getLoginName());
        activityModel.setUpdatedTime(DateTime.parse("2016-05-30T01:20").toDate());
        activityModel.setActivatedBy(userModel.getLoginName());

        activityModel.setShareUrl("url");
        activityModel.setShareContent("content");
        activityModel.setShareTitle("title");

        activityMapper.create(activityModel);

        return activityModel;
    }

    private List<ActivityModel> prepareData() {
        UserModel userModel = createUserModel("testEditRecheckUser1");
        long loanId = IdGenerator.generate();
        createLoanModel(userModel.getLoginName(), loanId);
        createInvests(userModel.getLoginName(), loanId);

        List<ActivityModel> activityModels = new ArrayList<>();
        ActivityModel activityModel = createActivityModel(1L, userModel, "activity1", DateTime.parse("2016-06-03T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        activityModels.add(activityModel);
        activityModel = createActivityModel(5L, userModel, "activity2", DateTime.parse("2016-06-02T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        activityModels.add(activityModel);
        activityModel = createActivityModel(4L, userModel, "activity3", DateTime.parse("2016-06-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        activityModels.add(activityModel);
        return activityModels;
    }

    @Test
    public void testGetAllOperatingActivities() throws Exception {
        prepareData();

        List<ActivityDto> activityDtoList = activityService.getAllActiveActivities("testUser", Source.WEB);
        assertEquals(3, activityDtoList.size());
        assertEquals("activity1", activityDtoList.get(0).getTitle());
        assertEquals("activity2", activityDtoList.get(1).getTitle());
        assertEquals("activity3", activityDtoList.get(2).getTitle());
    }

    private ActivityDto fakeActivityDto(String loginName, ActivityStatus activityStatus) {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setActivityId(IdGenerator.generate());
        activityDto.setTitle("title");
        activityDto.setSeq(10l);
        activityDto.setWebActivityUrl("WebActivityUrl");
        activityDto.setAppActivityUrl("AppActivityUrl");
        activityDto.setWebPictureUrl("WebPictureUrl");
        activityDto.setAppPictureUrl("AppPictureUrl");
        activityDto.setAppVerticalPictureUrl("AppPictureUrl");
        activityDto.setExpiredTime(new DateTime().withTimeAtStartOfDay().toDate());
        activityDto.setSource(Lists.newArrayList(Source.ANDROID));
        activityDto.setStatus(activityStatus);
        activityDto.setCreatedBy(loginName);
        activityDto.setCreatedTime(new Date());
        activityDto.setDescription("description");
        activityDto.setShareContent("content");
        activityDto.setShareTitle("title");
        activityDto.setShareUrl("url");
        return activityDto;
    }

    @Test
    public void shouldCreateEditRecheckActivityByCreate() {
        UserModel userModel = createUserModel("testEditRecheckUser2");
        ActivityDto activityDto = fakeActivityDto(userModel.getLoginName(), ActivityStatus.TO_APPROVE);

        activityService.saveOrUpdate(activityDto, ActivityStatus.TO_APPROVE, userModel.getLoginName(), "");

        ActivityModel activityModelCreate = activityMapper.findById(activityDto.getActivityId());

        assertEquals(activityDto.getActivityId(), activityModelCreate.getId());
        assertEquals(activityDto.getAppActivityUrl(), activityModelCreate.getAppActivityUrl());
        assertEquals(activityDto.getWebActivityUrl(), activityModelCreate.getWebActivityUrl());
        assertEquals(activityDto.getAppPictureUrl(), activityModelCreate.getAppPictureUrl());
        assertEquals(activityDto.getWebPictureUrl(), activityModelCreate.getWebPictureUrl());
        assertEquals(activityDto.getDescription(), activityModelCreate.getDescription());
        assertEquals(activityDto.getTitle(), activityModelCreate.getTitle());
        assertEquals(activityDto.getStatus(), activityModelCreate.getStatus());

    }

    @Test
    public void shouldCreateEditRecheckActivityByEdit() {
        UserModel userModel = createUserModel("testEditRecheckUser3");
        ActivityModel activityModel = createActivityModel(1L, userModel, "normal1", DateTime.parse("2016-06-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        ActivityDto activityDto = new ActivityDto(activityModel);

        activityDto.setAppPictureUrl("AppPictureUrlEdit");
        activityDto.setSource(Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityService.saveOrUpdate(activityDto, ActivityStatus.TO_APPROVE, userModel.getLoginName(), "");

        ActivityModel activityModelCreate = activityMapper.findById(activityDto.getActivityId());

        assertEquals(activityDto.getActivityId(), activityModelCreate.getId());
        assertEquals(activityDto.getAppActivityUrl(), activityModelCreate.getAppActivityUrl());
        assertEquals(activityDto.getWebActivityUrl(), activityModelCreate.getWebActivityUrl());
        assertEquals(activityDto.getAppPictureUrl(), activityModelCreate.getAppPictureUrl());
        assertEquals(activityDto.getWebPictureUrl(), activityModelCreate.getWebPictureUrl());
        assertEquals(activityDto.getDescription(), activityModelCreate.getDescription());
        assertEquals(activityDto.getTitle(), activityModelCreate.getTitle());
        assertEquals(ActivityStatus.TO_APPROVE, activityModelCreate.getStatus());

    }

    @Test
    public void shouldCreateEditRecheckActivityByRejection() {
        UserModel userModel = createUserModel("testEditRecheckUser4");
        ActivityModel activityModel = createActivityModel(1L, userModel, "normal1", DateTime.parse("2016-06-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        ActivityDto activityDto = new ActivityDto(activityModel);
        activityService.saveOrUpdate(activityDto, ActivityStatus.REJECTION, userModel.getLoginName(), "");
        ActivityModel activityModelCreate = activityMapper.findById(activityDto.getActivityId());

        assertEquals(ActivityStatus.REJECTION, activityModelCreate.getStatus());
    }

    @Test
    public void shouldCreateEditRecheckActivityByRecheck() {
        UserModel userModel = createUserModel("testEditRecheckUser5");
        ActivityModel activityModel = createActivityModel(1L, userModel, "normal1", DateTime.parse("2016-06-01T01:20").toDate(), DateTime.parse("2040-06-01T01:20").toDate());
        ActivityDto activityDto = new ActivityDto(activityModel);
        activityService.saveOrUpdate(activityDto, ActivityStatus.APPROVED, userModel.getLoginName(), "");
        ActivityModel activityModelCreate = activityMapper.findById(activityDto.getActivityId());

        assertEquals(ActivityStatus.APPROVED, activityModelCreate.getStatus());
    }
}
