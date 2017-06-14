package com.tuotiansudai.activity.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.CelebrationLoanItemDto;
import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.mapper.ExtraLoanRateMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CelebrationAchievementService {

    static Logger logger = Logger.getLogger(CelebrationAchievementService.class);


    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.endTime}\")}")
    private Date endTime;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private ExtraLoanRateMapper extraLoanRateMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;


    public List<CelebrationLoanItemDto> celebrationAchievementList() {
        List<CelebrationLoanItemDto> loanItemList = this.findLoanItems(null, null, 0, 0, 0, 0, 1);
        loanItemList = loanItemList.size() > 4 ? loanItemList.subList(1, 4) : loanItemList;

        return loanItemList.stream()
                .filter(loanItemDto -> loanItemDto.getFundraisingStartTime() != null && loanItemDto.getFundraisingStartTime().after(startTime)
                        && loanItemDto.getFundraisingStartTime().before(endTime)
                        && (loanItemDto.getStatus() == LoanStatus.RAISING || loanItemDto.getStatus() == LoanStatus.PREHEAT))
                .collect(Collectors.toList());

    }

    public List<InvestAchievementView> obtainCelebrationAchievement(long loanId) {
        return investMapper.findAmountOrderByLoanId(loanId, startTime, endTime);
    }

    public String encryptMobileForWeb(String loginName, String encryptLoginName, String encryptMobile) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return encryptMobile;
        }
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
    }

    public List<CelebrationLoanItemDto> findLoanItems(String name, LoanStatus status, double rateStart, double rateEnd, int durationStart, int durationEnd, int index) {
        index = (index - 1) * 10;

        List<LoanModel> loanModels = loanMapper.findLoanListWeb(name, status, rateStart, rateEnd, durationStart, durationEnd, index);

        return Lists.transform(loanModels, loanModel -> {
            CelebrationLoanItemDto loanItemDto = new CelebrationLoanItemDto();
            loanItemDto.setId(loanModel.getId());
            loanItemDto.setName(loanModel.getName());
            loanItemDto.setProductType(loanModel.getProductType());
            loanItemDto.setBaseRate(new BigDecimal(loanModel.getBaseRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            loanItemDto.setActivityRate(new BigDecimal(loanModel.getActivityRate() * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            loanItemDto.setPeriods(loanModel.getPeriods());
            loanItemDto.setType(loanModel.getType());
            loanItemDto.setStatus(loanModel.getStatus());
            loanItemDto.setLoanAmount(loanModel.getLoanAmount());
            loanItemDto.setActivityType(loanModel.getActivityType());
            loanItemDto.setMinInvestAmount(loanModel.getMinInvestAmount());
            BigDecimal loanAmountBigDecimal = new BigDecimal(loanModel.getLoanAmount());
            BigDecimal sumInvestAmountBigDecimal = new BigDecimal(investMapper.sumSuccessInvestAmount(loanModel.getId()));
            if (LoanStatus.PREHEAT == loanModel.getStatus()) {

                loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                loanItemDto.setProgress(0.0);
                loanItemDto.setPreheatSeconds((loanModel.getFundraisingStartTime().getTime() - System.currentTimeMillis()) / 1000);
            }
            if (LoanStatus.RAISING == loanModel.getStatus()) {
                loanItemDto.setAlert(MessageFormat.format("{0} 元", AmountConverter.convertCentToString(loanModel.getLoanAmount() - investMapper.sumSuccessInvestAmount(loanModel.getId()))));
                if (loanModel.getProductType() != ProductType.EXPERIENCE) {
                    loanItemDto.setProgress(sumInvestAmountBigDecimal.divide(loanAmountBigDecimal, 4, BigDecimal.ROUND_DOWN).multiply(new BigDecimal(100)).doubleValue());
                }
            }
            loanItemDto.setFundraisingStartTime(loanModel.getFundraisingStartTime());

            loanItemDto.setDuration(loanModel.getDuration());
            double rate = extraLoanRateMapper.findMaxRateByLoanId(loanModel.getId());
            List<Source> extraSource = Lists.newArrayList();
            boolean activity = false;
            String activityDesc = "";
            LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(loanModel.getId());
            if (loanDetailsModel != null) {
                extraSource = loanDetailsModel.getExtraSource();
                activity = loanDetailsModel.isActivity();
                activityDesc = loanDetailsModel.getActivityDesc();
            }
            if (rate > 0) {
                loanItemDto.setExtraRate(rate * 100);
                loanItemDto.setExtraSource((extraSource.size() == 1 && extraSource.contains(Source.MOBILE)) ? Source.MOBILE.name() : "");
            }
            loanItemDto.setActivity(activity);
            loanItemDto.setActivityDesc(activityDesc);
            loanItemDto.setAchievementViews(obtainCelebrationAchievement(loanItemDto.getId()));
            return loanItemDto;
        });
    }


}
