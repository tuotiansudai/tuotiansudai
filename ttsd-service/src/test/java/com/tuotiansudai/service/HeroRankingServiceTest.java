package com.tuotiansudai.service;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.MysteriousPrizeDto;
import com.tuotiansudai.membership.repository.mapper.MembershipMapper;
import com.tuotiansudai.membership.repository.mapper.UserMembershipMapper;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipModel;
import com.tuotiansudai.membership.repository.model.UserMembershipType;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@WebAppConfiguration
@Transactional
public class HeroRankingServiceTest {

    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private HeroRankingService heroRankingService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private RedisWrapperClient redisWrapperClient;

    private final static String MYSTERIOUSREDISKEY = "console:mysteriousPrize";

    @Autowired
    private ReferrerRelationMapper referrerRelationMapper;

    @Autowired
    private MembershipMapper membershipMapper;

    @Autowired
    private UserMembershipMapper userMembershipMapper;
    

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


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

    private InvestModel getFakeInvestModelByLoginName(String loginName,long loanId){
        InvestModel model = new InvestModel(idGenerator.generate(), loanId, null, 1000l, loginName, new DateTime().withTimeAtStartOfDay().toDate(), Source.WEB, null,0.1);
        model.setStatus(InvestStatus.SUCCESS);
        return model;
    }

    private LoanModel createLoan(String userId, long loanId, ActivityType activityType, LoanStatus loanStatus) {
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
        loanDto.setActivityType(activityType);
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
        loanDto.setProductType(ProductType._30);
        loanDto.setPledgeType(PledgeType.HOUSE);
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setStatus(loanStatus);
        return loanModel;
    }

    @Test
    public void shouldSaveMysteriousPrizeIsSuccess(){
        MysteriousPrizeDto mysteriousPrizeDto = new MysteriousPrizeDto();
        String now = new DateTime().withTimeAtStartOfDay().toString("yyyy-MM-dd");
        mysteriousPrizeDto.setImageUrl("imageUrl");
        mysteriousPrizeDto.setPrizeName("name");
        mysteriousPrizeDto.setPrizeDate(new DateTime().withTimeAtStartOfDay().toDate());
        heroRankingService.saveMysteriousPrize(mysteriousPrizeDto);
        MysteriousPrizeDto mysteriousPrizeDtoReturn = (MysteriousPrizeDto)redisWrapperClient.hgetSeri(MYSTERIOUSREDISKEY,now);
        assertEquals(mysteriousPrizeDto.getImageUrl(),mysteriousPrizeDtoReturn.getImageUrl());
        assertEquals(mysteriousPrizeDto.getPrizeName(),mysteriousPrizeDtoReturn.getPrizeName());
        redisWrapperClient.hdelSeri(MYSTERIOUSREDISKEY, now);


    }
}
