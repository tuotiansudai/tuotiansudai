package com.tuotiansudai.service;

import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
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

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class LoanServiceTest {
    @Autowired
    private LoanService loanService;
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;



    /**
     * 正常建标
     */
    @Test
    public void createLoanServiceTest_1() {
        LoanDto loanDto = new LoanDto();
        long loanId = idGenerator.generate();
        loanDto.setId(loanId);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setMaxInvestAmount("100.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("10000.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertTrue(baseDto.getData().getStatus());
        assertNotNull(loanMapper.findById(loanDto.getId()));
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() > 0);
    }

    /**
     * 建标时，起投时间晚于结束时间
     */
    @Test
    public void createLoanServiceTest_2() {
        LoanDto loanDto = new LoanDto();
        long id = idGenerator.generate();
        loanDto.setId(id);
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setMaxInvestAmount("100000000000.00");
        loanDto.setMinInvestAmount("1.00");
        loanDto.setLoanAmount("100000000000.00");
        loanDto.setFundraisingStartTime(new Date(System.currentTimeMillis() + 1000));
        loanDto.setFundraisingEndTime(new Date(System.currentTimeMillis()));
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(id).size() == 0);
        assertNull(loanMapper.findById(id));
    }

    /**
     * 投资最大金额小于投资最小金额
     */
    @Test
    public void createLoanServiceTest_3() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("10000.00");
        loanDto.setMaxInvestAmount("99.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 投资金额小于投资最大金额
     */
    @Test
    public void createLoanServiceTest_4() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("998.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 借款人不存在
     */
    @Test
    public void createLoanServiceTest_5() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("liming");
        loanDto.setAgentLoginName("xiangjie");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = creteLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    /**
     * 代理人不存在
     */
    @Test
    public void createLoanServiceTest_6() {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName("xiangjie");
        loanDto.setAgentLoginName("liming");
        loanDto.setLoanAmount("1000.00");
        loanDto.setMaxInvestAmount("999.00");
        loanDto.setMinInvestAmount("998.00");
        loanDto.setFundraisingEndTime(new Date());
        loanDto.setFundraisingStartTime(new Date());
        BaseDto<PayDataDto> baseDto = loanService.createLoan(loanDto);
        assertFalse(baseDto.getData().getStatus());
        assertTrue(loanTitleRelationMapper.findByLoanId(loanDto.getId()).size() == 0);
        assertNull(loanMapper.findById(loanDto.getId()));
    }

    public BaseDto<PayDataDto> creteLoan(LoanDto loanDto) {
        loanDto.setProjectName("店铺资金周转");
        loanDto.setActivityRate("12");
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
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        List<LoanTitleRelationModel> loanTitleRelationModelList = new ArrayList<LoanTitleRelationModel>();
        for (int i = 0; i < 5; i++) {
            LoanTitleRelationModel loanTitleRelationModel = new LoanTitleRelationModel();
            loanTitleRelationModel.setId(idGenerator.generate());
            loanTitleRelationModel.setLoanId(loanDto.getId());
            loanTitleRelationModel.setTitleId(Long.parseLong("12312312312"));
            loanTitleRelationModel.setApplyMetarialUrl("https://github.com/tuotiansudai/tuotian/pull/279,https://github.com/tuotiansudai/tuotian/pull/279");
            loanTitleRelationModelList.add(loanTitleRelationModel);
        }
        loanDto.setLoanTitles(loanTitleRelationModelList);
        return loanService.createLoan(loanDto);
    }

    @Test
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
        investModel.setSuccessTime(new Date());
        investModel.setCreatedTime(new Date());
        investMapper.create(investModel);

        loanService.notifyInvestorsLoanOutSuccessfulByEmail(model);

    }

}
