package com.tuotiansudai.api.service.impl;


import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppLoanDetailService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.mapper.LoanTitleRelationMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RandomUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MobileAppLoanDetailServiceImpl implements MobileAppLoanDetailService {
    static Logger log = Logger.getLogger(MobileAppLoanDetailServiceImpl.class);
    @Autowired
    private LoanMapper loanMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private LoanTitleRelationMapper loanTitleRelationMapper;

    @Autowired
    private RandomUtils randomUtils;

    @Value("${web.server}")
    private String domainName;

    private String title = "拓天速贷引领投资热，开启互金新概念";

    private String content = "个人经营借款理财项目，总额{0}元期限{1}{2}，年化利率{3}%，先到先抢！！！";

    @Override
    public BaseResponseDto generateLoanDetail(LoanDetailRequestDto loanDetailRequestDto) {
        BaseResponseDto<LoanDetailResponseDataDto> dto = new BaseResponseDto<LoanDetailResponseDataDto>();
        String loanId = loanDetailRequestDto.getLoanId();
        LoanModel loan = loanMapper.findById(Long.parseLong(loanId));

        if (loan == null) {
            log.info("标的详情" + ReturnMessage.LOAN_NOT_FOUND.getCode() + ":" + ReturnMessage.LOAN_NOT_FOUND.getMsg());
            return new BaseResponseDto(ReturnMessage.LOAN_NOT_FOUND.getCode(), ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }

        LoanDetailResponseDataDto loanDetailResponseDataDto = this.convertLoanDetailFromLoan(loan, loanDetailRequestDto.getBaseParam().getUserId());
        dto.setCode(ReturnMessage.SUCCESS.getCode());
        dto.setMessage(ReturnMessage.SUCCESS.getMsg());
        dto.setData(loanDetailResponseDataDto);
        return dto;
    }

    private LoanDetailResponseDataDto convertLoanDetailFromLoan(LoanModel loan, String loginName) {
        DecimalFormat decimalFormat = new DecimalFormat("######0.##");
        LoanDetailResponseDataDto loanDetailResponseDataDto = new LoanDetailResponseDataDto();
        loanDetailResponseDataDto.setLoanId("" + loan.getId());
        loanDetailResponseDataDto.setLoanType(loan.getProductType() != null ? loan.getProductType().getProductLine() : "");
        loanDetailResponseDataDto.setLoanName(loan.getName());
        String repayTypeName = loan.getType().getRepayType();
        String interestPointName = loan.getType().getInterestPointName();
        loanDetailResponseDataDto.setRepayTypeCode("");
        loanDetailResponseDataDto.setRepayTypeName(repayTypeName);
        loanDetailResponseDataDto.setInterestPointName(interestPointName);
        loanDetailResponseDataDto.setPeriods(loan.getPeriods());
        loanDetailResponseDataDto.setDeadline(loan.getPeriods());
        loanDetailResponseDataDto.setRepayUnit(loan.getType().getLoanPeriodUnit().getDesc());
        loanDetailResponseDataDto.setRatePercent(decimalFormat.format((loan.getBaseRate() + loan.getActivityRate()) * 100));
        loanDetailResponseDataDto.setLoanMoney(AmountConverter.convertCentToString(loan.getLoanAmount()));
        loanDetailResponseDataDto.setTitle(title);
        loanDetailResponseDataDto.setContent(MessageFormat.format(content,loanDetailResponseDataDto.getLoanMoney(),loanDetailResponseDataDto.getDeadline(),loanDetailResponseDataDto.getRepayUnit(),loanDetailResponseDataDto.getRatePercent()));
        if(LoanStatus.PREHEAT.equals(loan.getStatus())){
            loanDetailResponseDataDto.setLoanStatus(LoanStatus.RAISING.name().toLowerCase());
            loanDetailResponseDataDto.setLoanStatusDesc(LoanStatus.RAISING.getDescription());
        }else{
            loanDetailResponseDataDto.setLoanStatus(loan.getStatus().name().toLowerCase());
            loanDetailResponseDataDto.setLoanStatusDesc(loan.getStatus().getDescription());
        }

        loanDetailResponseDataDto.setAgent(loan.getAgentLoginName());
        loanDetailResponseDataDto.setLoaner(loan.getLoanerLoginName());
        loanDetailResponseDataDto.setInvestedCount(investMapper.countSuccessInvest(loan.getId()));
        loanDetailResponseDataDto.setRemainTime(calculateRemainTime(loan.getFundraisingEndTime(), loan.getStatus()));
        if (loan.getFundraisingStartTime() != null) {
            loanDetailResponseDataDto.setInvestBeginTime(new DateTime(loan.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
            loanDetailResponseDataDto.setVerifyTime(new DateTime(loan.getFundraisingStartTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        if(loan.getFundraisingEndTime() != null){
            loanDetailResponseDataDto.setFundRaisingEndTime(new DateTime(loan.getFundraisingEndTime()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        loanDetailResponseDataDto.setInvestBeginSeconds(CommonUtils.calculatorInvestBeginSeconds(loan.getFundraisingStartTime()));
        long investedAmount = investMapper.sumSuccessInvestAmount(loan.getId());
        loanDetailResponseDataDto.setInvestedMoney(AmountConverter.convertCentToString(investedAmount));
        loanDetailResponseDataDto.setBaseRatePercent(decimalFormat.format(loan.getBaseRate() * 100));
        loanDetailResponseDataDto.setActivityRatePercent(decimalFormat.format(loan.getActivityRate() * 100));
        loanDetailResponseDataDto.setLoanDetail(loan.getDescriptionHtml());
        loanDetailResponseDataDto.setEvidence(getEvidenceByLoanId(loan.getId()));

        List<InvestModel> investAchievements = investMapper.findInvestAchievementsByLoanId(loan.getId());
        StringBuffer marqueeTitle = new StringBuffer();
        if (CollectionUtils.isEmpty(investAchievements) && Lists.newArrayList(LoanStatus.RAISING, LoanStatus.PREHEAT).contains(loan.getStatus())) {
            marqueeTitle.append("第一个投资者将获得“拓荒先锋”称号及0.2％加息券＋50元红包    ");
        } else {
            for (InvestModel investModel : investAchievements) {
                String investorLoginName = randomUtils.encryptLoginName(loginName, investModel.getLoginName(), 3, investModel.getId());
                if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loan.getStatus() == LoanStatus.RAISING) {
                    marqueeTitle.append(investorLoginName + "以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loan.getId(), investModel.getLoginName())) + "元暂居标王，快来争夺吧    ");
                    marqueeTitle.append("目前项目剩余" + AmountConverter.convertCentToString(loan.getLoanAmount() - investedAmount) + "元，快来一锤定音获取奖励吧    ");
                } else if (investModel.getAchievements().contains(InvestAchievement.MAX_AMOUNT) && loan.getStatus() != LoanStatus.RAISING) {
                    marqueeTitle.append("恭喜" + investorLoginName + "以累计投资" + AmountConverter.convertCentToString(investMapper.sumSuccessInvestAmountByLoginName(loan.getId(), investModel.getLoginName())) + "元夺得标王，奖励0.5％加息券＋100元红包    ");
                } else if (investModel.getAchievements().contains(InvestAchievement.FIRST_INVEST)) {
                    marqueeTitle.append("恭喜" + investorLoginName + new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss") + "占领先锋，奖励0.2％加息券＋50元红包    ");
                } else if (investModel.getAchievements().contains(InvestAchievement.LAST_INVEST)){
                    marqueeTitle.append("恭喜" + investorLoginName + new DateTime(investModel.getTradingTime()).toString("yyyy-MM-dd HH:mm:ss") + "一锤定音，奖励0.2％加息券＋50元红包    ");
                }
            }
        }
        loanDetailResponseDataDto.setMarqueeTitle(marqueeTitle.toString());

        loanDetailResponseDataDto.setMinInvestMoney(AmountConverter.convertCentToString(loan.getMinInvestAmount()));
        loanDetailResponseDataDto.setMaxInvestMoney(AmountConverter.convertCentToString(loan.getMaxInvestAmount()));
        loanDetailResponseDataDto.setCardinalNumber(AmountConverter.convertCentToString(loan.getInvestIncreasingAmount()));
        if(loan.getRaisingCompleteTime() != null) {
            loanDetailResponseDataDto.setRaiseCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(loan.getRaisingCompleteTime()));
        }
        loanDetailResponseDataDto.setInvestFeeRate("" + loan.getInvestFeeRate());
        loanDetailResponseDataDto.setRaisingPeriod(String.valueOf(Days.daysBetween(new DateTime(loan.getFundraisingStartTime()).withTimeAtStartOfDay(),
                new DateTime(loan.getFundraisingEndTime()).withTimeAtStartOfDay()).getDays() + 1));
        return loanDetailResponseDataDto;

    }

    private List<EvidenceResponseDataDto> getEvidenceByLoanId(long loanId) {
        EvidenceResponseDataDto evidenceResponseDataDto;
        List<LoanTitleRelationModel> loanTitleRelationModels = loanTitleRelationMapper.findLoanTitleRelationAndTitleByLoanId(loanId);
        List<String> imageUrlList;
        List<EvidenceResponseDataDto> evidenceResponseDataDtos = Lists.newArrayList();
        for (LoanTitleRelationModel loanTitleRelationModel : loanTitleRelationModels) {
            imageUrlList = Lists.newArrayList();
            evidenceResponseDataDto = new EvidenceResponseDataDto();
            String materialUrl = loanTitleRelationModel.getApplicationMaterialUrls();
            if (StringUtils.isNotEmpty(materialUrl)) {
                for (String url : materialUrl.split(",")) {
                    String tempUrl = domainName + "/" +url;
                    imageUrlList.add(tempUrl);
                }
            }
            evidenceResponseDataDto.setTitle(loanTitleRelationModel.getTitle());
            evidenceResponseDataDto.setImageUrl(imageUrlList);
            evidenceResponseDataDtos.add(evidenceResponseDataDto);
        }

        return evidenceResponseDataDtos;

    }

    private String calculateRemainTime(Date fundraisingEndTime, LoanStatus status) {

        Long time = (fundraisingEndTime.getTime() - System
                .currentTimeMillis()) / 1000;

        if (time < 0 || !status.equals(LoanStatus.RAISING)) {
            return "已到期";
        }
        long days = time / 3600 / 24;
        long hours = (time / 3600) % 24;
        long minutes = (time / 60) % 60;
        if (minutes < 1) {
            minutes = 1L;
        }

        return days + "天" + hours + "时" + minutes + "分";
    }
}
