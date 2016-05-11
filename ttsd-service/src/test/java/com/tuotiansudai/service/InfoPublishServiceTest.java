package com.tuotiansudai.service;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InfoPublishServiceTest {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InfoPublishService infoPublishService;

    @Autowired
    private AccountMapper accountMapper;

    private LoanModel createLoanByUserId(String userId, long loanId, ProductType productType) {
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
        loanDto.setPeriods(31);
        loanDto.setActivityType(ActivityType.NEWBIE);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setProductType(productType);
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        LoanModel loanModel = new LoanModel(loanDto);
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

    private InvestModel createInvest(String loginName, long loanId,long amount) {
        InvestModel model = new InvestModel();
        model.setAmount(amount);
        model.setCreatedTime(new Date());
        model.setId(idGenerator.generate());
        model.setIsAutoInvest(false);
        model.setLoginName(loginName);
        model.setLoanId(loanId);
        model.setSource(Source.ANDROID);
        model.setStatus(InvestStatus.SUCCESS);
        investMapper.create(model);
        return model;
    }

    @Test
    public void shouldGetInvestDetail() {
        UserModel userModelInvest = createUserByUserId("testUserInvest");
        UserModel userModelLoaner = createUserByUserId("testUserLoaner");

        createLoanByUserId("testUserLoaner",10001,ProductType.SYL);
        createLoanByUserId("testUserLoaner",10002,ProductType.WYX);
        createLoanByUserId("testUserLoaner",10003,ProductType.JYF);


        createInvest("testUserInvest",10001,1000);
        createInvest("testUserInvest",10001,2000);
        createInvest("testUserInvest",10002,3000);
        createInvest("testUserInvest",10002,4000);
        createInvest("testUserInvest",10003,5000);
        createInvest("testUserInvest",10003,6000);


        List<InvestDataView> investDataViewList = new ArrayList<InvestDataView>();
        
        assertTrue(infoPublishService.getInvestDetail().size() > 0);
    }

}
