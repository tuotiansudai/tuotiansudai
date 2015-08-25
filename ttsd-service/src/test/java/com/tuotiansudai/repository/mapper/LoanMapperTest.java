package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanMapperTest {
    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    LoanTitleMapper loanTitleMapper;
    @Autowired
    private IdGenerator idGenerator;

    @Test
    public void createLoanTest() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setBasicRate("16.00");
        long id = idGenerator.generate();
        String idStr = String.valueOf(id);
        loanDto.setId(idStr);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
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
        loanDto.setLoanTitles(loanTitleModelList);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.createLoan(loanModel);
        assertNotNull(loanMapper.findLoanByLoanId(id));
    }
}
