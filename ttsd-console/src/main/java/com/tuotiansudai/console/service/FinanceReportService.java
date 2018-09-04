package com.tuotiansudai.console.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.CouponMapper;
import com.tuotiansudai.repository.mapper.CouponRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.CouponModel;
import com.tuotiansudai.repository.model.CouponRepayModel;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.FinanceReportDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.FinanceReportMapper;
import com.tuotiansudai.repository.mapper.InvestReferrerRewardMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.model.*;
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
public class FinanceReportService {

    static Logger logger = Logger.getLogger(FinanceReportService.class);

    @Autowired
    private FinanceReportMapper financeReportMapper;

    @Autowired
    private InvestReferrerRewardMapper investReferrerRewardMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponRepayMapper couponRepayMapper;

    public BasePaginationDataDto<FinanceReportDto> getFinanceReportDtos(Long loanId, Integer period, String investLoginName,
                                                                        Date investStartTime, Date investEndTime, PreferenceType preferenceType,
                                                                        Date repayStartTime, Date repayEndTime,
                                                                        int index, int pageSize) {
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime, repayStartTime, repayEndTime, preferenceType, (index - 1) * pageSize, pageSize);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportItemViews);
        int count = financeReportMapper.findCountFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime, repayStartTime, repayEndTime, preferenceType);
        return new BasePaginationDataDto<>(index, pageSize, count, financeReportDtos);
    }

    public List<List<String>> getFinanceReportCsvData(Long loanId, Integer period, String investLoginName,
                                                      Date investStartTime, Date investEndTime, PreferenceType preferenceType, Date repayStartTime, Date repayEndTime) {
        //直接导出所有内容，所以index = 1, pageSize = 9999999
        final int index = 1;
        final int pageSize = 9999999;
        List<FinanceReportItemView> financeReportItemViews = financeReportMapper.findFinanceReportViews(loanId, period, investLoginName, investStartTime, investEndTime, repayStartTime, repayEndTime, preferenceType, (index - 1) * pageSize, pageSize);
        List<FinanceReportDto> financeReportDtos = combineFinanceReportDtos(financeReportItemViews);
        List<List<String>> csvData = new ArrayList<>();
        for (FinanceReportDto financeReportDto : financeReportDtos) {
            List<String> dtoStrings = ExportCsvUtil.dtoToStringList(financeReportDto);
            csvData.add(dtoStrings);
        }
        return csvData;
    }

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
                if (Lists.newArrayList(Role.SD_STAFF, Role.ZC_STAFF).contains(investReferrerRewardModel.getReferrerRole())) {
                    financeReportDto.setReferrer(financeReportDto.getReferrer() + "(业务员)");
                }
                if (1 == financeReportDto.getPeriod()) {
                    financeReportDto.setRecommendAmount(AmountConverter.convertCentToString(investReferrerRewardModel.getAmount()));
                }
            }

            //Set Coupon Detail
            CouponModel couponModel = couponMapper.findById(financeReportItemView.getCouponId());
            if (null != couponModel) {
                long couponActualInterest = 0;
                if (couponModel.getCouponType().equals(CouponType.RED_ENVELOPE)) {
                    List<UserCouponModel> userCouponModels = userCouponMapper.findUserCouponSuccessByInvestId(financeReportItemView.getInvestId());
                    for (UserCouponModel userCouponModel : userCouponModels) {
                        couponActualInterest += userCouponModel.getActualInterest();
                    }
                } else {
                    List<CouponRepayModel> couponRepayModels = couponRepayMapper.findByUserCouponByInvestId(financeReportItemView.getInvestId());
                    for (CouponRepayModel couponRepayModel : couponRepayModels) {
                        couponActualInterest += couponRepayModel.getActualInterest();
                    }
                }
                financeReportDto.setCouponActualInterest(couponActualInterest);
                financeReportDto.setCouponDetail(couponModel);
            }

            financeReportDtos.add(financeReportDto);
        }
        return financeReportDtos;
    }
}
