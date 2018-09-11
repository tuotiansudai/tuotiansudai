package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.ActivityMapper;
import com.tuotiansudai.activity.repository.model.ActivityModel;
import com.tuotiansudai.activity.repository.model.ActivityStatus;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppActivityService;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.FakeUserHelper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MobileAppActivityServiceTest extends ServiceTestBase {
    @Autowired
    InvestMapper investMapper;

    @Autowired
    LoanMapper loanMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    FakeUserHelper userMapper;

    @Autowired
    MobileAppActivityService mobileAppActivityService;

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

    private ActivityModel createActivityModel(long id, UserModel userModel, String description, Date activatedTime, List<Source> source) {
        ActivityModel activityModel = new ActivityModel();
        activityModel.setId(id);
        activityModel.setSeq(10L);
        activityModel.setTitle(description);
        activityModel.setWebActivityUrl("testWebActivityUrl");
        activityModel.setAppActivityUrl("testAppActivityUrl");
        activityModel.setDescription(description);
        activityModel.setWebPictureUrl("testWebPictureUrl");
        activityModel.setAppPictureUrl("testAppPictureUrl");
        activityModel.setAppVerticalPictureUrl("testAppVerticalPictureUrl");
        activityModel.setActivatedTime(activatedTime);
        activityModel.setExpiredTime(DateTime.parse("2040-07-30T01:20").toDate());
        activityModel.setSource(source);
        activityModel.setStatus(ActivityStatus.APPROVED);
        activityModel.setCreatedBy(userModel.getLoginName());
        activityModel.setCreatedTime(DateTime.parse("2016-04-30T01:20").toDate());
        activityModel.setUpdatedBy(userModel.getLoginName());
        activityModel.setUpdatedTime(DateTime.parse("2016-05-30T01:20").toDate());
        activityModel.setActivatedBy(userModel.getLoginName());
        activityModel.setShareUrl("shareUrl");
        activityModel.setShareContent("content");
        activityModel.setShareTitle("title");

        activityMapper.create(activityModel);

        return activityModel;
    }

    private List<ActivityModel> prepareData(String loginName) {
        UserModel userModel = createUserModel(loginName);
        long loanId = IdGenerator.generate();
        createLoanModel(userModel.getLoginName(), loanId);
        createInvests(userModel.getLoginName(), loanId);

        List<ActivityModel> activityModels = new ArrayList<>();
        ActivityModel activityModel = createActivityModel(1L, userModel, "normal1", DateTime.parse("2016-06-09T01:20").toDate(), Lists.newArrayList(Source.ANDROID));
        activityModels.add(activityModel);
        activityModel = createActivityModel(5L, userModel, "新手1", DateTime.parse("2016-06-08T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityModels.add(activityModel);
        activityModel = createActivityModel(4L, userModel, "normal4", DateTime.parse("2016-06-03T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityModels.add(activityModel);
        activityModel = createActivityModel(6L, userModel, "新手2", DateTime.parse("2016-06-06T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityModels.add(activityModel);
        activityModel = createActivityModel(3L, userModel, "normal3", DateTime.parse("2016-06-05T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityModels.add(activityModel);
        activityModel = createActivityModel(7L, userModel, "新手3", DateTime.parse("2016-06-04T01:20").toDate(), Lists.newArrayList(Source.ANDROID, Source.IOS));
        activityModels.add(activityModel);
        activityModel = createActivityModel(2L, userModel, "normal2", DateTime.parse("2016-06-07T01:20").toDate(), Lists.newArrayList(Source.IOS));
        activityModels.add(activityModel);

        return activityModels;
    }

    @Test
    public void testGetAppActivityCenterResponseData() {
        prepareData("testApiActivityUser1");
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("testUser");
        baseParam.setPlatform(Source.IOS.toString());
        ActivityCenterRequestDto requestDto = new ActivityCenterRequestDto();
        requestDto.setBaseParam(baseParam);
        requestDto.setActivityType(ActivityCenterType.CURRENT);
        ActivityCenterResponseDto activityCenterResponseDto = mobileAppActivityService.getAppActivityCenterResponseData(requestDto);
        assertEquals(1, activityCenterResponseDto.getIndex().intValue());
        assertEquals(10, activityCenterResponseDto.getPageSize().intValue());

        List<ActivityCenterDataDto> activityCenterDataDtos = activityCenterResponseDto.getActivities();
        assertEquals("新手1", activityCenterDataDtos.get(0).getDescTitle());
        assertEquals("normal4", activityCenterDataDtos.get(1).getDescTitle());
        assertEquals("新手2", activityCenterDataDtos.get(2).getDescTitle());
        assertEquals("normal3", activityCenterDataDtos.get(3).getDescTitle());
        assertTrue(activityCenterDataDtos.get(0).getVerticalImageUrl().indexOf("testAppVerticalPictureUrl") != -1);
    }
}
