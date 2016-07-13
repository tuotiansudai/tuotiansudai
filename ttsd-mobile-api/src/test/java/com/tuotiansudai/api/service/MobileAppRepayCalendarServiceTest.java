package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppRepayCalendarService;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.DateUtil;
import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class MobileAppRepayCalendarServiceTest {

    @Autowired
    private MobileAppRepayCalendarService mobileAppRepayCalendarService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");

    @Test
    public void shouldGetYearRepayCalendarByIsOk(){
        String loginName = "testRepayCalender";
        long loanId = idGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName,loanId);
        InvestModel investModel1 = createInvest(loginName,loanId);
        InvestModel investModel2 = createInvest(loginName,loanId);
        InvestModel investModel3 = createInvest(loginName,loanId);
        InvestModel investModel4 = createInvest(loginName,loanId);
        InvestModel investModel5 = createInvest(loginName,loanId);
        InvestModel investModel6 = createInvest(loginName,loanId);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateUtils.addMonths(DateTime.now().toDate(),-3)));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateUtils.addMonths(DateTime.now().toDate(),-3)));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateUtils.addMonths(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateUtils.addMonths(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateUtils.addMonths(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateUtils.addMonths(DateTime.now().toDate(),-1)));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear(simpleDateFormat.format(DateTime.now().toDate()));
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarListResponseDto> repayCalendarListResponseDtoBaseResponseDto =  mobileAppRepayCalendarService.getYearRepayCalendar(repayCalendarRequestDto);
        List<RepayCalendarResponseDto> repayCalendarResponseDtoList = repayCalendarListResponseDtoBaseResponseDto.getData().getRepayCalendarResponseDtos();
        assertThat(repayCalendarResponseDtoList.size(),is(3));
        assertEquals(repayCalendarResponseDtoList.get(0).getExpectedRepayAmount(),"2");
        assertEquals(repayCalendarResponseDtoList.get(1).getExpectedRepayAmount(),"3");
        assertEquals(repayCalendarResponseDtoList.get(2).getExpectedRepayAmount(),"1");
    }

    @Test
    public void shouldGetMonthRepayCalendarByIsOk(){
        String loginName = "testRepayCalender";
        long loanId = idGenerator.generate();
        createUserByUserId(loginName);
        createAccountByUserId(loginName);
        createLoanByUserId(loginName,loanId);
        InvestModel investModel1 = createInvest(loginName,loanId);
        InvestModel investModel2 = createInvest(loginName,loanId);
        InvestModel investModel3 = createInvest(loginName,loanId);
        InvestModel investModel4 = createInvest(loginName,loanId);
        InvestModel investModel5 = createInvest(loginName,loanId);
        InvestModel investModel6 = createInvest(loginName,loanId);

        List<InvestRepayModel> investRepayModels = new ArrayList<>();
        investRepayModels.add(createInvestRepay(investModel3.getId(), DateUtils.addDays(DateTime.now().toDate(),-3)));
        investRepayModels.add(createInvestRepay(investModel4.getId(), DateUtils.addDays(DateTime.now().toDate(),-3)));
        investRepayModels.add(createInvestRepay(investModel2.getId(), DateUtils.addDays(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel5.getId(), DateUtils.addDays(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel6.getId(), DateUtils.addDays(DateTime.now().toDate(),-2)));
        investRepayModels.add(createInvestRepay(investModel1.getId(), DateUtils.addDays(DateTime.now().toDate(),-1)));

        investRepayMapper.create(investRepayModels);

        RepayCalendarRequestDto repayCalendarRequestDto = new RepayCalendarRequestDto();
        repayCalendarRequestDto.setYear(simpleDateFormat.format(DateTime.now().toDate()));
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId(loginName);
        repayCalendarRequestDto.setBaseParam(baseParam);
        BaseResponseDto<RepayCalendarListResponseDto> repayCalendarListResponseDtoBaseResponseDto =  mobileAppRepayCalendarService.getMonthRepayCalendar(repayCalendarRequestDto);
        List<RepayCalendarResponseDto> repayCalendarResponseDtoList = repayCalendarListResponseDtoBaseResponseDto.getData().getRepayCalendarResponseDtos();
        assertThat(repayCalendarResponseDtoList.size(),is(3));
        assertEquals(repayCalendarResponseDtoList.get(0).getExpectedRepayAmount(),"2");
        assertEquals(repayCalendarResponseDtoList.get(1).getExpectedRepayAmount(),"3");
        assertEquals(repayCalendarResponseDtoList.get(2).getExpectedRepayAmount(),"1");
    }

    private InvestRepayModel createInvestRepay(long investId,Date time){
        InvestRepayModel investRepayModel = new InvestRepayModel();
        investRepayModel.setInvestId(investId);
        investRepayModel.setId(idGenerator.generate());
        investRepayModel.setStatus(RepayStatus.REPAYING);
        investRepayModel.setActualRepayDate(new Date());
        investRepayModel.setRepayDate(time);
        investRepayModel.setExpectedInterest(1);
        investRepayModel.setActualInterest(1);
        return investRepayModel;
    }

    private LoanModel createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NEWBIE);
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
        com.tuotiansudai.repository.model.LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(LoanStatus.RAISING);
        loanMapper.create(loanModel);
        return loanModel;
    }

    private UserModel createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
        return userModelTest;
    }

    private InvestModel createInvest(String loginName, long loanId) {
        InvestModel model = new InvestModel();
        model.setAmount(10);
        model.setInvestTime(new Date());
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        model.setTransferStatus(TransferStatus.TRANSFERRING);
        investMapper.create(model);
        return model;
    }

    private AccountModel createAccountByUserId(String userId) {
        AccountModel accountModel = new AccountModel(userId, userId, "120101198810012010", "", "", new Date());
        accountModel.setBalance(10000);
        accountModel.setFreeze(10000);
        accountMapper.create(accountModel);
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);
        accountMapper.update(accountModel);
        return accountModel;
    }

}
