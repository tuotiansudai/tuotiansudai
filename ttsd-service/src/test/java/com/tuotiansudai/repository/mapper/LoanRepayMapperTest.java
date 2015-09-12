package com.tuotiansudai.repository.mapper;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.collections4.CollectionUtils;
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
public class LoanRepayMapperTest {

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
        UserModel userModel = this.getFakeUserModel();
        LoanModel fakeLoanModel = this.getFakeLoanModel();
        userMapper.create(userModel);
        loanMapper.create(fakeLoanModel);
        List<LoanRepayModel> loanRepayModels = Lists.newArrayList();
        LoanRepayModel loanRepayModel = new LoanRepayModel();
        loanRepayModel.setId(idGenerator.generate());
        loanRepayModel.setDefaultInterest(0);
        loanRepayModel.setActualInterest(0);
        loanRepayModel.setPeriod(1);
        loanRepayModel.setStatus(RepayStatus.REPAYING);
        loanRepayModel.setLoanId(fakeLoanModel.getId());
        loanRepayModel.setRepayDate(new Date());
        loanRepayModel.setCorpus(0);
        loanRepayModel.setExpectedInterest(0);
        loanRepayModels.add(loanRepayModel);
        loanRepayMapper.create(loanRepayModels);

        List<LoanRepayModel> actualLoanRepayModels = loanRepayMapper.findByLoanId(fakeLoanModel.getId());

        assertTrue(CollectionUtils.isNotEmpty(actualLoanRepayModels));
    }

    private UserModel getFakeUserModel() {
        UserModel fakeUserModel = new UserModel();
        fakeUserModel.setLoginName("loginName");
        fakeUserModel.setPassword("password");
        fakeUserModel.setEmail("12345@abc.com");
        fakeUserModel.setMobile("13900000000");
        fakeUserModel.setRegisterTime(new Date());
        fakeUserModel.setStatus(UserStatus.ACTIVE);
        fakeUserModel.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        return fakeUserModel;
    }

    private LoanModel getFakeLoanModel() {
        UserModel fakeUserModel = getFakeUserModel();
        LoanModel fakeLoanModel = new LoanModel();
        fakeLoanModel.setId(idGenerator.generate());
        fakeLoanModel.setName("name");
        fakeLoanModel.setAgentLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setLoanerLoginName(fakeUserModel.getLoginName());
        fakeLoanModel.setType(LoanType.LOAN_TYPE_1);
        fakeLoanModel.setPeriods(1);
        fakeLoanModel.setDescriptionText("text");
        fakeLoanModel.setDescriptionHtml("html");
        fakeLoanModel.setActivityType(ActivityType.NORMAL);
        fakeLoanModel.setFundraisingStartTime(new Date());
        fakeLoanModel.setFundraisingEndTime(new Date());
        fakeLoanModel.setStatus(LoanStatus.REPAYING);
        return fakeLoanModel;
    }
}
