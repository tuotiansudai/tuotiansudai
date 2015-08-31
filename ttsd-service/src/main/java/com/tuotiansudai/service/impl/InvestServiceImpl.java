package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.LoanDetailDto;
import com.tuotiansudai.exception.InsufficientBalanceException;
import com.tuotiansudai.exception.LoanNotFoundException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.utils.IdGenerator;
import com.tuotiansudai.utils.LoginUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class InvestServiceImpl implements InvestService {
    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private LoanService loanService;

    @Autowired
    private InvestMapper mapper;

    @Override
    public long doInvest(long loanId, long amount, InvestSource source) {
        // 查找对应标的
        LoanDetailDto loanDetailDto = loanService.getLoanDetail(loanId);
        if (loanDetailDto == null) {
            throw new LoanNotFoundException("查找不到对应的标的信息:" + loanId);
        }

        // 检查标的可投余额
        long investedAmount = getSuccessInvestedAmountByLoanId(loanId);
        long remainInvestableAmount = loanDetailDto.getLoanAmount() - investedAmount;

        if(remainInvestableAmount < amount){
            throw new InsufficientBalanceException(String.format("投资金额超过了该标的的可投金额",amount,remainInvestableAmount));
        }

        long investId = createInvestRecord(loanId, amount, source);

        return investId;
    }

    private long createInvestRecord(long loanId, long amount, InvestSource source) {
        long investId = idGenerator.generate();
        String loginName = LoginUserInfo.getLoginName();
        InvestModel invest = new InvestModel();
        invest.setId(investId);
        invest.setLoanId(loanId);
        invest.setLoginName(loginName);
        invest.setAmount(amount);
        invest.setStatus(InvestStatus.WAITING);
        invest.setIsAutoInvest(false);
        invest.setSource(source);
        invest.setCreatedTime(new Date());
        mapper.create(invest);
        return investId;
    }

    @Override
    public long getSuccessInvestedAmountByLoanId(long loanId) {
        return 0;
    }

    @Override
    public List<InvestModel> getByLoanId(long loanId, int rowLimit, int rowIndex) {
        return null;
    }
}
