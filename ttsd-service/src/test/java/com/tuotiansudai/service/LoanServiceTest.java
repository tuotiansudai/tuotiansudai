package com.tuotiansudai.service;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;
    @Autowired
    private IdGenerator idGenerator;
    @Test
    public void createLoanServiceTest(){
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        long id = idGenerator.generate();
        String idStr = String.valueOf(id);
        loanDto.setId(idStr);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods("30");
        loanDto.setActivityType(ActivityType.DIRECTIONAL_INVEST);
        loanDto.setContractId("123");
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
        for(int i=0;i<5;i++){
            LoanTitleModel loanTitleModel = new LoanTitleModel();
            loanTitleModel.setId(idGenerator.generate());
            loanTitleModel.setLoanId(id);
            loanTitleModel.setTitleId(Long.parseLong("1234567890"));
            loanTitleModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleModelList.add(loanTitleModel);
        }
        loanDto.setLoanTitles(loanTitleModelList);
        loanService.createLoanBid(loanDto);

    }
}
