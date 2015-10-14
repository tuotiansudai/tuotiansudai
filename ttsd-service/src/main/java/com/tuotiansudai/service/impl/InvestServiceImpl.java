package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.AutoInvestPlanMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.AutoInvestMonthPeriod;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.InterestCalculator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    static Logger logger = Logger.getLogger(InvestServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private AutoInvestPlanMapper autoInvestPlanMapper;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AccountService accountService;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) {
        String loginName = LoginUserInfo.getLoginName();
        investDto.setLoginName(loginName);
        return payWrapperClient.invest(investDto);
    }

    @Override
    public BaseDto<PayDataDto> investNopwd(InvestDto investDto) {
        investDto.setInvestSource(InvestSource.AUTO);
        return payWrapperClient.investNopwd(investDto);
    }

    @Override
    public long calculateExpectedInterest(long loanId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        int repayTimes = loanModel.calculateLoanRepayTimes();
        LoanType loanType = loanModel.getType();

        int daysOfMonth = 30;
        int duration = loanModel.getPeriods();
        if (loanType.getLoanPeriodUnit() == LoanPeriodUnit.MONTH) {
            duration = repayTimes * daysOfMonth;
        }
        return InterestCalculator.calculateInterest(loanModel, amount * duration);
    }

    @Override
    public BasePaginationDataDto<InvestDetailDto> queryInvests(InvestDetailQueryDto queryDto, boolean includeNextRepay) {
        int offset = (queryDto.getPageIndex() - 1) * queryDto.getPageSize();
        int limit = queryDto.getPageSize();
        List<InvestDetailModel> investModelList = investMapper.findByPage(
                queryDto.getLoanId(),
                queryDto.getLoginName(),
                queryDto.getBeginTime(),
                queryDto.getEndTime(),
                queryDto.getLoanStatus(),
                queryDto.getInvestStatus(),
                includeNextRepay,
                offset,
                limit
        );
        int count = investMapper.findCount(
                queryDto.getLoanId(),
                queryDto.getLoginName(),
                queryDto.getBeginTime(),
                queryDto.getEndTime(),
                queryDto.getLoanStatus(),
                queryDto.getInvestStatus()
        );

        List<InvestDetailDto> dtoList = Lists.newArrayList();
        for (InvestDetailModel model : investModelList) {
            dtoList.add(new InvestDetailDto(model));
        }
        BasePaginationDataDto<InvestDetailDto> paginationDto = new BasePaginationDataDto<>(
                queryDto.getPageIndex(), queryDto.getPageSize(), count, dtoList
        );
        paginationDto.setStatus(true);
        return paginationDto;
    }

    @Override
    public void turnOnAutoInvest(AutoInvestPlanModel model) {
        if(StringUtils.isBlank(model.getLoginName())){
            throw new NullPointerException("Not Login");
        }

        AutoInvestPlanModel planModel = autoInvestPlanMapper.findByLoginName(model.getLoginName());
        model.setCreatedTime(new Date());
        model.setEnabled(true);

        if (planModel != null) {
            model.setId(planModel.getId());
            autoInvestPlanMapper.update(model);
        } else {
            model.setId(idGenerator.generate());
            autoInvestPlanMapper.create(model);
        }
    }

    @Override
    public void turnOffAutoInvest(String loginName) {
        autoInvestPlanMapper.disable(loginName);
    }

    @Override
    public AutoInvestPlanModel findUserAutoInvestPlan(String loginName) {
        return autoInvestPlanMapper.findByLoginName(loginName);
    }

    @Override
    public List<AutoInvestPlanModel> findValidPlanByPeriod(AutoInvestMonthPeriod period) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return autoInvestPlanMapper.findEnabledPlanByPeriod(period.getPeriodValue(), cal.getTime());
    }

    @Override
    public void validateAutoInvest(long loanId) {
        LoanModel loanModel= loanMapper.findById(loanId);
        List<AutoInvestPlanModel> autoInvestPlanModels = this.findValidPlanByPeriod(AutoInvestMonthPeriod.generateFromLoanPeriod(loanModel.getPeriods()));
        for (AutoInvestPlanModel autoInvestPlanModel: autoInvestPlanModels) {
            try {
                long availableLoanAmount = loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanId);
                if (availableLoanAmount <= 0) {
                    return;
                }
                InvestDto investDto = new InvestDto();
                investDto.setLoanId(String.valueOf(loanId));
                investDto.setLoginName(autoInvestPlanModel.getLoginName());
                long autoInvestAmount = this.calculateAutoInvestAmount(autoInvestPlanModel, availableLoanAmount);
                if (autoInvestAmount == 0) {
                    continue;
                }
                investDto.setAmount(String.valueOf(autoInvestAmount));
                BaseDto<PayDataDto> baseDto = this.investNopwd(investDto);
                if (!baseDto.isSuccess()) {
                    logger.debug("auto invest failed auto invest plan id is " + autoInvestPlanModel.getId());
                }
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
                continue;
            }
        }
    }

    private long calculateAutoInvestAmount(AutoInvestPlanModel autoInvestPlanModel, long availableLoanAmount) {
        long availableAmount = accountService.getBalance(autoInvestPlanModel.getLoginName()) - autoInvestPlanModel.getRetentionAmount();
        long maxInvestAmount = autoInvestPlanModel.getMaxInvestAmount();
        long minInvestAmount = autoInvestPlanModel.getMinInvestAmount();
        long returnAmount = 0;
        if (availableLoanAmount < minInvestAmount) {
            return returnAmount;
        }
        if (availableAmount >= maxInvestAmount) {
            returnAmount = maxInvestAmount;
        } else if (availableAmount < maxInvestAmount && availableAmount > minInvestAmount) {
            returnAmount = availableAmount;
        }
        if (returnAmount >= availableLoanAmount) {
            returnAmount = availableLoanAmount;
        }
        return returnAmount;
    }

}
