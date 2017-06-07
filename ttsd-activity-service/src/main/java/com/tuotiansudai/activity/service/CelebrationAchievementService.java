package com.tuotiansudai.activity.service;

import com.tuotiansudai.dto.LoanItemDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.service.LoanService;
import com.tuotiansudai.util.MobileEncryptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CelebrationAchievementService {

    static Logger logger = Logger.getLogger(CelebrationAchievementService.class);
    @Autowired
    private LoanService loanService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.endTime}\")}")
    private Date endTime;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;


    public List<LoanItemDto> celebrationAchievementList() {
        List<LoanItemDto> loanItemList = loanService.findLoanItems(null, null, 0, 0, 0, 0, 1);
        loanItemList = loanItemList.size() > 3 ? loanItemList.subList(0, 3) : loanItemList;

        return loanItemList.stream()
                .filter(loanItemDto -> loanItemDto.getFundraisingStartTime().after(startTime)
                        && loanItemDto.getFundraisingStartTime().before(endTime)
                        && (loanItemDto.getStatus() == LoanStatus.RAISING || loanItemDto.getStatus() == LoanStatus.PREHEAT))
                .collect(Collectors.toList());

    }

    public List<InvestAchievementView> obtainCelebrationAchievement(long loanId) {
        return investMapper.findAmountOrderByLoanId(loanId, startTime, endTime);

    }

    public String encryptMobileForWeb(String loginName,String encryptLoginName, String encryptMobile) {
        if (encryptLoginName.equalsIgnoreCase(loginName)) {
            return encryptMobile;
        }
        return MobileEncryptor.encryptMiddleMobile(encryptMobile);
    }
}
