package com.tuotiansudai.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.HomeLoanDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.HomeService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public List<HomeLoanDto> getLoans() {
        List<LoanModel> loanModels = loanMapper.findHomeLoan();

        return Lists.transform(loanModels, new Function<LoanModel, HomeLoanDto>() {
            @Override
            public HomeLoanDto apply(LoanModel loan) {
                List<InvestModel> investModels = investMapper.findSuccessInvestsByLoanId(loan.getId());
                long investAmount = 0;
                for (InvestModel investModel : investModels) {
                    investAmount += investModel.getAmount();
                }
                return new HomeLoanDto(loan.getId(),
                        loan.getName(),
                        loan.getProductType(),
                        loan.getActivityType(),
                        loan.getType().getLoanPeriodUnit(),
                        loan.getBaseRate(),
                        loan.getActivityRate(),
                        loan.getPeriods(),
                        loan.getLoanAmount(),
                        investAmount,
                        loan.getStatus(),
                        loan.getFundraisingStartTime());
            }
        });
    }
}
