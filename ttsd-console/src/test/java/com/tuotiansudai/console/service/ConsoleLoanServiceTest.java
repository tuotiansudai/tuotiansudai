package com.tuotiansudai.console.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.ActivityType;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.LoanTitleRelationModel;
import com.tuotiansudai.repository.model.LoanType;
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
public class ConsoleLoanServiceTest {
    @Autowired
    private IdGenerator idGenerator;
    @Autowired
    private ConsoleLoanService consoleLoanService;

    @Test
    public void editLoanTest() {
        LoanDto loanDto = assembleLoanParam();
        BaseDto<PayDataDto> baseDto = consoleLoanService.editLoan(loanDto);
        assertTrue(baseDto.getData().getStatus());
    }

    @Test
    public void firstTrialPassedTest() {
        LoanDto loanDto = assembleLoanParam();
        BaseDto<PayDataDto> baseDto = consoleLoanService.firstTrialPassed(loanDto);
        assertTrue(baseDto.getData().getStatus());
    }

    @Test
    public void firstTrialRefusedTest() {
        LoanDto loanDto = assembleLoanParam();
        BaseDto<PayDataDto> baseDto = consoleLoanService.firstTrialRefused(loanDto);
        assertTrue(baseDto.getData().getStatus());
    }

    private LoanDto assembleLoanParam() {
        long loanId = 194989993639936l;
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("5000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        loanDto.setProjectName("店铺资金周转更新");
        loanDto.setActivityRate("12.00");
        loanDto.setBasicRate("16.00");
        loanDto.setShowOnHome(true);
        loanDto.setPeriods(30);
        loanDto.setActivityType(ActivityType.NORMAL);
        loanDto.setContractId(123);
        loanDto.setDescriptionHtml("asdfasdf");
        loanDto.setDescriptionText("asdfasd");
        loanDto.setInvestFeeRate("15");
        loanDto.setInvestIncreasingAmount("1");
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setCreatedTime(new Date());
        loanDto.setStatus(LoanStatus.VERIFY_FAIL);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            loanTitleRelationModel.setTitleId(Long.parseLong("12312312312"));
            loanTitleRelationModel.setApplyMetarialUrl("www.baidu.com,www.google.com");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return loanDto;
    }
}
