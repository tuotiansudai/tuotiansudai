package com.tuotiansudai.service;


import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;

    @Autowired
    private IdGenerator idGenerator;


    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;




    public void shouldNotifyInvestorsLoanOutSuccessfulByEmail() {
        LoanModel model = new LoanModel();
        long id = idGenerator.generate();
        model.setId(id);
        model.setName("hourglass");
        model.setLoanerLoginName("loaner");
        model.setAgentLoginName("agent");
        model.setType(LoanType.LOAN_TYPE_1);
        model.setPeriods(3l);
        model.setLoanAmount(1000l);
        model.setInvestFeeRate(1.0);
        model.setActivityType(ActivityType.EXCLUSIVE);
        model.setBaseRate(1.0);
        model.setContractId(123);
        model.setDescriptionText("text");
        model.setDescriptionHtml("html");
        model.setFundraisingStartTime(new Date());
        model.setFundraisingEndTime(new Date());
        model.setCreatedTime(new Date());
        model.setStatus(LoanStatus.RAISING);
        loanMapper.create(model);

        InvestModel investModel = new InvestModel();
        investModel.setId(idGenerator.generate());
        investModel.setLoginName("zhuyanyan");
        investModel.setLoanId(id);
        investModel.setAmount(100);
        investModel.setStatus(InvestStatus.SUCCESS);
        investModel.setSource(InvestSource.ANDROID);
        investModel.setIsAutoInvest(true);
        investModel.setCreatedTime(new Date());
        investMapper.create(investModel);

        loanService.notifyInvestorsLoanOutSuccessfulByEmail(model);

    }

}
