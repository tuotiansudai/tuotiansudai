package com.tuotiansudai.paywrapper.service;

import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestRepayModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanType;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class RepayServiceTest {

    @Autowired
    private RepayService repayService;



    @Test
    public void shouldName1() throws Exception {
        LoanModel loanModel = new LoanModel();
        loanModel.setType(LoanType.LOAN_TYPE_1);
        loanModel.setRecheckTime(new Date());
        loanModel.setPeriods(3);
        loanModel.setBaseRate(0.1);
        loanModel.setActivityRate(0.01);
        loanModel.setInvestFeeRate(0.1);

        InvestModel investModel = new InvestModel();
        investModel.setAmount(100);

        List<InvestRepayModel> investRepayModels = repayService.generateInvestRepay(loanModel, investModel);

        assertTrue(true);

    }

    @Test
    public void shouldName() throws Exception {
        System.out.println(Days.daysBetween(new DateTime().withDate(2015, 1, 1).withTimeAtStartOfDay(), new DateTime().withDate(2015, 1, 31).withTimeAtStartOfDay()).getDays());

    }
}
