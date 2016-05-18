package com.tuotiansudai.service.impl;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.InvestAchievementDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.InvestAchievement;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.service.InvestAchievementService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class InvestAchievementServiceImpl implements InvestAchievementService{

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public long findInvestAchievementManageCount(String loginName) {
        if (StringUtils.isEmpty(loginName)) {
            return loanMapper.findLoanAchievementCount();
        } else {
            return investMapper.findInvestAchievementCount(loginName);
        }
    }

    @Override
    public List<InvestAchievementDto> findInvestAchievementManage(int index, int pageSize, String loginName) {
        List<LoanModel> loanModels = Lists.newArrayList();
        List<InvestModel> investModels;
        List<InvestAchievementDto> investAchievementDtos = Lists.newArrayList();
        if (StringUtils.isEmpty(loginName)) {
            loanModels = loanMapper.findLoanAchievement((index - 1) * pageSize, pageSize);
            for (LoanModel loanModel : loanModels) {
                investModels = investMapper.findInvestAchievement(null, loanModel.getId());
                investAchievementDtos.add(convertToInvestAchievementDto(loanModel, investModels));
            }
        } else {
            investModels = investMapper.findInvestAchievement(loginName, null);
            HashSet<Long> longHashSet = new HashSet<>();
            for (InvestModel investModel : investModels) {
                if (!longHashSet.contains(investModel.getLoanId())) {
                    loanModels.add(loanMapper.findById(investModel.getLoanId()));
                    longHashSet.add(investModel.getLoanId());
                }
            }
            HashSet<LoanModel> hashSet = new HashSet<>(loanModels);
            loanModels.clear();
            loanModels.addAll(hashSet);
            sortLoanModel(loanModels);
            if (loanModels.size() >= pageSize) {
                loanModels = loanModels.subList((index - 1) * pageSize, (index - 1) * pageSize + pageSize);
            }
            for (LoanModel loanModel : loanModels) {
                investModels = investMapper.findInvestAchievement(loginName, loanModel.getId());
                investAchievementDtos.add(convertToInvestAchievementDto(loanModel, investModels));
            }
        }
        return investAchievementDtos;
    }

    private void sortLoanModel(List<LoanModel> loanModels) {
        Collections.sort(loanModels, new Comparator<LoanModel>() {
            @Override
            public int compare(LoanModel first, LoanModel second) {
                return -first.getCreatedTime().compareTo(second.getCreatedTime());
            }
        });
    }

    private InvestAchievementDto convertToInvestAchievementDto(LoanModel loanModel, List<InvestModel> investModels) {
        InvestAchievementDto investAchievementDto = new InvestAchievementDto();
        investAchievementDto.setName(loanModel.getName());
        investAchievementDto.setLoanStatus(loanModel.getStatus());
        investAchievementDto.setPeriods(loanModel.getPeriods());
        investAchievementDto.setLoanAmount(loanModel.getLoanAmount());
        investAchievementDto.setRaisingCompleteTime(loanModel.getRaisingCompleteTime());
        DateTime fundraisingStartTime = new DateTime(loanModel.getFundraisingStartTime());
        for (InvestModel investModel : investModels) {
            if (investModel.getAchievements().contains(InvestAchievement.FIRST_INVEST) && investModel.getTradingTime() != null) {
                DateTime tradingTime = new DateTime(investModel.getTradingTime());
                investAchievementDto.setWhenFirstInvest(duration(fundraisingStartTime, tradingTime));
                investAchievementDto.setFirstInvestLoginName(investModel.getLoginName());
                investAchievementDto.setFirstInvestAmount(investModel.getAmount());
            } else if (investModel.getAchievements().contains(InvestAchievement.LAST_INVEST)){
                investAchievementDto.setLastInvestLoginName(investModel.getLoginName());
                investAchievementDto.setLastInvestAmount(investModel.getAmount());
            } else {
                investAchievementDto.setMaxAmountLoginName(investModel.getLoginName());
                investAchievementDto.setMaxAmount(investModel.getAmount());
            }
        }
        if (loanModel.getRaisingCompleteTime() != null) {
            DateTime raisingCompleteTime = new DateTime(loanModel.getRaisingCompleteTime());
            investAchievementDto.setWhenCompleteInvest(duration(fundraisingStartTime, raisingCompleteTime));
        }
        return investAchievementDto;
    }

    private String duration(DateTime startTime, DateTime endTime) {
        StringBuffer stringBuffer = new StringBuffer();
        int days = Days.daysBetween(startTime, endTime).getDays();
        if (days > 0) {
            stringBuffer.append(days + "天");
        }
        int hours = Hours.hoursBetween(startTime, endTime).getHours() - days * 24;
        if (hours > 0) {
            stringBuffer.append(hours + "小时");
        }
        int minutes = Minutes.minutesBetween(startTime, endTime).getMinutes() - (days * 24 + hours) * 60;
        if (minutes > 0) {
            stringBuffer.append(minutes + "分");
        }
        int seconds = Seconds.secondsBetween(startTime, endTime).getSeconds() - ((days * 24 + hours) * 60 + minutes) * 60;
        if (seconds > 0) {
            stringBuffer.append(seconds + "秒");
        }
        return stringBuffer.toString();
    }

}
