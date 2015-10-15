package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestDetailModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.LoanPeriodUnit;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.utils.InterestCalculator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestServiceImpl implements InvestService {

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public BaseDto<PayFormDataDto> invest(InvestDto investDto) throws InvestException {
        String loginName = LoginUserInfo.getLoginName();
        investDto.setLoginName(loginName);

        String investCheckResult = checkInvestAmount(investDto);
        if (investCheckResult == null) {
            return payWrapperClient.invest(investDto);
        } else {
            throw new InvestException(investCheckResult);
        }
    }

    private String checkInvestAmount(InvestDto investDto) {
        long loanId = investDto.getLoanIdLong();
        LoanModel loan = loanMapper.findById(loanId);
        long userInvestMinAmount = loan.getMinInvestAmount();
        long investAmount = Long.parseLong(investDto.getAmount());
        long userInvestIncreasingAmount = loan.getInvestIncreasingAmount();

        // 不满足最小投资限制
        if (investAmount < userInvestMinAmount) {
            return "投资金额小于标的最小投资金额";
        }

        // 不满足递增规则
        if ((investAmount - userInvestMinAmount) % userInvestIncreasingAmount > 0) {
            return "投资金额不符合递增金额要求";
        }

        long userInvestMaxAmount = loan.getMaxInvestAmount();
        long successInvestAmount = investMapper.sumSuccessInvestAmount(loanId);
        long loanNeedAmount = loan.getLoanAmount() - successInvestAmount;

        // 标已满
        if (loanNeedAmount <= 0) {
            return "标的已满";
        }

        // 超投
        if (investAmount < loanNeedAmount) {
            return "标的可投金额不足";
        }

        long userInvestAmount = investMapper.sumSuccessInvestAmountByLoginName(loanId, investDto.getLoginName());

        // 不满足单用户投资限额
        if (investAmount > userInvestMaxAmount - userInvestAmount) {
            return "投资金额超过了用户投资限额";
        }

        return null;
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
}
