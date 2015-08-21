package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanTitleModel;
import com.tuotiansudai.repository.model.TitleModel;
import com.tuotiansudai.utils.IdGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml"})
@Transactional
public class LoanMapperTest {
    @Autowired
    private LoanMapper loanMapper;

    @Test
    public void createLoanTest(){
        LoanDto loanDto = new LoanDto();
        IdGenerator idGenerator = new IdGenerator();
        loanDto.setLoanLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        String id = String.valueOf(idGenerator.generate());
        loanDto.setId(id);
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
        loanDto.setShowOnHome("1");
        loanDto.setPeriods("30");
        loanDto.setActivityType("DIRECTIONAL_INVEST");
        loanDto.setContractId("123");
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setFundraisingEndTime("2015-11-22");
        loanDto.setFundraisingStartTime("2015-8-12");
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setLoanAmount("10000");
        loanDto.setType("loan_type_1");
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        List<LoanTitleModel> loanTitleModelList = new ArrayList<LoanTitleModel>();
        for(int i=0;i<5;i++){
            LoanTitleModel loanTitleModel = new LoanTitleModel();
            loanTitleModel.setId(new BigInteger(String.valueOf(idGenerator.generate())));
            loanTitleModel.setLoanId(new BigInteger(id));
            loanTitleModel.setTitleId(new BigInteger("1234567890"));
            loanTitleModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleModelList.add(loanTitleModel);
        }
        loanDto.setLoanTitles(loanTitleModelList);
        try {
            LoanModel loanModel = new LoanModel(loanDto);
            loanMapper.createLoan(loanModel);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
