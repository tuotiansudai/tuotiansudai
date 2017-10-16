package com.tuotiansudai.repository.mapper;


import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class ExtraLoanRateMapperTest {

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private FakeUserHelper userMapper;

    @Test
    public void shouldFindMaxRateByLoanIdIsOk() {
        String loginName = "testExtraRate";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        List<ExtraLoanRateModel> extraLoanRateModels = createExtraLoanRate(loanId);
        extraLoanRateMapper.create(extraLoanRateModels);
        double maxRate = extraLoanRateMapper.findMaxRateByLoanId(loanId);
        assertTrue(maxRate == extraLoanRateModels.get(2).getRate());
    }

    @Test
    public void shouldFindByLoanIdOrderByRate() {
        String loginName = "testExtraRate";
        long loanId = IdGenerator.generate();
        createUserByUserId(loginName);
        createLoanByUserId(loginName, loanId);
        extraLoanRateMapper.create(createExtraLoanRate(loanId));
        List<ExtraLoanRateModel> extraLoanRateModelsOrderByRate = extraLoanRateMapper.findByLoanIdOrderByRate(loanId);
        assertTrue(extraLoanRateModelsOrderByRate.size() == 3);
        assertTrue(extraLoanRateModelsOrderByRate.get(0).getRate() < extraLoanRateModelsOrderByRate.get(1).getRate());
        assertTrue(extraLoanRateModelsOrderByRate.get(1).getRate() < extraLoanRateModelsOrderByRate.get(2).getRate());
    }


    private List<ExtraLoanRateModel> createExtraLoanRate(long loanId) {
        ExtraLoanRateModel model = new ExtraLoanRateModel();
        model.setLoanId(loanId);
        model.setExtraRateRuleId(100001);
        model.setCreatedTime(new Date());
        model.setMinInvestAmount(100000);
        model.setMaxInvestAmount(1000000);
        model.setRate(0.1);
        ExtraLoanRateModel model2 = new ExtraLoanRateModel();
        model2.setLoanId(loanId);
        model2.setExtraRateRuleId(100001);
        model2.setCreatedTime(new Date());
        model2.setMinInvestAmount(1000000);
        model2.setMaxInvestAmount(5000000);
        model2.setRate(0.3);
        ExtraLoanRateModel model3 = new ExtraLoanRateModel();
        model3.setLoanId(loanId);
        model3.setExtraRateRuleId(100001);
        model3.setCreatedTime(new Date());
        model3.setMinInvestAmount(5000000);
        model3.setMaxInvestAmount(0);
        model3.setRate(0.5);
        List<ExtraLoanRateModel> list = Lists.newArrayList();
        list.add(model);
        list.add(model2);
        list.add(model3);
        return list;
    }

    private void createLoanByUserId(String userId, long loanId) {
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
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setPledgeType(PledgeType.HOUSE);
        loanMapper.create(loanModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }
}
