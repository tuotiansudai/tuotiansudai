package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.IdGenerator;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RepayMapperTest {

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void shouldCreateLoanRepayModel() throws Exception {
        UserModel userModel = this.getUserModelTest();
        userMapper.create(userModel);
        LoanDto loanDto = this.getLoanModel();
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(idGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(loanModel.getId());
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);
        loanRepayMapper.create(loanRepayModels);
    }

    @Test
    public void shouldLoanRepayPaginationIsOk(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        UserModel userModel = this.getUserModelTest();
        userMapper.create(userModel);
        LoanDto loanDto = this.getLoanModel();
        LoanModel loanModel = new LoanModel(loanDto);
        loanModel.setCreatedTime(new DateTime().minusDays(2).withTimeAtStartOfDay().toDate());
        loanMapper.create(loanModel);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(idGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(loanModel.getId());
        loanRepayModel.setRepayDate(new DateTime().withTimeAtStartOfDay().toDate());
        loanRepayModel.setCreatedTime(new DateTime().minusDays(1).withTimeAtStartOfDay().toDate());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);
        LoanRepayModel loanRepayModel1 = new LoanRepayModel();
        loanRepayModel1.setId(idGenerator.generate());
        loanRepayModel1.setDefaultInterest(0);
        loanRepayModel1.setActualInterest(0);
        loanRepayModel1.setPeriod(1);
        loanRepayModel1.setStatus(RepayStatus.REPAYING);
        loanRepayModel1.setLoanId(loanModel.getId());
        loanRepayModel1.setRepayDate(new DateTime().plusDays(10).withTimeAtStartOfDay().toDate());
        loanRepayModel1.setCorpus(0);
        loanRepayModel1.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel1);
        loanRepayMapper.create(loanRepayModels);

        List<LoanRepayModel> models = loanRepayMapper.findLoanRepayPagination(0, 1, loanModel.getId(), "", RepayStatus.REPAYING, new DateTime().withTimeAtStartOfDay().toDate(), new DateTime().withTimeAtStartOfDay().toDate());
        assertNotNull(models);

        assertThat(models.get(0).getId(), is(loanRepayModel.getId()));
        assertThat(models.get(0).getLoan().getId(), is(loanModel.getId()));
        assertThat(models.get(0).getStatus(), is(loanRepayModel.getStatus()));
        assertThat(models.get(0).getCreatedTime().getTime(), is(loanRepayModel.getCreatedTime().getTime()));
        assertThat(models.get(0).getLoan().getStatus(), is(loanModel.getStatus()));
        assertThat(models.get(0).getLoan().getCreatedTime().getTime(), is(loanModel.getCreatedTime().getTime()));
        assertNotNull(models.get(0).getLoan().getName());
    }

    private LoanDto getLoanModel(){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("helloworld");
        loanDto.setLoanerUserName("借款人");
        loanDto.setLoanerIdentityNumber("111111111111111111");
        loanDto.setAgentLoginName("helloworld");
        loanDto.setBasicRate("16.00");
        long id = idGenerator.generate();
        loanDto.setId(id);
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
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.INVEST_INTEREST_MONTHLY_REPAY);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        loanDto.setProductType(ProductType._30);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return loanDto;
    }

    private UserModel getUserModelTest() {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName("helloworld");
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("13900000000");
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return userModelTest;
    }

}
