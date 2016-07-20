package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.repository.mapper.FinanceReportMapper;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.FinanceReportService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.ExportCsvUtil;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FinanceReportServiceImpl implements FinanceReportService {

    static Logger logger = Logger.getLogger(FinanceReportServiceImpl.class);

    @Autowired
    FinanceReportMapper financeReportMapper;

    @Autowired
    InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    InvestRepayMapper investRepayMapper;

    private List<FinanceReportDto> combineFinanceReportDtos(List<FinanceReportItemView> financeReportItemViews) {
        List<FinanceReportDto> financeReportDtos = new ArrayList<>();

        for (FinanceReportItemView financeReportItemView : financeReportItemViews) {
            FinanceReportDto financeReportDto = new FinanceReportDto(financeReportItemView);

            //Set Benefit Days
            if (1 == financeReportDto.getPeriod()) {
                if (financeReportItemView.getLoanType().equals(LoanType.INVEST_INTEREST_MONTHLY_REPAY) ||
                        financeReportItemView.getLoanType().equals(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY)) {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(financeReportDto.getInvestTime()),
                            new LocalDate(financeReportDto.getRepayTime())).getDays() + 1);
                } else if (financeReportItemView.getLoanType().equals(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY) ||
                        financeReportItemView.getLoanType().equals(LoanType.LOAN_INTEREST_MONTHLY_REPAY)) {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(financeReportDto.getRecheckTime()),
                            new LocalDate(financeReportDto.getRepayTime())).getDays() + 1);
                }
            } else {
                InvestRepayModel beforeInvestRepayModel = investRepayMapper.findByInvestIdAndPeriod(financeReportItemView.getInvestId(),
                        financeReportItemView.getPeriod() - 1);
                if (null == beforeInvestRepayModel) {
                    financeReportDto.setBenefitDays(-1);
                } else {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(beforeInvestRepayModel.getRepayDate()),
                            new LocalDate(financeReportItemView.getRepayTime())).getDays());
                }
            }

            //Set Recommend Reward: Default value
            financeReportDto.setRecommendAmount("");

            InvestReferrerRewardModel investReferrerRewardModel = investReferrerRewardMapper.findByInvestIdAndReferrer(financeReportItemView.getInvestId(), financeReportItemView.getReferrer());
            if (investReferrerRewardModel != null) {
                if (investReferrerRewardModel.getReferrerRole().equals(Role.STAFF)) {
                    financeReportDto.setReferrer(financeReportDto.getReferrer() + "(业务员)");
                }
                if (1 == financeReportDto.getPeriod()) {
                    financeReportDto.setRecommendAmount(AmountConverter.convertCentToString(investReferrerRewardModel.getAmount()));
                }
            }

            financeReportDtos.add(financeReportDto);
        }
        return financeReportDtos;
    }

    @Override
    public BasePaginationDataDto<FinanceReportDto> getFinanceReportDtos(Long loanId, Integer period, String investLoginName,
                                                                        Date investStartTime, Date investEndTime, int index, int pageSize) {
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime, (index - 1) * pageSize, pageSize);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportItemViews);
        int count = financeReportMapper.findCountFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime);
        return new BasePaginationDataDto<>(index, pageSize, count, financeReportDtos);
    }

    @Override
    public List<List<String>> getFinanceReportCsvData(Long loanId, Integer period, String investLoginName,
                                                      Date investStartTime, Date investEndTime) {
        //直接导出所有内容，所以index = 1, pageSize = 9999999
        final int index = 1;
        final int pageSize = 9999999;
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime, (index - 1) * pageSize, pageSize);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportItemViews);
        List<List<String>> csvData = new ArrayList<>();
        for (FinanceReportDto financeReportDto : financeReportDtos) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(financeReportDto);
            csvData.add(dtoStrings);
        }
        return csvData;
    }
}
