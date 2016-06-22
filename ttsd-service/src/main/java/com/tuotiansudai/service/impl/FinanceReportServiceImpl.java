package com.tuotiansudai.service.impl;

import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.repository.mapper.FinanceReportMapper;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.model.FinanceReportItemView;
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

    private FinanceReportItemView getBeforeFinanceReportModel(List<FinanceReportItemView> financeReportItemViews, int index,
                                                              List<FinanceReportItemView> beforeFinanceReportItemViews) {
        FinanceReportItemView currentFinanceReportItemView = financeReportItemViews.get(index);
        if (1 == currentFinanceReportItemView.getPeriod()) {
            return currentFinanceReportItemView;
        }
        for (int i = index - 1; i >= 0; --i) {
            FinanceReportItemView financeReportItemView = financeReportItemViews.get(i);
            if ((financeReportItemView.getInvestId() == currentFinanceReportItemView.getInvestId()) &&
                    financeReportItemView.getPeriod() == currentFinanceReportItemView.getPeriod() - 1) {
                return financeReportItemView;
            }
        }
        for (int i = index + 1; i < financeReportItemViews.size(); ++i) {
            FinanceReportItemView financeReportItemView = financeReportItemViews.get(i);
            if ((financeReportItemView.getInvestId() == currentFinanceReportItemView.getInvestId()) &&
                    financeReportItemView.getPeriod() == currentFinanceReportItemView.getPeriod() - 1) {
                return financeReportItemView;
            }
        }
        for (FinanceReportItemView financeReportItemView : beforeFinanceReportItemViews) {
            if ((financeReportItemView.getInvestId() == currentFinanceReportItemView.getInvestId()) &&
                    financeReportItemView.getPeriod() == currentFinanceReportItemView.getPeriod() - 1) {
                return financeReportItemView;
            }
        }
        return null;
    }

    private List<FinanceReportDto> combineFinanceReportDtos(List<FinanceReportItemView> financeReportItemViews, List<FinanceReportItemView> beforeFinanceReportItemViews) {
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
                FinanceReportItemView beforeFinanceReportItemView = getBeforeFinanceReportModel(financeReportItemViews,
                        financeReportItemViews.indexOf(financeReportItemView), beforeFinanceReportItemViews);
                if (null == beforeFinanceReportItemView) {
                    financeReportDto.setBenefitDays(-1);
                } else {
                    financeReportDto.setBenefitDays(Days.daysBetween(new LocalDate(beforeFinanceReportItemView.getRepayTime()),
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
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime);
        List<FinanceReportDto> financeReportDtos;
        if (null == period || 1 == period) {
            financeReportDtos = combineFinanceReportDtos(financeReportItemViews, null);
        } else {
            List<FinanceReportItemView> beforeFinanceReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period - 1, investLoginName, investStartTime, investEndTime);
            financeReportDtos = combineFinanceReportDtos(financeReportItemViews, beforeFinanceReportItemViews);
        }

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
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime);
        List<FinanceReportDto> financeReportDtos;
        if (null == period || 1 == period) {
            financeReportDtos = combineFinanceReportDtos(financeReportItemViews, null);
        } else {
            List<FinanceReportItemView> beforeFinanceReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period - 1, investLoginName, investStartTime, investEndTime);
            financeReportDtos = combineFinanceReportDtos(financeReportItemViews, beforeFinanceReportItemViews);
        }
        List<List<String>> csvData = new ArrayList<>();
        for (FinanceReportDto financeReportDto : financeReportDtos) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(financeReportDto);
            csvData.add(dtoStrings);
        }
        return csvData;
    }
}
