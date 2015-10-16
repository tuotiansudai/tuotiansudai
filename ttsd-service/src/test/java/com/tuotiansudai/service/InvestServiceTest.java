package com.tuotiansudai.service;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.InvestDetailDto;
import com.tuotiansudai.dto.InvestDetailQueryDto;
import com.tuotiansudai.dto.LoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.utils.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@Transactional
public class InvestServiceTest {
    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestService investService;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IdGenerator idGenerator;

    private void createLoanByUserId(String userId, long loanId) {
        LoanDto loanDto = new LoanDto();
        loanDto.setLoanerLoginName(userId);
        loanDto.setAgentLoginName(userId);
        loanDto.setBasicRate("16.00");
        loanDto.setId(loanId);
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
        loanDto.setType(LoanType.LOAN_TYPE_1);
        loanDto.setMaxInvestAmount("100000000000");
        loanDto.setMinInvestAmount("0");
        loanDto.setCreatedTime(new Date());
        loanDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        LoanModel loanModel = new LoanModel(loanDto);
        loanMapper.create(loanModel);
    }

    private void createUserByUserId(String userId) {
        UserModel userModelTest = new UserModel();
        userModelTest.setLoginName(userId);
        userModelTest.setPassword("123abc");
        userModelTest.setEmail("12345@abc.com");
        userModelTest.setMobile("1" + RandomStringUtils.randomNumeric(10));
        userModelTest.setRegisterTime(new Date());
        userModelTest.setStatus(UserStatus.ACTIVE);
        userModelTest.setSalt(UUID.randomUUID().toString().replaceAll("-", ""));
        userMapper.create(userModelTest);
    }

    private void createInvests(String loginName, long loanId) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -98);
        for (int i = 10000000; i < 10099000; i += 1000) {
            cal.add(Calendar.SECOND, 1);
            InvestModel model = new InvestModel();
            model.setAmount(1000000);
            model.setCreatedTime(cal.getTime());
            model.setId(i);
            model.setIsAutoInvest(false);
            model.setLoginName(loginName);
            model.setLoanId(loanId);
            model.setSource(InvestSource.ANDROID);
            model.setStatus(InvestStatus.SUCCESS);
            investMapper.create(model);
        }
    }

    @Before
    public void setup() throws Exception {
        long loanId = idGenerator.generate();
        createUserByUserId("testuser");
        createLoanByUserId("testuser", loanId);
        createInvests("testuser", loanId);
    }

    @Test
    public void testPaginationInvestQuery() {
        InvestDetailQueryDto queryDto = new InvestDetailQueryDto();
        queryDto.setLoginName("testuser");
        queryDto.setPageIndex(1);
        queryDto.setPageSize(10);
        BasePaginationDataDto<InvestDetailDto> paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 99;
        InvestDetailDto dto = paginationDto.getRecords().get(0);
        assert dto.getId() == 10098000;

        queryDto.setPageIndex(3);
        queryDto.setPageSize(20);
        paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 99;
        dto = paginationDto.getRecords().get(0);
        assert dto.getId() == 10058000;
        assert dto.getId() == 10058000;

        assert dto.getLoanType() == LoanType.LOAN_TYPE_1;

        queryDto.setInvestStatus(InvestStatus.FAIL);
        paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 0;

        queryDto.setInvestStatus(InvestStatus.SUCCESS);
        queryDto.setLoanStatus(LoanStatus.CANCEL);
        paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 0;

        queryDto.setLoanStatus(LoanStatus.WAITING_VERIFY);
        paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 99;

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -15);
        queryDto.setBeginTime(cal.getTime());
        cal.add(Calendar.SECOND, 5);
        queryDto.setEndTime(cal.getTime());
        paginationDto = investService.queryInvests(queryDto, false);
        assert paginationDto.getCount() == 5;
    }
}
