package com.tuotiansudai.paywrapper.repository.mapper;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.LoanType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional(value = "payTransactionManager")
public class LoanMapperTest {
    @Autowired
    private LoanMapper loanMapper;

    @Test
    public void createLoanTest(){
        LoanDto loanDto = new LoanDto();
        loanDto.setActivityRate("12");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods("30");
        loanDto.setActivityType(ActivityType.DIRECTIONAL_INVEST);
        loanDto.setContractId("123");
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime("2015-11-22");
        loanDto.setFundraisingStartTime("2015-8-12");
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType(LoanType.loan_type_1);
        loanDto.setMaxInvestAmount("100000000000000");
        loanDto.setMinInvestAmount("0");
        List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
        for(int i=0;i<5;i++){
            LoanTitleModel loanTitleModel = new LoanTitleModel();
            loanTitleModel.setLoanId("123123");
            loanTitleModel.setTitleId("12341234");
            loanTitleModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleModelList.add(loanTitleModel);
        }
        loanDto.setLoanTitles(loanTitleModelList);
        try {
            LoanModel loanModel = new LoanModel(loanDto);
            loanModel.setId("1234567890");
            loanModel.setLoanLoginName("asdfas");
            loanModel.setName("asdfas");
           loanMapper.createLoan(loanModel);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
