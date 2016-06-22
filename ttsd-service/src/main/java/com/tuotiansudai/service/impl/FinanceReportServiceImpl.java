package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.repository.mapper.FinanceReportMapper;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.model.FinanceReportView;
import com.tuotiansudai.repository.model.InvestReferrerRewardModel;
import com.tuotiansudai.repository.model.LoanType;
import com.tuotiansudai.repository.model.Role;
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

    private FinanceReportView getBeforeFinanceReportModel(List<FinanceReportView> financeReportViews, int index) {
        FinanceReportView currentFinanceReportView = financeReportViews.get(index);
        if (1 == currentFinanceReportView.getPeriod()) {
            return currentFinanceReportView;
        }
        for (int i = index - 1; i >= 0; --i) {
            FinanceReportView financeReportView = financeReportViews.get(i);
            if ((financeReportView.getInvestId() == currentFinanceReportView.getInvestId()) &&
                    financeReportView.getPeriod() == currentFinanceReportView.getPeriod() - 1) {
                return financeReportView;
            }
        }
        for (int i = index + 1; i < financeReportViews.size(); ++i) {
            FinanceReportView financeReportView = financeReportViews.get(i);
            if ((financeReportView.getInvestId() == currentFinanceReportView.getInvestId()) &&
                    financeReportView.getPeriod() == currentFinanceReportView.getPeriod() - 1) {
                return financeReportView;
            }
        }
        return null;
    }

    private List<FinanceReportDto> combineFinanceReportDtos(List<FinanceReportView> financeReportViews) {
        List<FinanceReportDto> financeReportDtos = new ArrayList<>();

        for (FinanceReportView financeReportView : financeReportViews) {
            FinanceReportDto financeReportDto = new FinanceReportDto(financeReportView);

            //Set Benefit Days
            if (1 == financeReportDto.getPeriod()) {
                if (financeReportDto.getLoanType().equals(LoanType.INVEST_INTEREST_MONTHLY_REPAY) ||
                        financeReportDto.getLoanType().equals(LoanType.INVEST_INTEREST_LUMP_SUM_REPAY)) {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(financeReportDto.getInvestTime()),
                            new LocalDate(financeReportDto.getRepayTime())).getDays() + 1);
                } else if (financeReportDto.getLoanType().equals(LoanType.LOAN_INTEREST_LUMP_SUM_REPAY) ||
                        financeReportDto.getLoanType().equals(LoanType.LOAN_INTEREST_MONTHLY_REPAY)) {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(financeReportDto.getRecheckTime()),
                            new LocalDate(financeReportDto.getRepayTime())).getDays() + 1);
                }
            } else {
                FinanceReportView beforeFinanceReportView = getBeforeFinanceReportModel(financeReportViews, financeReportViews.indexOf(financeReportView));
                if (null == beforeFinanceReportView) {
                    financeReportDto.setBenefitDays(-1);
                } else {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(beforeFinanceReportView.getRepayTime()),
                            new LocalDate(financeReportView.getRepayTime())).getDays());
                }
            }

            //Set Recommend Reward: Default value
            financeReportDto.setRecommendAmount("");

            InvestReferrerRewardModel investReferrerRewardModel = investReferrerRewardMapper.findByInvestIdAndReferrer(financeReportView.getInvestId(), financeReportView.getReferrer());
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
        List<FinanceReportView> financeReportViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportViews);

        List<FinanceReportDto> results = new ArrayList<>();
        for (int startIndex = (index - 1) * pageSize,
             endIndex = index * pageSize <= financeReportDtos.size() ? index * pageSize : financeReportDtos.size();
             startIndex < endIndex; ++startIndex) {
            results.add(financeReportDtos.get(startIndex));
        }

        return new BasePaginationDataDto<>(index, pageSize, financeReportDtos.size(), results);
    }

    @Override
    public List<List<String>> getFinanceReportCsvData(Long loanId, Integer period, String investLoginName,
                                                      Date investStartTime, Date investEndTime) {
        List<FinanceReportView> financeReportViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportViews);
        List<List<String>> csvData = new ArrayList<>();
        for (FinanceReportDto financeReportDto : financeReportDtos) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(financeReportDto);
            csvData.add(dtoStrings);
        }
        return csvData;
    }
}
