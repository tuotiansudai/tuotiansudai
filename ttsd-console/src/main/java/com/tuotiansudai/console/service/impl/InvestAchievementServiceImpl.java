package com.tuotiansudai.console.service.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.console.service.InvestAchievementService;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanAchievementView;
import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class InvestAchievementServiceImpl implements InvestAchievementService {

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public long findInvestAchievementCount(String loginName) {
        return loanMapper.findLoanAchievementCount(loginName);
    }

    @Override
    public List<LoanAchievementView> findInvestAchievement(int index, int pageSize, String loginName) {
        List<LoanAchievementView> loanAchievementViews = loanMapper.findLoanAchievement((index - 1) * pageSize, pageSize, loginName);
        return Lists.transform(loanAchievementViews, new Function<LoanAchievementView, LoanAchievementView>() {
            @Override
            public LoanAchievementView apply(LoanAchievementView input) {
                if (input.getRaisingCompleteTime() == null) {
                    input.setCompleteInvestDuration("/");
                } else {
                    input.setCompleteInvestDuration(duration(new DateTime(input.getFundraisingStartTime()), new DateTime(input.getRaisingCompleteTime())));
                }
                Date firstInvestDate = investMapper.findFirstTradeTimeInvestByLoanId(input.getLoanId());
                input.setFirstInvestDuration(duration(new DateTime(input.getFundraisingStartTime()), new DateTime(firstInvestDate)));
                return input;
            }
        });
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
